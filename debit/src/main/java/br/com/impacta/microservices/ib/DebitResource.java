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

import java.util.EmptyStackException;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;

import br.com.impacta.microservices.ib.model.Debit;
import br.com.impacta.microservices.ib.services.DebitServices;

@Path("/debit")
public class DebitResource {
    
    @Inject
    DebitServices debitServices;

    @GET
    @Fallback(fallbackMethod = "fallbackGetAll")
    @Timeout(5000)
    @CircuitBreaker(
        requestVolumeThreshold = 8,
        delay = 5000,
        successThreshold = 4
    )
    @Retry(maxRetries = 5)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(){
        List<Debit> debits = debitServices.listDebit();
        if(debits.isEmpty()){
            throw new EmptyStackException();

        }
        return Response.ok(debits).build();
    }

    @POST
    @Transactional
    @Timeout(5000)
    @Fallback(fallbackMethod = "fallbackAddDebit")
    @Retry(maxRetries = 5)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDebit(Debit debit){
        if(debit.debit.intValue() != 0) {
            debitServices.addDebit(debit);
            return Response.created(URI.create("/debit" + debit.id)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    private Response fallbackAddDebit(Debit debit){
        return Response.ok("Não foi possível adicionar o débito.").build();
    }

    private Response fallbackGetAll(){
        return Response.ok("Não foi possível trazer a lista de débitos.").build();
    }

}
