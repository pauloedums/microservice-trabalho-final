package br.com.impacta.microservices.ib.services;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import br.com.impacta.microservices.ib.model.Debit;

@ApplicationScoped
public class DebitServices {

    @Transactional
    public Debit addDebit(Debit debit){
        Debit.persist(debit);
        return debit;
    }

    @Transactional
    public List<Debit> listDebit(){
        List<Debit> debitList = Debit.listAll();
        return debitList;
    }

}
