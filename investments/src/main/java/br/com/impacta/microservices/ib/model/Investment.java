package br.com.impacta.microservices.ib.model;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name = "investments")
public class Investment extends PanacheEntity {
    public int codeTesouroDireto;
    public BigDecimal investmentValue;
    public int quantidade;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="client_id")    
    public Client client;
    
    public int getCodeTesouroDireto() {
        return codeTesouroDireto;
    }
    public void setCodeTesouroDireto(int codeTesouroDireto) {
        this.codeTesouroDireto = codeTesouroDireto;
    }
    public BigDecimal getInvestmentValue() {
        return investmentValue;
    }
    public void setInvestmentValue(BigDecimal investmentValue) {
        this.investmentValue = investmentValue;
    }
    public int getQuantidade() {
        return quantidade;
    }
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }

    
}
