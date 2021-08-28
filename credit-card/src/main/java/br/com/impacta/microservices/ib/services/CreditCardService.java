package br.com.impacta.microservices.ib.services;

import java.math.BigDecimal;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import br.com.impacta.microservices.ib.model.Balance;
import br.com.impacta.microservices.ib.model.CreditCard;
import br.com.impacta.microservices.ib.model.Purchase;

@ApplicationScoped
public class CreditCardService {
    
    @Transactional
    public CreditCard addCreditCard(CreditCard creditCard){
        CreditCard.persist(creditCard);
        return creditCard;
    }

    @Transactional
    public List<CreditCard> listCreditCards() {
        List<CreditCard> creditCardList = CreditCard.listAll();
        return creditCardList;
    }

    @Transactional
    public CreditCard findCreditCardById(int cardNumber){
        return CreditCard.find("cardNumber", cardNumber).firstResult();
    }

    @Transactional
    public CreditCard addPurchaseToCreditCard(CreditCard creditCard, Purchase purchase) {
        Balance newBalance = new Balance();
        BigDecimal spendingLimit = creditCard.getSpendingLimit();
        if (spendingLimit.subtract(purchase.getValue()).intValue() > 0) {            
            newBalance.setBalance(creditCard.getCardBalance().add(purchase.getValue()));
            creditCard.setCardBalance(newBalance.getBalance());
            purchase.setValue(purchase.getValue());
            creditCard.getPurchases().add(purchase);
            return creditCard;
        } else {
           System.out.println("Charge Denied"); 
           return creditCard;
        }
	}

    @Transactional
    public Purchase addPurchase(Purchase purchase){
        System.out.println(purchase);
        Purchase.persist(purchase);
        return purchase;
    }

    @Transactional
    public List<Purchase> listPurchases(){
        List<Purchase> purchaseList = Purchase.listAll();
        return purchaseList;
    }


    @Transactional
    public List<Purchase> listPurchasesByCreditCard(int cardNumber){
        List<Purchase> purchaseList= findCreditCardById(cardNumber).getPurchases();
        return purchaseList;
    }
}
