package br.com.impacta.microservices.ib;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import javax.ws.rs.PathParam;

import br.com.impacta.microservices.ib.model.Debit;
import br.com.impacta.microservices.ib.services.DebitServices;

@Path("/debit")
public class DebitResource {
    
    @Inject
    DebitServices debitServices;

    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Debit> getAllDebits(){
        return debitServices.listDebit();
    }

    @POST
    @Path("/{value}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Debit addDebit(@PathParam("value") BigDecimal debit){
        Debit debitEntity = new Debit();
        debitEntity.setDebit(debit.negate());
        debitServices.addDebit(debitEntity);
        return debitEntity;
    }

}
