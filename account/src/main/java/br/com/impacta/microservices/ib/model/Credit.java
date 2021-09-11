package br.com.impacta.microservices.ib.model;

import java.math.BigDecimal;
public class Credit {
    
    public BigDecimal credit;

    public int clientCpf;
    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public int getClientCpf() {
        return clientCpf;
    }

    public void setClientCpf(int clientCpf) {
        this.clientCpf = clientCpf;
    }
}
