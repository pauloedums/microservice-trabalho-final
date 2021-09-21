package br.com.impacta.microservices.ib.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

public class CreditCard extends PanacheEntity{
       
    public String cardName;
	public int cardNumber;
    
    @JsonIgnore
	public String clientName;
       
    @JsonIgnore
    public BigDecimal cardBalance;

    @JsonIgnore
	public BigDecimal spendingLimit;
    
	@OneToMany(cascade = CascadeType.ALL,targetEntity = Purchase.class,fetch = FetchType.EAGER)
    @JsonIgnore
    public List<Purchase> purchases;

    @ManyToOne(cascade = CascadeType.ALL, targetEntity = CreditCardClient.class, fetch = FetchType.EAGER)
    @JsonIgnore
    public CreditCardClient client;

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public BigDecimal getCardBalance() {
        return cardBalance;
    }

    public void setCardBalance(BigDecimal cardBalance) {
        this.cardBalance = cardBalance;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public BigDecimal getSpendingLimit() {
        return spendingLimit;
    }

    public void setSpendingLimit(BigDecimal spendingLimit) {
        this.spendingLimit = spendingLimit;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    public CreditCardClient getClient() {
        return client;
    }

    public void setClient(CreditCardClient client) {
        this.client = client;
    }

    
}
