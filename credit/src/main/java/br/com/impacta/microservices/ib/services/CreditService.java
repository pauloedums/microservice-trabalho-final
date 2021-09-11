package br.com.impacta.microservices.ib.services;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import br.com.impacta.microservices.ib.model.Credit;

@ApplicationScoped
public class CreditService {

    @Transactional
    public Credit addCredit(Credit credit){
        Credit.persist(credit);
        return credit;
    }

    @Transactional
    public List<Credit> listCredit(){
        List<Credit> creditList = Credit.listAll();
        return creditList;
    }

}
