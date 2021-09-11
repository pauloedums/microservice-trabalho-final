package br.com.impacta.microservices.ib.model;

import java.math.BigDecimal;

public class Debit{
    
    public BigDecimal debit;

    public int clientCpf;
    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public int getClientCpf() {
        return clientCpf;
    }

    public void setClientCpf(int clientCpf) {
        this.clientCpf = clientCpf;
    }

    
}