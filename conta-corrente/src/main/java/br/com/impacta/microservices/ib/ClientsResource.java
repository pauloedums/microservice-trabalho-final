package br.com.impacta.microservices.ib;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlow;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlows;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.com.impacta.microservices.ib.model.Client;
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
    
    @POST
    @Path("/{cpf}")
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed("user")
    public String me(@PathParam("cpf") Integer cpf) {
        Client clientEntity = new Client();
        clientEntity.setCpf(cpf);
        clientEntity = clientService.getClientByCpf(clientEntity);
        return clientEntity.getFirstName().toString() + ", seja bem-vindo ao Banco X.";
    }
}