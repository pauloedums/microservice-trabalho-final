package br.com.impacta.microservices.ib;

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
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(){
        List<CreditCard> creditCards = creditCardService.listCreditCards();
        return Response.ok(creditCards).build();
    }


    @GET
    @Path("/{cardNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCreditCardByNumber(@PathParam("cardNumber") int cardNumber){
        CreditCard card = creditCardService.findCreditCardById(cardNumber);
        return Response.ok(card).build();
    }


    @GET
    @Path("/{cardNumber}/purchases")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPurchases(@PathParam("cardNumber") int cardNumber){
        List<Purchase> purchaseList = creditCardService.listPurchasesByCreditCard(cardNumber);
        return Response.ok(purchaseList).build();
    }

    @POST
    @Path("/create")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCreditCard(CreditCard creditCard){
        creditCardService.addCreditCard(creditCard);
        if(creditCard.isPersistent()) { 
            for(Purchase purchase:creditCard.getPurchases()) {
                Debit debit = new Debit();
                debit.setDebit(purchase.getValue());
                debitRestClient.addDebit(debit);
            }
            return Response.created(URI.create("/create" + creditCard.id)).build();
        }
        System.out.println(Response.Status.values());
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Transactional
    @Path("/{cardNumber}/purchases")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPurchase(@PathParam("cardNumber") int cardNumber, Purchase purchase){
        CreditCard card = creditCardService.findCreditCardById(cardNumber);
        
        // ADD to debit
        Debit debit = new Debit();
        debit.setDebit(purchase.getValue().negate());
        debitRestClient.addDebit(debit);

        // ADD new purchase to CreditCard
        creditCardService.addPurchaseToCreditCard(card, purchase);
        CreditCard.persist(card);
        if(card.isPersistent()) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}