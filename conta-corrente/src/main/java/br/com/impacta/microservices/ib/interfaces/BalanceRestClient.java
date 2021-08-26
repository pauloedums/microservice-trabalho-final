package br.com.impacta.microservices.ib.interfaces;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.impacta.microservices.ib.model.Balance;

@ApplicationScoped
@RegisterRestClient
public interface BalanceRestClient {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Balance getBalance();
}