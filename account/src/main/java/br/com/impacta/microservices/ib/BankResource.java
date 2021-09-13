package br.com.impacta.microservices.ib;

import java.math.BigDecimal;
import java.net.URI;
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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlow;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlows;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import br.com.impacta.microservices.ib.enums.FallbackClientMessages;
import br.com.impacta.microservices.ib.enums.FallbackCreditCard;
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
@Tag(name="Banco - Administração", description="API de Gerenciamento Administrativo de clientes")
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
        summary = "Visualização do token que está sendo utilizado."
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
        summary = "Adiciona cliente, com crédito inicial de R$ 5000,00"
    )
    @RolesAllowed("admin")
    public Client addClient(Client client){
        
        // crédito inicial do cliente - 5000
        Credit initialClientCredit = new Credit();
        initialClientCredit.setClientCpf(client.getCpf());
        initialClientCredit.setCredit(new BigDecimal(5000));
        creditRestClient.addCredit(initialClientCredit);

        // débito inicial do cliente - 0
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
    public List<Client> getAllClients(){
        return clientService.listClient();
    }

    @GET
    @Operation(
        summary = "Retorna detalhes do cliente por número da conta"
    )
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/clients/{account}")
    @RolesAllowed("admin")
    public Client getClient(@PathParam("account") int account){
        Client clientEntity = new Client();
        clientEntity.setAccountNumber(account);
        clientEntity = clientService.getClientByAccount(clientEntity);
        return clientEntity;
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
    public Response getInvestments() {
        List<Investment> investments = investmentsRestClient.getInvestments();
        return Response.ok(investments).build();
    }

    @POST
    @Operation(
        summary = "Adiciona cartão de crédito na conta do cliente"
    )
    @Transactional
    @Timeout(5000)
    @Retry(maxRetries = 5)
    @Path("/{cpf}/add-credit-card")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Fallback(fallbackMethod = "fallbackAddCreditCard")
    @RolesAllowed("admin")
    public Response addCreditCard(@PathParam("cpf") int cpf, CreditCard creditCard) {
        Client client = clientService.getClientByCpf(cpf);
        CreditCardClient creditCardClient = new CreditCardClient();

        creditCardClient.setCpf(client.getCpf());
        creditCardClient.setFirstName(client.getFirstName());
        creditCardClient.setLastName(client.getLastName());

        creditCard.setClient(creditCardClient);
        creditCard.setClientName(client.getFirstName() + " " + client.getLastName());

        // balanço padrão para criação de cartão de crédito
        creditCard.setCardBalance(new BigDecimal(0));

        // crédito inicial liberado no cartão de crédito
        creditCard.setSpendingLimit(new BigDecimal(2500));

        creditCardRestClient.addCreditCard(creditCard);
        return Response.created(
            URI.create("credit card created")
        ).build();
    }


    private Response fallbackGetAllInvestments(){
        return Response.serverError()
                .header("erro", FallbackClientMessages.GET_ALL_INVESTMENTS.getDescription())
                .build();
    }
    private Response fallbackAddCreditCard(@PathParam("cpf") int cpf, CreditCard creditCard){
        return Response.serverError()
                .header("erro", FallbackCreditCard.ADD_CREDIT_CARD.getDescription())
                .build();
    }

}