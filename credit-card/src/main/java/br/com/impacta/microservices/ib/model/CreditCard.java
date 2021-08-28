package br.com.impacta.microservices.ib.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class CreditCard extends PanacheEntity {
   
    public String cardName;
	public String clientName;
    public BigDecimal cardBalance;
	public int cardNumber;
	public BigDecimal spendingLimit;
    
    @OneToMany(cascade = CascadeType.ALL, targetEntity = Purchase.class,fetch = FetchType.EAGER)
    @JoinColumn(name = "purchases")
    public List<Purchase> purchases;
    
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

}
