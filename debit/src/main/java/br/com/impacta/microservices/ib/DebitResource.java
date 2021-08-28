package br.com.impacta.microservices.ib;

import java.net.URI;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.impacta.microservices.ib.model.Debit;
import br.com.impacta.microservices.ib.services.DebitServices;

@Path("/debit")
public class DebitResource {
    
    @Inject
    DebitServices debitServices;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(){
        List<Debit> debits = debitServices.listDebit();
        return Response.ok(debits).build();
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDebit(Debit debit){
        debitServices.addDebit(debit);
        if(debit.isPersistent()) {
            return Response.created(URI.create("/debit" + debit.id)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

}
