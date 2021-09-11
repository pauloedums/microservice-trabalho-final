package br.com.impacta.microservices.ib.services;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import br.com.impacta.microservices.ib.interfaces.BalanceRestClient;
import br.com.impacta.microservices.ib.interfaces.DebitRestClient;
import br.com.impacta.microservices.ib.model.InvestimentClient;
import br.com.impacta.microservices.ib.model.Debit;
import br.com.impacta.microservices.ib.model.Investment;
import br.com.impacta.microservices.ib.model.TesouroDireto;

@ApplicationScoped
public class InvestmentsService {

    @Inject
    @RestClient
    DebitRestClient debitRestClient;
    
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
        BigDecimal reservedValue = new BigDecimal(0).add(investmentValue.multiply(qty));

        BigDecimal invstAmount = new BigDecimal(0).add(tesouroDireto.getMinInvstmtAmt().multiply(reservedValue));

        return invstAmount;
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

        BigDecimal investmentValue = investment.getInvestmentValue();
        
        BigDecimal qty = new BigDecimal(investment.getQuantidade());
        
        Debit debit = new Debit();
        BigDecimal lote = new BigDecimal(0)
            .add(
                lote(tesouroDireto,investmentValue, qty)
            );
        System.out.println("COMEÇOU A ADICIONAR OS VALORES");
        if (spendingLimit.subtract(investmentValue).signum() > 0 && spendingLimit.intValue() > lote.intValue()) {         
            System.out.println("ENTROU NO IF");
            debit.setDebit(lote.negate());
            debit.setClientCpf(investment.getClient().getCpf());
            debitRestClient.addDebit(debit);
            
            BigDecimal storeOldAmountOfInvestment = investment.getClient().getInvestimentValue();
            
            investment.getClient().setInvestimentValue(storeOldAmountOfInvestment.add(lote));

            System.out.println("ENTROU NO IF");

            System.out.println("PEGA O CLIENTE");

            System.out.println("CPF DO CLIENTE: " + investment.getClient().getCpf());
            Set<InvestimentClient> clients = new HashSet<InvestimentClient>();
            
            clients.add(investment.getClient());
            
            tesouroDireto.setClients(clients);

            InvestimentClient.persist(investment.getClient());  
            System.out.println("##############################");
            System.out.println("CPF DO CLIENTE: ");
            System.out.println(investment.getClient().getCpf());
            System.out.println("##############################");
            Investment.persist(investment);
            
            TesouroDireto.persist(tesouroDireto);
            return tesouroDireto;
            
        } else {
            return new TesouroDireto();
        }
	}
}
