package br.com.impacta.microservices.ib.services;

import java.math.BigDecimal;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import br.com.impacta.microservices.ib.interfaces.CreditRestClient;
import br.com.impacta.microservices.ib.interfaces.DebtRestClient;
import br.com.impacta.microservices.ib.model.Balance;
import br.com.impacta.microservices.ib.model.Credit;
import br.com.impacta.microservices.ib.model.Debit;
import br.com.impacta.microservices.ib.model.Extract;

@ApplicationScoped
public class ExtractBalanceService {
    
    @Inject
    @RestClient
    DebtRestClient debtRestClient;

    @Inject
    @RestClient
    CreditRestClient creditRestClient;

    public BigDecimal balance;

    public Extract getExtract(){
        //Get Credito
        List<Credit> creditList = creditRestClient.getAll();
        Extract extrato = new Extract();
        extrato.setCreditList(creditList);
        BigDecimal creditSum = creditList.stream().map(Credit::getCredit).reduce(BigDecimal.ZERO, BigDecimal::add);
        //Get Debito
        List<Debit> debitList = debtRestClient.getAll();
        extrato.setDebitList(debitList);
        BigDecimal debitSum = debitList.stream().map(Debit::getDebit).reduce(BigDecimal.ZERO, BigDecimal::add);
        //Calcular saldo
        BigDecimal balance = creditSum.add(debitSum);
        extrato.setBalance(balance);
        return extrato;
    }

    public Balance getBalance(){
        Balance balance = new Balance();
        balance.setBalance(getExtract().getBalance());
        return balance;
    }
}
