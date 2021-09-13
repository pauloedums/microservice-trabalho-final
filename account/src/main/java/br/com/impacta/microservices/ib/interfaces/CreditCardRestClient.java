package br.com.impacta.microservices.ib.interfaces;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import br.com.impacta.microservices.ib.model.CreditCard;
import br.com.impacta.microservices.ib.model.CreditCardClient;
import br.com.impacta.microservices.ib.model.Purchase;

@Path("/card")
@RegisterRestClient
public interface CreditCardRestClient {
        
    @GET
    public List<CreditCard> getAll();

    @GET
    @Path("/{cardNumber}")
    public CreditCard getCreditCardByNumber(@PathParam("cardNumber") int cardNumber);

    @GET
    @Path("/{cardNumber}/purchases")
    public List<Purchase> getAllPurchases(@PathParam("cardNumber") int cardNumber);

    @GET
    @Path("/client/{cpf}")
    public CreditCardClient getClient(@PathParam("cpf") int cpf);

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void addCreditCard(CreditCard creditCard);

    @PUT
    @Path("/{cardNumber}/purchases")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void addPurchase(@PathParam("cardNumber") int cardNumber, Purchase purchase);
}