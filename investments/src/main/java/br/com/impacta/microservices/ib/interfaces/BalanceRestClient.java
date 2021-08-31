package br.com.impacta.microservices.ib.interfaces;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import br.com.impacta.microservices.ib.model.Balance;

@Path("/balance")
@Produces(MediaType.APPLICATION_JSON)
@RegisterRestClient
public interface BalanceRestClient {
    
    @GET
    public Balance get();
}