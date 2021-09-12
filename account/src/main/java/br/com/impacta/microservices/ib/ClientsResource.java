package br.com.impacta.microservices.ib;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
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
import br.com.impacta.microservices.ib.interfaces.InvestmentsRestClient;
import br.com.impacta.microservices.ib.model.Balance;
import br.com.impacta.microservices.ib.model.Client;
import br.com.impacta.microservices.ib.model.InvestimentClient;
import br.com.impacta.microservices.ib.model.Investment;
import br.com.impacta.microservices.ib.model.TesouroDireto;
import br.com.impacta.microservices.ib.services.ClientService;

@Path("/client")
@Tag(name="Cliente", description="Acesso a conta, aderir a investimentos e pagar cartão de crédito.")
@SecurityScheme(
    securitySchemeName = "quarkus",
    type = SecuritySchemeType.OAUTH2,
    flows = @OAuthFlows(
        password = @OAuthFlow(
            tokenUrl = "http://localhost:10520/auth/realms/Quarkus/protocol/openid-connect/token"
        )
    )
)
public class ClientsResource {

    @Inject
    ClientService clientService;

    @Inject
    @RestClient
    InvestmentsRestClient investmentsRestClient;

    @Inject
    @RestClient
    CreditCardRestClient creditCardRestClient;

    @Inject
    @RestClient
    BalanceRestClient balanceRestClient;
    
    @POST
    @Path("/{cpf}")
    @Operation(
        summary = "Retorna os dados do cliente pela busca via CPF"
    )
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed({"admin", "user"})
    public String me(@PathParam("cpf") int cpf) {
        Balance newBalance = balanceRestClient.getBalance();
        Client clientEntity = clientService.getClientByCpf(cpf);
        List<Investment> invst = investmentsRestClient.getInvestments();

        List<TesouroDireto> tesouroDireto = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        clientEntity.setBalance(newBalance);
        String clientBalance = NumberFormat.getCurrencyInstance()
                                            .format(clientEntity.getBalance()
                                                            .getBalance()
                                                            .doubleValue());
        StringBuilder result  = new StringBuilder() ;
        result.append(clientEntity.getFirstName().toString() + ", seja bem-vindo ao Banco X. \n"
                + "Seu saldo atual é de R$ " + clientBalance + "\n\n"                                            
                + "Investimentos: \n"
                + "------------------------- \n");
                
        invst.forEach(in -> {
            tesouroDireto.add(clientService.getTesouroByCode(in.getCodeTesouroDireto()));
        });

        tesouroDireto.forEach(t -> {
            result.append(
            "Nome: " + t.getNm() + "\n"
            + "Data de encerramento: " + sdf.format(t.getMtrtyDt()) + "\n"
            + "Valor mínimo de investimento: " + t.getMinInvstmtAmt().toString() + "\n"
            + "Valor investido: " + 
            invst.parallelStream()
                 .filter(i -> i.getCodeTesouroDireto() == t.getCd())
                 .findFirst()
                 .get()
                 .getInvestmentValue() + "\n"
            + "------------------------- \n");
        });

        return result.toString();

    }

    @GET
    @Operation(
        summary = "Retorna a lista de tesouros diretos disponíveis para investimento"
    )
    @Path("/tesouro-direto")
    @Timeout(5000)
    @Retry(maxRetries = 5)
    @Fallback(fallbackMethod = "fallbackGetAllTesouroDireto")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin", "user"})
    public Response getTesouroDiretos() {
        List<TesouroDireto> tesouroDiretos = investmentsRestClient.getAllTesouroDireto();
        return Response.ok(tesouroDiretos).build();
    }

    @PUT
    @Operation(
        summary = "Adiciona investimento na conta do cliente"
    )
    @Transactional
    @Path("/{cpf}/add-investment")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin", "user"})
    public Investment addInvestment(@PathParam("cpf") int cpf, Investment investment) {
        Client client = clientService.getClientByCpf(cpf);
        InvestimentClient invstClient = new InvestimentClient(
            client.getFirstName(),
            client.getLastName(),
            client.getAccountNumber(),
            client.getCpf()
        );
        invstClient.setInvestimentValue(investment.getInvestmentValue());
        investment.setClient(invstClient);
        investmentsRestClient.addInvestment(investment);
        return investment;
    }

    private Response fallbackGetAllTesouroDireto(){
        return Response.serverError()
                .header("erro", FallbackClientMessages.GET_ALL_TESOURO_DIRETO.getDescription())
                .build();
    }
    
}