package br.com.impacta.microservices.ib;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
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

import br.com.impacta.microservices.ib.enums.FallbackClientMessages;
import br.com.impacta.microservices.ib.interfaces.InvestmentsRestClient;
import br.com.impacta.microservices.ib.model.Client;
import br.com.impacta.microservices.ib.model.InvestimentClient;
import br.com.impacta.microservices.ib.model.Investment;
import br.com.impacta.microservices.ib.model.TesouroDireto;
import br.com.impacta.microservices.ib.services.ClientService;

@Path("/investments")
@Tag(name="Investimentos", description="Aderir a investimentos e verificar mais detalhes.")
@SecurityScheme(
    securitySchemeName = "quarkus",
    type = SecuritySchemeType.OAUTH2,
    flows = @OAuthFlows(
        password = @OAuthFlow(
            tokenUrl = "http://microservices-impacta-keycloak.com/auth/realms/Quarkus/protocol/openid-connect/token"
        )
    )
)
public class InvestmentsResource {
    
    @Inject
    ClientService clientService;
    
    @Inject
    @RestClient
    InvestmentsRestClient investmentsRestClient;


    @GET
    @Operation(
        summary = "Retorna a lista de tesouros diretos dispon??veis para investimento"
    )
    @Path("/tesouro-direto")
    @Timeout(5000)
    @Retry(maxRetries = 5)
    @Fallback(fallbackMethod = "fallbackGetAllTesouroDireto")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin", "user"})
    @Counted(name = "countGetTesouroDiretos", description = "Count how many times the GetTesouroDiretos has been invoked")
    @Timed(
        name = "timeGetTesouroDiretos",
        description = "How long it takes to invoke the GetTesouroDiretos",
        unit = MetricUnits.MILLISECONDS)
    @Metered(name = "meteredGetTesouroDiretos", description = "Measures throughput of GetTesouroDiretos method")
    public Response getTesouroDiretos() {
        List<TesouroDireto> tesouroDiretos = investmentsRestClient.getAllTesouroDireto();
        return Response.ok(tesouroDiretos).build();
    }


    @GET
    @Operation(
        summary = "Retorna o tesouro direto pesquisado pelo c??digo."
    )
    @Path("/tesouro-direto/{code}")
    @Timeout(5000)
    @Retry(maxRetries = 5)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin", "user"})
    @Counted(name = "countGetTesouroDiretosByCode", description = "Count how many times the GetTesouroDiretosByCode has been invoked")
    @Timed(
        name = "timeGetTesouroDiretosByCode",
        description = "How long it takes to invoke the GetTesouroDiretosByCode",
        unit = MetricUnits.MILLISECONDS)
    @Metered(name = "meteredGetTesouroDiretosByCode", description = "Measures throughput of GetTesouroDiretosByCode method")
    public Response getTesouroDiretosByCode(@PathParam("code") int code) {
        TesouroDireto tesouroDireto = investmentsRestClient.getTesouroDireto(code);
        return Response.ok(tesouroDireto).build();
    }

    // TODO - Arrumar adicionar investimentos para enviar um response
    @PUT
    @Operation(
        summary = "Adiciona investimento na conta do cliente"
    )
    @Transactional
    @Path("/{cpf}/add-investment")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin", "user"})
    @Counted(name = "countAddInvestment", description = "Count how many times the AddInvestment has been invoked")
    @Timed(
        name = "timeAddInvestment",
        description = "How long it takes to invoke the AddInvestment",
        unit = MetricUnits.MILLISECONDS)
    @Metered(name = "meteredAddInvestment", description = "Measures throughput of AddInvestment method")
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
