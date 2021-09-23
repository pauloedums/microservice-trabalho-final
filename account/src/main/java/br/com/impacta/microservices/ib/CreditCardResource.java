package br.com.impacta.microservices.ib;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
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

import br.com.impacta.microservices.ib.enums.FallbackCreditCard;
import br.com.impacta.microservices.ib.interfaces.CreditCardRestClient;
import br.com.impacta.microservices.ib.model.CreditCard;
import br.com.impacta.microservices.ib.model.CreditCardClient;
import br.com.impacta.microservices.ib.model.Purchase;

@Path("/credit-card")
@Tag(name="Cartão de Crédito", description="Consultar cartão de crédito e realizar compras.")
@SecurityScheme(
    securitySchemeName = "quarkus",
    type = SecuritySchemeType.OAUTH2,
    flows = @OAuthFlows(
        password = @OAuthFlow(
            tokenUrl = "http://microservices-impacta-keycloak.com/auth/realms/Quarkus/protocol/openid-connect/token"
        )
    )
)
public class CreditCardResource {
    
    @Inject
    @RestClient
    CreditCardRestClient creditCardRestClient;

    @GET
    @Operation(
        summary = "Retorna a lista de cartões de crédito"
    )
    @Path("/list")
    @Timeout(5000)
    @Retry(maxRetries = 5)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Fallback(fallbackMethod = "fallbackGetAll")
    @RolesAllowed("admin")
    @Counted(name = "countGetInvestments", description = "Count how many times the GetInvestments has been invoked")
    @Timed(
        name = "timeGetInvestments",
        description = "How long it takes to invoke the GetInvestments",
        unit = MetricUnits.MILLISECONDS)
    @Metered(name = "meteredGetInvestments", description = "Measures throughput of GetInvestments method")
    public Response getInvestments() {
        List<CreditCard> creditCards = creditCardRestClient.getAll();
        return Response.ok(creditCards).build();
    }    

    @GET
    @Path("/{cardNumber}")
    @Timeout(5000)
    @Retry(maxRetries = 5)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin","user"})
    @Fallback(fallbackMethod = "fallbackGetCreditCardByNumber")
    @Counted(name = "countGetCreditCardByNumber", description = "Count how many times the GetCreditCardByNumber has been invoked")
    @Timed(
        name = "timeGetCreditCardByNumber",
        description = "How long it takes to invoke the GetCreditCardByNumber",
        unit = MetricUnits.MILLISECONDS)
    @Metered(name = "meteredGetCreditCardByNumber", description = "Measures throughput of GetCreditCardByNumber method")
    public Response getCreditCardByNumber(@PathParam("cardNumber") int cardNumber){
        CreditCard creditCard = creditCardRestClient.getCreditCardByNumber(cardNumber);
        return Response.ok(creditCard).build();
    }

    @GET
    @Path("/{cardNumber}/purchases")
    @Timeout(5000)
    @Retry(maxRetries = 5)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin", "user"})
    @Fallback(fallbackMethod = "fallbackGetAllPurchases")
    @Counted(name = "countGetAllPurchases", description = "Count how many times the GetAllPurchases has been invoked")
    @Timed(
        name = "timeGetAllPurchases",
        description = "How long it takes to invoke the GetAllPurchases",
        unit = MetricUnits.MILLISECONDS)
    @Metered(name = "meteredGetAllPurchases", description = "Measures throughput of GetAllPurchases method")
    public Response getAllPurchases(@PathParam("cardNumber") int cardNumber){
        List<Purchase> purchases = creditCardRestClient.getAllPurchases(cardNumber);
        return Response.ok(purchases).build();
    }

    @GET
    @Path("/client/{cpf}")
    @Timeout(5000)
    @Retry(maxRetries = 5)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin", "user"})
    @Fallback(fallbackMethod = "fallbackGetAllPurchases")
    @Counted(name = "countGetClient", description = "Count how many times the GetClient has been invoked")
    @Timed(
        name = "timeGetClient",
        description = "How long it takes to invoke the GetClient",
        unit = MetricUnits.MILLISECONDS)
    @Metered(name = "meteredGetClient", description = "Measures throughput of GetClient method")
    public Response getClient(@PathParam("cpf") int cpf){
        CreditCardClient client = creditCardRestClient.getClient(cpf);
        return Response.ok(client).build();
    }


    @PUT
    @Path("/{cardNumber}/purchase")
    @Timeout(5000)
    @Retry(maxRetries = 5)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin", "user"})
    @Fallback(fallbackMethod = "fallbackAddPurchase")
    @Counted(name = "countGetClientByCardNumber", description = "Count how many times the GetClientByCardNumber has been invoked")
    @Timed(
        name = "timeGetClientByCardNumber",
        description = "How long it takes to invoke the GetClientByCardNumber",
        unit = MetricUnits.MILLISECONDS)
    @Metered(name = "meteredGetClientByCardNumber", description = "Measures throughput of GetClientByCardNumber method")
    public Response getClientByCardNumber(@PathParam("cardNumber") int cardNumber, Purchase purchase){
        creditCardRestClient.addPurchase(cardNumber, purchase);
        CreditCard creditCard = creditCardRestClient.getCreditCardByNumber(cardNumber);
        return Response.ok(creditCard).build();
    }


    private Response fallbackAddPurchase(int cardNumber, Purchase purchase){
        return Response.serverError()
                .header("erro", FallbackCreditCard.ADD_PURCHASE.getDescription())
                .build();
    }

    private Response fallbackAddCreditCard(CreditCard creditCard){
        return Response.serverError()
                .header("erro", FallbackCreditCard.ADD_CREDIT_CARD.getDescription())
                .build();
    }
    private Response fallbackGetAllPurchases(int cardNumber){
        return Response.serverError()
                .header("erro", FallbackCreditCard.GET_ALL_PURCHASES.getDescription())
                .build();
    }

    private Response fallbackGetCreditCardByNumber(int cardNumber){
        return Response.serverError()
                .header("erro", FallbackCreditCard.GET_CREDIT_CARD_BY_NUMBER.getDescription())
                .build();
    }
    private Response fallbackGetAll(){
        return Response.serverError()
                .header("erro", FallbackCreditCard.GET_ALL.getDescription())
                .build();
    }
}
