package br.com.impacta.microservices.ib.interfaces;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import br.com.impacta.microservices.ib.model.Investment;

@Path("/investments")
@Produces(MediaType.APPLICATION_JSON)
@RegisterRestClient
public interface InvestmentsRestClient {
    
    @GET
    public List<Investment> getInvestments();
}
