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

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import br.com.impacta.microservices.ib.enums.FallbackMessages;
import br.com.impacta.microservices.ib.interfaces.BalanceRestClient;
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

    @Inject
    @RestClient
    BalanceRestClient balanceRestClient;
    
    @GET
    @Path("/tesouro-direto")
    @Timeout(5000)
    @CircuitBreaker(
        requestVolumeThreshold = 8,
        delay = 5000,
        successThreshold = 4
    )
    @Retry(maxRetries = 5)
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAllTesouroDireto(){
        if(TesouroDireto.findAll().count() <= 0){
            return Response.ok(FallbackMessages.GET_ALL_TESOURO_DIRETO.getDescricao()).build();
        }
        List<TesouroDireto> tesouroDiretos = investmentsService.listTesouroDireto();
        return Response.ok(tesouroDiretos).build();
    }


    @GET
    @Path("/list")
    @Transactional
    @Timeout(5000)
    @CircuitBreaker(
        requestVolumeThreshold = 8,
        delay = 5000,
        successThreshold = 4
    )
    @Retry(maxRetries = 5)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getInvestments(){
        if(Investment.findAll().list().isEmpty()){
           return Response.ok(FallbackMessages.GET_INVESTIMENTS.getDescricao()).build();
        }
        List<Investment> investments = investmentsService.listInvestments();
        return Response.ok(investments).build();
    }

    @GET
    @Path("{code}")
    @Transactional
    @Fallback(fallbackMethod = "fallbackGetTesouroDireto")
    @Timeout(5000)
    @CircuitBreaker(
        requestVolumeThreshold = 8,
        delay = 5000,
        successThreshold = 4
    )
    @Retry(maxRetries = 5)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getTesouroDireto(@PathParam("code") int code){
        TesouroDireto tesouroDireto = investmentsService.findInvestmentsByCode(code);
        if(!tesouroDireto.isPersistent()){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok(tesouroDireto).build();
    }

    @POST
    @Transactional
    @Fallback(fallbackMethod = "fallbackCreateTesouroDireto")
    @Timeout(5000)
    @Retry(maxRetries = 5)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTesouroDireto(List<TesouroDireto> tesouroDiretos){
        if(tesouroDiretos.get(0).cd == 0){
            return Response.ok(FallbackMessages.CREATE_TESOURO_DIRETO.getDescricao()).build();
        }
        investmentsService.createListTesouroDireto(tesouroDiretos);
        return Response.created(URI.create("tesouroDiretos")).build();
    }

    @PUT
    @Transactional
    @Path("/add")
    @Fallback(fallbackMethod = "fallbackAddInvestment")
    @Timeout(5000)
    @Retry(maxRetries = 5)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addInvestment(Investment investment){
        if(balanceRestClient.get().getBalance().intValue() <= 0){
            return Response.ok(FallbackMessages.ADD_INVESTMENT.getDescricao()).build();
        }
        else {
            TesouroDireto tesouroDireto = investmentsService.findInvestmentsByCode(
                investment.getCodeTesouroDireto());
                
            BigDecimal qty = new BigDecimal(investment.getQuantidade());
            
            BigDecimal investmentValue = investment.getInvestmentValue();
            
            Debit debit = new Debit();
            
            debit.setDebit(
                investmentsService.lote(
                    tesouroDireto,investmentValue, qty).negate());
            debitRestClient.addDebit(debit);
            investmentsService.addInvestmentToClient(investment);
            Investment.persist(investment);
            return Response.ok(investment).build();       
        }
    }

    private Response fallbackAddInvestment(Investment investment){
        return Response.ok(FallbackMessages.ADD_INVESTMENT.getDescricao()).build();
    }

    private Response fallbackGetTesouroDireto(int code){
        return Response.ok(FallbackMessages.GET_TESOURO_DIRETO.getDescricao()).build();
    }

    private Response fallbackCreateTesouroDireto(List<TesouroDireto> tesouroDiretos){
        return Response.ok(FallbackMessages.CREATE_TESOURO_DIRETO.getDescricao()).build();
    }
}