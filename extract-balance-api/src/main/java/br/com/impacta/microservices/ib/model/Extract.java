package br.com.impacta.microservices.ib.model;

import java.math.BigDecimal;
import java.util.List;

public class Extract{
    
    private List<Credit> creditList;
    private List<Debit> debitList;
    private BigDecimal balance;

    public List<Debit> getDebitList() {
        return debitList;
    }

    public void setDebitList(List<Debit> debitList) {
        this.debitList = debitList;
    }

    public List<Credit> getCreditList() {
        return creditList;
    }

    public void setCreditList(List<Credit> creditoList) {
        this.creditList = creditoList;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
