package br.com.impacta.microservices.ib;

import java.net.URI;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.Retry;
import java.util.EmptyStackException;

import br.com.impacta.microservices.ib.model.Credit;
import br.com.impacta.microservices.ib.services.CreditService;

@Path("/credit")
public class CreditResource {
    
    @Inject
    CreditService creditService;

    
    @GET
    @Fallback(fallbackMethod = "fallbackGetAllCredits")
    @Timeout(5000)
    @CircuitBreaker(
            requestVolumeThreshold = 4,
            failureRatio = 0.5,
            delay = 200,
            successThreshold =2)
    @Retry(maxRetries = 5)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCredits(){
        List<Credit> credits = creditService.listCredit();
        if(credits.isEmpty()){
            throw new EmptyStackException();
        }
        return Response.ok(credits).build();
    }

    @POST
    @Transactional
    @Timeout(5000)
    @Fallback(fallbackMethod = "fallbackAddCredit")
    @CircuitBreaker(
        requestVolumeThreshold = 4,
        failureRatio = 0.5,
        delay = 200,
        successThreshold = 2)
    @Retry(maxRetries = 5)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCredit(Credit credit){
        if(credit.credit.intValue() != 0) {
            creditService.addCredit(credit);
            return Response.created(URI.create("/credit" + credit.id)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    private Response fallbackAddCredit(Credit credit){
        return Response.ok("Não foi possível adicionar o crédito.").build();
    }

    private Response fallbackGetAllCredits(){
        return Response.ok("Não foi possível trazer a lista de créditos.").build();
    }
}
