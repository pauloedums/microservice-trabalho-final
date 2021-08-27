package br.com.impacta.microservices.ib;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.impacta.microservices.ib.model.Credit;
import br.com.impacta.microservices.ib.services.CreditService;

@Path("/credit")
public class CreditResource {
    
    @Inject
    CreditService creditService;

    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Credit> getAllCredits(){
        return creditService.listCredit();
    }

    @POST
    @Path("/{value}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Credit addDebit(@PathParam("value") BigDecimal credit){
        Credit creditEntity = new Credit();
        creditEntity.setCredit(credit);
        creditService.addCredit(creditEntity);
        return creditEntity;
    }

}
