package br.com.impacta.microservices.ib;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlow;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlows;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import br.com.impacta.microservices.ib.interfaces.BalanceRestClient;
import br.com.impacta.microservices.ib.model.Client;
import br.com.impacta.microservices.ib.model.Balance;
import br.com.impacta.microservices.ib.services.ClientService;


@Path("/admin")
@Tag(name="Banco - Administração", description="API de Gerenciamento Administrativo de clientes")
@SecurityScheme(
    securitySchemeName = "quarkus",
    type = SecuritySchemeType.OAUTH2,
    flows = @OAuthFlows(
        password = @OAuthFlow(
            tokenUrl = "http://localhost:10520/auth/realms/Quarkus/protocol/openid-connect/token"
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

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed("admin")
    public String admin() {
        return "Access for subject " + jwt.getSubject() + " is granted";
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/client")
    @RolesAllowed("admin")
    public Client addClient(Client client){
        Balance newBalance = balanceRestClient.getBalance();
        client.setBalance(newBalance);
        Client clientEntity = clientService.addClient(client);
        return clientEntity;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/clients")
    @RolesAllowed("admin")
    public List<Client> getAllClients(){
        return clientService.listClient();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/clients/{account}")
    @RolesAllowed("admin")
    public Client getClient(@PathParam("account") Integer account){
        Client clientEntity = new Client();
        clientEntity.setAccountNumber(account);
        clientEntity = clientService.getClientByAccount(clientEntity);
        return clientEntity;
    }
}