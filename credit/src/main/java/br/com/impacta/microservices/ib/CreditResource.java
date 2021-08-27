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

import br.com.impacta.microservices.ib.model.Credit;
import br.com.impacta.microservices.ib.services.CreditService;

@Path("/credit")
public class CreditResource {
    
    @Inject
    CreditService creditService;

    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCredits(){
        List<Credit> credits = creditService.listCredit();
        return Response.ok(credits).build();
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCredit(Credit credit){
        creditService.addCredit(credit);
        if(credit.isPersistent()){
            return Response.created(URI.create("/credit" + credit.id)).build();
        }
        
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

}
