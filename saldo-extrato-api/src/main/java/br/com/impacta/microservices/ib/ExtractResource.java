package br.com.impacta.microservices.ib;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import br.com.impacta.microservices.ib.interfaces.CreditRestClient;
import br.com.impacta.microservices.ib.interfaces.DebtRestClient;
import br.com.impacta.microservices.ib.model.Debit;
import br.com.impacta.microservices.ib.model.Extract;
import br.com.impacta.microservices.ib.services.ExtractService;

@Path("/extract")
public class ExtractResource {
    
    @Inject
    ExtractService extractService;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExtract(){
        Extract extract = extractService.getExtract();
        return Response.ok(extract).build();
    }
}
