package br.com.impacta.microservices.ib.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import br.com.impacta.microservices.ib.interfaces.BalanceRestClient;
import br.com.impacta.microservices.ib.model.Client;
import br.com.impacta.microservices.ib.model.Investment;
import br.com.impacta.microservices.ib.model.TesouroDireto;

@ApplicationScoped
public class InvestmentsService {

    @Inject
    @RestClient
    BalanceRestClient balanceRestClient;

    @Transactional
    public List<TesouroDireto> createListTesouroDireto(List<TesouroDireto> tesouroDiretos){
        for(TesouroDireto td: tesouroDiretos){
            TesouroDireto.persist(td);
        }
        return tesouroDiretos;
    }

    @Transactional
    public List<TesouroDireto> listTesouroDireto() {
        List<TesouroDireto> tesouroDiretoList = TesouroDireto.listAll();
        return tesouroDiretoList;
    }

    @Transactional
    public List<Investment> listInvestments() {
        List<Investment> investmentsList = Investment.listAll();
        return investmentsList;
    }


    @Transactional
    public TesouroDireto findInvestmentsByCode(int cd){
        return TesouroDireto.find("cd", cd).firstResult();
    }

    public BigDecimal lote(TesouroDireto tesouroDireto,BigDecimal investmentValue, BigDecimal qty) {
        if(tesouroDireto.getMinInvstmtAmt().intValue() == 0){
            tesouroDireto.setMinInvstmtAmt(new BigDecimal(1));
        } 
        BigDecimal reservedValue = new BigDecimal(investmentValue.intValue()).multiply(qty);
        return tesouroDireto.getMinInvstmtAmt().multiply(reservedValue);
    }


    public BigDecimal diff(BigDecimal lote, BigDecimal investmentValue) {
        if(investmentValue.subtract(lote).intValue() > 0) {
            return investmentValue;
        } else if(lote.subtract(investmentValue).intValue() > 0 ){
            return lote;
        }else {
            return new BigDecimal(0);
        }
    }

    @Transactional
    public TesouroDireto addInvestmentToClient(
        Investment investment) 
    {
        TesouroDireto tesouroDireto = findInvestmentsByCode(investment.getCodeTesouroDireto());

        BigDecimal spendingLimit = balanceRestClient.get().getBalance();

        // Valor do lote
        BigDecimal investmentValue = investment.getInvestmentValue();

        if (spendingLimit.subtract(investmentValue).intValue() > 0) {         
            
            // TODO -> pegar novo balanço após mais um débito.
            List<Client> addClients = new ArrayList<Client>();
            investment.getClient().setBalance(spendingLimit);
            addClients.add(investment.getClient());
            tesouroDireto.setClients(addClients);
            
            Client.persist(investment.getClient());        

            TesouroDireto.persist(tesouroDireto);

            Investment.persist(investment);

            return tesouroDireto;
            
        } else {
            return new TesouroDireto();
        }
	}
}
