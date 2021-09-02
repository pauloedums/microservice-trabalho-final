package br.com.impacta.microservices.ib;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;

import br.com.impacta.microservices.ib.model.Balance;
import br.com.impacta.microservices.ib.services.ExtractBalanceService;

@Path("/balance")
public class BalanceResource {
    
    @Inject
    ExtractBalanceService extractService;

    @GET

    @Fallback(fallbackMethod = "fallbackGetBalance")
    @Timeout(2000)
    @CircuitBreaker(
        requestVolumeThreshold=4,
        failureRatio=0.5,
        successThreshold=2
    )
    @Retry(maxRetries = 5)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBalance(){
        Balance balance = extractService.getBalance();
        return Response.ok(balance).build();
    }


    private Response fallbackGetBalance(){
        return Response.ok("Não foi possível encontrar dados para criar o saldo.").build();
    }
}