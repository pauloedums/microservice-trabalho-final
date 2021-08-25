package br.com.impacta.microservices.ib;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.resource.spi.SecurityPermission;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlow;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlows;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;

import io.quarkus.security.identity.SecurityIdentity;

@Path("/api/clients")
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
    SecurityIdentity securityIdentity;

    @GET
    @Path("/me")
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed("user")
    public String me() {
        return "You are authenticated!";
    }
}