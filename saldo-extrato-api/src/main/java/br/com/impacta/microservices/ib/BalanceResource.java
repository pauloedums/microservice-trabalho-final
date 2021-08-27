package br.com.impacta.microservices.ib;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Random;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.impacta.microservices.ib.model.Balance;

@Path("/balance")
public class BalanceResource {

    private static final BigDecimal minValue = new BigDecimal(BigInteger.ONE);
    private static final BigDecimal maxValue = new BigDecimal(BigInteger.TEN);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getBalance(){
        Balance balance = new Balance();
        BigDecimal randomValue = minValue.add(new BigDecimal(Math.random()).multiply(maxValue.subtract(minValue))).setScale(1, RoundingMode.HALF_UP);
        balance.setBalance(randomValue);
        return Response.ok(balance).build();
    }
}