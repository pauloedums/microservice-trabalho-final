package br.com.impacta.microservices.ib.model;

import java.math.BigDecimal;

public class Debit{
    
    public BigDecimal debit;

    public Integer clientCpf;
    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public Integer getClientCpf() {
        return clientCpf;
    }

    public void setClientCpf(Integer clientCpf) {
        this.clientCpf = clientCpf;
    }

    
}