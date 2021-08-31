package br.com.impacta.microservices.ib;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import br.com.impacta.microservices.ib.interfaces.DebitRestClient;
import br.com.impacta.microservices.ib.model.Debit;
import br.com.impacta.microservices.ib.model.Investment;
import br.com.impacta.microservices.ib.model.TesouroDireto;
import br.com.impacta.microservices.ib.services.InvestmentsService;

@Path("/investments")
public class InvestmentsResource {

    @Inject
    InvestmentsService investmentsService;

    @Inject
    @RestClient
    DebitRestClient debitRestClient;
    
    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTesouroDireto(List<TesouroDireto> tesouroDiretos){
        investmentsService.createListTesouroDireto(tesouroDiretos);
        return Response.created(URI.create("tesouroDiretos")).build();
    }

    @GET
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAllTesouroDireto(){
        List<TesouroDireto> tesouroDiretos = investmentsService.listTesouroDireto();
        return Response.ok(tesouroDiretos).build();
    }


    @GET
    @Transactional
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getInvestments(){
        List<Investment> investments = investmentsService.listInvestments();
        return Response.ok(investments).build();
    }

    @GET
    @Transactional
    @Path("{code}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getTesouroDireto(@PathParam("code") int code){
        TesouroDireto tesouroDireto = investmentsService.findInvestmentsByCode(code);
        return Response.ok(tesouroDireto).build();
    }

    @PUT
    @Transactional
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addInvestment(Investment investment){
        investmentsService.addInvestmentToClient(investment);

        TesouroDireto tesouroDireto = investmentsService.findInvestmentsByCode(
            investment.getCodeTesouroDireto());

        BigDecimal qty = new BigDecimal(investment.getQuantidade());

        BigDecimal investmentValue = investment.getInvestmentValue();

        Debit debit = new Debit();

        debit.setDebit(
            investmentsService.diff(
                investmentsService.lote(
                    tesouroDireto, qty),
                    investmentValue).negate());
                    
        debitRestClient.addDebit(debit);

        Investment.persist(investment);
        if(investment.isPersistent()) {
            return Response.ok(investment).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}