package br.com.impacta.microservices.ib;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import org.eclipse.microprofile.jwt.JsonWebToken;

import br.com.impacta.microservices.ib.model.Client;
import br.com.impacta.microservices.ib.services.ClientService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/api/admin")
public class BankResource {
    @Inject
    JsonWebToken jwt;

    @Inject
    ClientService clientService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed("admin")
    public String admin() {
        return "Access for subject " + jwt.getSubject() + " is granted";
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
    @Path("/client/{account}")
    @RolesAllowed("admin")
    public Client getClient(@PathParam("account") Integer account){
        Client clientEntity = new Client();
        clientEntity.setAccountNumber(account);
        clientEntity = clientService.getClientByAccount(clientEntity);
        return clientEntity;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/add/client")
    @RolesAllowed("admin")
    public Client addClient(Client client){
        Client clientEntity = clientService.addClient(client);
        return clientEntity;
    }

}