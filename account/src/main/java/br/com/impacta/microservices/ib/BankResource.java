package br.com.impacta.microservices.ib;

import java.math.BigDecimal;
import java.net.URI;
import java.util.EmptyStackException;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlow;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlows;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import br.com.impacta.microservices.ib.enums.FallbackClientMessages;
import br.com.impacta.microservices.ib.interfaces.BalanceRestClient;
import br.com.impacta.microservices.ib.interfaces.CreditCardRestClient;
import br.com.impacta.microservices.ib.interfaces.CreditRestClient;
import br.com.impacta.microservices.ib.interfaces.DebitRestClient;
import br.com.impacta.microservices.ib.interfaces.InvestmentsRestClient;
import br.com.impacta.microservices.ib.model.Balance;
import br.com.impacta.microservices.ib.model.Client;
import br.com.impacta.microservices.ib.model.Credit;
import br.com.impacta.microservices.ib.model.CreditCard;
import br.com.impacta.microservices.ib.model.CreditCardClient;
import br.com.impacta.microservices.ib.model.Debit;
import br.com.impacta.microservices.ib.model.Investment;
import br.com.impacta.microservices.ib.model.TesouroDireto;
import br.com.impacta.microservices.ib.services.ClientService;


@Path("/admin")
@Tag(name="Banco - Administra????o", description="API de Gerenciamento Administrativo de clientes")
@SecurityScheme(
    securitySchemeName = "quarkus",
    type = SecuritySchemeType.OAUTH2,
    flows = @OAuthFlows(
        password = @OAuthFlow(
            tokenUrl = "http://microservices-impacta-keycloak.com/auth/realms/Quarkus/protocol/openid-connect/token"
        )
    )
)
public class BankResource {
    @Inject
    JsonWebToken jwt;

    @Inject
    ClientService clientService;

    @Inject
    @RestClient
    BalanceRestClient balanceRestClient;


    @Inject
    @RestClient
    DebitRestClient debitRestClient;

    @Inject
    @RestClient
    CreditRestClient creditRestClient;

    @Inject
    @RestClient
    InvestmentsRestClient investmentsRestClient;

    @Inject
    @RestClient
    CreditCardRestClient creditCardRestClient;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/token")
    @Operation(
        summary = "Visualiza????o do token que est?? sendo utilizado."
    )
    @RolesAllowed("admin")
    public String admin() {
        return "Access for subject " + jwt.getSubject() + " is granted";
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/client/add")
    @Operation(
        summary = "Adiciona cliente, com cr??dito inicial de R$ 5000,00"
    )
    @RolesAllowed("admin")
    @Counted(name = "countAddClient", description = "Count how many times the AddClient has been invoked")
    @Timed(
        name = "timeAddClient",
        description = "How long it takes to invoke the AddClient",
        unit = MetricUnits.MILLISECONDS)
    @Metered(name = "meteredGetAll", description = "Measures throughput of getAll method")
    public Client addClient(Client client){
        
        // cr??dito inicial do cliente - 5000
        Credit initialClientCredit = new Credit();
        initialClientCredit.setClientCpf(client.getCpf());
        initialClientCredit.setCredit(new BigDecimal(5000));
        creditRestClient.addCredit(initialClientCredit);

        // d??bito inicial do cliente - 0
        Debit initialClientDebit = new Debit();
        initialClientDebit.setClientCpf(client.getCpf());
        initialClientDebit.setDebit(new BigDecimal(-1));
        debitRestClient.addDebit(initialClientDebit);

        // seta o valor do extrato e saldo
        Balance newBalance = balanceRestClient.getBalance();
        client.setBalance(newBalance);
        Balance.persist(newBalance);
        
        // adiciona o cliente
        Client clientEntity = clientService.addClient(client);
        return clientEntity;
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Adiciona todos os tesouros diretos"
    )
    @Path("/tesouro-direto")
    @RolesAllowed("admin")
    @Counted(name = "countCreateTesouroDireto", description = "Count how many times the CreateTesouroDireto has been invoked")
    @Timed(
        name = "timeCreateTesouroDireto",
        description = "How long it takes to invoke the CreateTesouroDireto",
        unit = MetricUnits.MILLISECONDS)
    @Metered(name = "meteredCreateTesouroDireto", description = "Measures throughput of CreateTesouroDireto method")
    public List<TesouroDireto> createTesouroDireto(List<TesouroDireto> tesouroDiretos){
        investmentsRestClient.createTesouroDireto(tesouroDiretos);
        return tesouroDiretos;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Retorna todos os clientes do banco"
    )
    @Path("/clients")
    @RolesAllowed("admin")
    @Counted(name = "countGetAllClients", description = "Count how many times the GetAllClients has been invoked")
    @Timed(
        name = "timeGetAllClients",
        description = "How long it takes to invoke the GetAllClients",
        unit = MetricUnits.MILLISECONDS)
    @Metered(name = "meteredGetAllClients", description = "Measures throughput of GetAllClients method")
    public List<Client> getAllClients(){
        return clientService.listClient();
    }

    @GET
    @Operation(
        summary = "Retorna detalhes do cliente por n??mero da conta"
    )
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/clients/{account}")
    @Timeout(5000)
    @Retry(maxRetries = 5)
    @RolesAllowed("admin")
    @Fallback(fallbackMethod = "fallbackGetClient", applyOn = EmptyStackException.class)
    @Counted(name = "countGetClient", description = "Count how many times the GetClient has been invoked")
    @Timed(
        name = "timeGetClient",
        description = "How long it takes to invoke the GetClient",
        unit = MetricUnits.MILLISECONDS)
    @Metered(name = "meteredGetClient", description = "Measures throughput of GetClient method")
    public Response getClient(@PathParam("account") int account){
        Client clientEntity = clientService.getClientByAccount(account);
        if(clientEntity == null){
            throw new EmptyStackException();
        }
        return Response.ok(clientEntity).build();
    }



    @GET
    @Operation(
        summary = "Retorna a lista de investimentos realizados"
    )
    @Path("/investments")
    @Timeout(5000)
    @Retry(maxRetries = 5)
    @Fallback(fallbackMethod = "fallbackGetAllInvestments")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    @RolesAllowed("admin")
    @Counted(name = "countGetInvestments", description = "Count how many times the GetInvestments has been invoked")
    @Timed(
        name = "timeGetInvestments",
        description = "How long it takes to invoke the GetInvestments",
        unit = MetricUnits.MILLISECONDS)
    @Metered(name = "meteredGetInvestments", description = "Measures throughput of GetInvestments method")
    public Response getInvestments() {
        List<Investment> investments = investmentsRestClient.getInvestments();
        return Response.ok(investments).build();
    }

    @POST
    @Operation(
        summary = "Adiciona cart??o de cr??dito na conta do cliente"
    )
    @Transactional
    @Path("/{cpf}/add-credit-card")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    @Counted(name = "countAddCreditCard", description = "Count how many times the AddCreditCard has been invoked")
    @Timed(
        name = "timeAddCreditCard",
        description = "How long it takes to invoke the AddCreditCard",
        unit = MetricUnits.MILLISECONDS)
    @Metered(name = "meteredAddCreditCard", description = "Measures throughput of AddCreditCard method")
    public Response addCreditCard(@PathParam("cpf") int cpf, CreditCard creditCard) {
        Client client = clientService.getClientByCpf(cpf);
        CreditCardClient creditCardClient = new CreditCardClient();

        creditCardClient.setCpf(client.getCpf());
        creditCardClient.setFirstName(client.getFirstName());
        creditCardClient.setLastName(client.getLastName());

        creditCard.setClient(creditCardClient);
        creditCard.setClientName(client.getFirstName() + " " + client.getLastName());

        // balan??o padr??o para cria????o de cart??o de cr??dito
        creditCard.setCardBalance(new BigDecimal(0));

        // cr??dito inicial liberado no cart??o de cr??dito
        creditCard.setSpendingLimit(new BigDecimal(2500));

        creditCardRestClient.addCreditCard(creditCard);
        return Response.ok(creditCard).build();
    }


    private Response fallbackGetAllInvestments(){
        return Response.serverError()
                .header("erro", FallbackClientMessages.GET_ALL_INVESTMENTS.getDescription())
                .build();
    }

    private Response fallbackGetClient(@PathParam("account") int account){
        return Response.serverError()
                .build();
    }

}