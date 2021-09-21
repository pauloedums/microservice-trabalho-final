package br.com.impacta.microservices.ib;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.faulttolerance.Fallback;
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
import br.com.impacta.microservices.ib.model.CreditCard;
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
            tokenUrl = "http://microservices-impacta-keycloak.com/auth/realms/Quarkus/protocol/openid-connect/token"
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
    @Fallback(fallbackMethod = "fallbackGetCLientInvestments", applyOn = EmptyStackException.class)
    @RolesAllowed({"admin", "user"})
    public Response me(@PathParam("cpf") int cpf) {
        Balance newBalance = new Balance();
        Client clientEntity = new Client();
        List<Investment> invst = new ArrayList<>();
        List<CreditCard> creditCards = new ArrayList<>();
        List<TesouroDireto> tesouroDireto = new ArrayList<>();
        StringBuilder result  = new StringBuilder() ;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        if(Client.listAll().size() > 0 ){
            final Client clientEntityDTO = clientService.getClientByCpf(cpf);
            clientEntity = clientEntityDTO;
        }
        if(Balance.listAll().size() > 0 ){
            final Balance newBalanceRest = balanceRestClient.getBalance();
            newBalance.setBalance(newBalanceRest.getBalance());
            if(newBalance.getBalance().signum() <= 0 || clientEntity == null){
                throw new EmptyStackException();
            }
        }

        if(investmentsRestClient.getInvestments().size() > 0) {
            final List<Investment> invstmentsRestList = investmentsRestClient.getInvestments();
            for (Investment investment : invstmentsRestList) {
                invst.add(investment);
            }
        }
        if(creditCardRestClient.getAll().size() > 0 ) {
            final List<CreditCard> creditCardsRestList = creditCardRestClient.getAll();
            creditCards.addAll(creditCardsRestList);
        }

        
        if(clientEntity.getAccountNumber() <= 0){
            result.append(
                "--- Cliente não cadastrado no banco. --- \n"
                + "------------------------- \n");
        } else {
            clientEntity.setBalance(newBalance);
            String clientBalance = NumberFormat.getCurrencyInstance()
                                                .format(clientEntity.getBalance()
                                                                .getBalance()
                                                                .doubleValue());
            result.append(clientEntity.getFirstName().toString() + ", seja bem-vindo ao Banco X. \n"
                    + "Seu saldo atual é de R$ " + clientBalance + "\n"
                    + "------------------------- \n\n");            
        }

        result.append("Investimentos: \n"
        + "------------------------- \n");

        if(invst.isEmpty()){
            result.append(
                "--- Cliente não possui investimentos --- \n"
                + "------------------------- \n");
        } else {
            invst.forEach(in -> {
                tesouroDireto.add(clientService.getTesouroByCode(in.getCodeTesouroDireto()));
            });

            tesouroDireto.forEach(t -> {
                result.append(
                "Nome: " + t.getNm() + "\n"
                + "Data de encerramento: " + sdf.format(t.getMtrtyDt()) + "\n"
                + "Valor mínimo de investimento: " + NumberFormat.getCurrencyInstance().format(t.getMinInvstmtAmt()) + "\n"
                + "Valor investido: " + 
                NumberFormat.getCurrencyInstance().format(invst.parallelStream()
                    .filter(i -> i.getCodeTesouroDireto() == t.getCd())
                    .findFirst()
                    .get()
                    .getInvestmentValue()) + "\n"
                + "------------------------- \n");
                
            });

        } 

        result.append("Cartão de Crédito: \n"
        + "------------------------- \n");

        if(creditCards.isEmpty()){
        result.append(
            "--- Cliente não possui cartão de crédito --- \n"
            + "------------------------- \n");
        } else {
            creditCards.forEach(c -> {
                result.append(
                "Nome do cartão: " + c.getCardName() + "\n"
                + "Número do cartão de crédito: " + c.getCardNumber() + "\n"
                + "Valor de limite do cartão: " + NumberFormat.getCurrencyInstance().format(c.getSpendingLimit()) + "\n"
                + "Valor atual do cartão de crédito: " +NumberFormat.getCurrencyInstance().format( c.getCardBalance()) + "\n"
                + "Nome do cliente: " + c.getClientName() + "\n"
                + "------------------------- \n");
                
            });
        }
        
        return Response.ok(result.toString()).build();

    }


    private Response fallbackGetCLientInvestments(@PathParam("cpf") int cpf){
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
   
}