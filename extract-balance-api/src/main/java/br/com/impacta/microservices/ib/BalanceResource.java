package br.com.impacta.microservices.ib;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.impacta.microservices.ib.model.Balance;
import br.com.impacta.microservices.ib.services.ExtractBalanceService;

@Path("/balance")
public class BalanceResource {
    
    @Inject
    ExtractBalanceService extractService;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBalance(){
        Balance balance = extractService.getBalance();
        return Response.ok(balance).build();
    }
}