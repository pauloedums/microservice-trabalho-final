package br.com.impacta.microservices.ib.model;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
public class Investment extends PanacheEntityBase {

    @Id
    @GeneratedValue
    public int id;

    public int codeTesouroDireto;
    public BigDecimal investmentValue;
    public int quantidade;
    @ManyToOne(cascade = CascadeType.ALL, targetEntity = InvestimentClient.class, fetch = FetchType.EAGER)
    public InvestimentClient client;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public InvestimentClient getClient() {
        return client;
    }

    public void setClient(InvestimentClient client) {
        this.client = client;
    }
    
   
    
}
