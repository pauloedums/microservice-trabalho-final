package br.com.impacta.microservices.ib;

import java.util.Random;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.impacta.microservices.ib.model.Balance;

@Path("/balance")
public class BalanceResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getBalance(){
        Balance balance = new Balance();
        balance.setBalance(new Random().ints(1, 99999).findFirst().getAsInt());
        return Response.ok(balance).build();
    }
}