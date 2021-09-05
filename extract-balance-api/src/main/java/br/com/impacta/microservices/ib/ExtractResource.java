package br.com.impacta.microservices.ib;

import java.util.EmptyStackException;

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

import br.com.impacta.microservices.ib.model.Extract;
import br.com.impacta.microservices.ib.services.ExtractBalanceService;

@Path("/extract")
public class ExtractResource {
    
    @Inject
    ExtractBalanceService extractService;

    @GET
    @Fallback(fallbackMethod = "fallbackGetExtract")
    @Timeout(5000)
    @CircuitBreaker(        
        requestVolumeThreshold = 8,
        failureRatio = 0.5,
        delay = 5000,
        successThreshold = 4
    )
    @Retry(maxRetries = 5)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExtract(){
        Extract extract = extractService.getExtract();
        if(extract.equals(new Extract())){
            throw new EmptyStackException();
        }
        return Response.ok(extract).build();
    }


    private Response fallbackGetExtract(){
        return Response.ok("Não foi possível encontrar dados para criar o extrato.").build();
    }
}
