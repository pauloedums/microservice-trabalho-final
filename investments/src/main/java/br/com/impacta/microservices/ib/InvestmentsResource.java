package br.com.impacta.microservices.ib;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.com.impacta.microservices.ib.services.InvestmentsService;

@Path("/investments")
public class InvestmentsResource {

    @Inject
    InvestmentsService investmentsService;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAllTesouroDireto() throws JsonParseException, JsonMappingException, IOException {
        return Response.ok(investmentsService.getAllTesouroDireto()).build();
    }
}