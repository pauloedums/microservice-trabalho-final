package br.com.impacta.microservices.ib.interfaces;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import br.com.impacta.microservices.ib.model.Investment;
import br.com.impacta.microservices.ib.model.TesouroDireto;

@Path("/investments")
@RegisterRestClient
public interface InvestmentsRestClient {
    
    @GET
    @Path("/tesouro-direto")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TesouroDireto> getAllTesouroDireto();

    @GET
    @Path("{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public void getTesouroDireto(@PathParam("code") int code);

    @GET
    @Path("/list")
    public List<Investment> getInvestments();

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void createTesouroDireto(@RequestBody List<TesouroDireto> tesouroDiretos);


    @PUT
    @Path("/add")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void addInvestment(@RequestBody Investment investment);
}
