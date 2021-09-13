package br.com.impacta.microservices.ib;

import java.net.URI;
import java.util.EmptyStackException;
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

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import br.com.impacta.microservices.ib.enums.FallbackCreditCard;
import br.com.impacta.microservices.ib.interfaces.DebitRestClient;
import br.com.impacta.microservices.ib.model.CreditCardClient;
import br.com.impacta.microservices.ib.model.CreditCard;
import br.com.impacta.microservices.ib.model.Debit;
import br.com.impacta.microservices.ib.model.Purchase;
import br.com.impacta.microservices.ib.services.CreditCardService;

@Path("/card")
public class CreditCardResource {

    @Inject
    @RestClient
    DebitRestClient debitRestClient;

    @Inject
    CreditCardService creditCardService;
    
    @GET
    @Timeout(5000)
    @Retry(maxRetries = 5)
    @Fallback(fallbackMethod = "fallbackGetAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(){
        List<CreditCard> creditCards = creditCardService.listCreditCards();
        if(creditCards.isEmpty()){
            throw new EmptyStackException();
        }
        return Response.ok(creditCards).build();
    }


    @GET
    @Timeout(5000)
    @Retry(maxRetries = 5)
    @Path("/{cardNumber}")
    @Fallback(fallbackMethod = "fallbackGetCreditCardByNumber")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCreditCardByNumber(@PathParam("cardNumber") int cardNumber){
        CreditCard card = creditCardService.findCreditCardById(cardNumber);
        if(card.equals(new CreditCard())){
            throw new EmptyStackException();
        }
        return Response.ok(card).build();
    }


    @GET
    @Timeout(5000)
    @Retry(maxRetries = 5)
    @Path("/{cardNumber}/purchases")
    @Fallback(fallbackMethod = "fallbackGetAllPurchases")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPurchases(@PathParam("cardNumber") int cardNumber){
        List<Purchase> purchaseList = creditCardService.listPurchasesByCreditCard(cardNumber);
        if(purchaseList.isEmpty()){
            throw new EmptyStackException();
        }
        return Response.ok(purchaseList).build();
    }

    @GET
    @Timeout(5000)
    @Transactional
    @Retry(maxRetries = 5)
    @Path("/client/{cpf}")
    @Fallback(fallbackMethod = "fallbackGetAllPurchases")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClient(@PathParam("cpf") int cpf){
        CreditCardClient client = creditCardService.findClientByCpf(cpf);
        if(client.equals(new CreditCardClient())){
            throw new EmptyStackException();
        }
        return Response.ok(client).build();
    }

    @POST
    @Path("/create")
    @Transactional
    @Timeout(5000)
    @Fallback(fallbackMethod = "fallbackAddCreditCard")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCreditCard(CreditCard creditCard){
        if(CreditCard.find("cardNumber", creditCard.getCardNumber()).firstResult() != null) { 
            Response.serverError().build();
            throw new EmptyStackException();
        }
        creditCardService.addCreditCard(creditCard);
        for(Purchase purchase:creditCard.getPurchases()) {
            Debit debit = new Debit();
            debit.setDebit(purchase.getValue().negate());
            debit.setClientCpf(creditCard.getClient().getCpf());
            debitRestClient.addDebit(debit);
        }
        return Response.created(URI.create("/create" + creditCard.id)).build();
    }

    @PUT
    @Transactional
    @Timeout(5000)
    @Retry(maxRetries = 5)
    @Fallback(fallbackMethod = "fallbackAddPurchase")
    @Path("/{cardNumber}/purchases")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPurchase(@PathParam("cardNumber") int cardNumber, Purchase purchase){
        CreditCard card = creditCardService.findCreditCardById(cardNumber);
        
        // ADD to debit
        Debit debit = new Debit();
        debit.setDebit(purchase.getValue().negate());
        debit.setClientCpf(card.getClient().getCpf());
        debitRestClient.addDebit(debit);

        // ADD new purchase to CreditCard
        creditCardService.addPurchaseToCreditCard(card, purchase);
        CreditCard.persist(card);
        if(card.isPersistent()) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    private Response fallbackAddPurchase(int cardNumber, Purchase purchase){
        return Response.ok(FallbackCreditCard.ADD_PURCHASE.getDescription()).build();
    }

    private Response fallbackAddCreditCard(CreditCard creditCard){
        return Response.ok(FallbackCreditCard.ADD_CREDIT_CARD.getDescription()).build();
    }
    private Response fallbackGetAllPurchases(int cardNumber){
        return Response.ok(FallbackCreditCard.GET_ALL_PURCHASES.getDescription()).build();
    }

    private Response fallbackGetCreditCardByNumber(int cardNumber){
        return Response.ok(FallbackCreditCard.GET_CREDIT_CARD_BY_NUMBER.getDescription()).build();
    }
    private Response fallbackGetAll(){
        return Response.ok(FallbackCreditCard.GET_ALL.getDescription()).build();
    }
}