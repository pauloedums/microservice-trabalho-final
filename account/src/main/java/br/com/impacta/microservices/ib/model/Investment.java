package br.com.impacta.microservices.ib.model;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.quarkus.hibernate.orm.panache.PanacheEntity;


@Entity
public class Investment extends PanacheEntity {

    public int codeTesouroDireto;
    public BigDecimal investmentValue;
    public int quantidade;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, targetEntity = InvestimentClient.class, fetch = FetchType.EAGER)
    public InvestimentClient client;
    
    public Investment() {

    }
    
    public Investment(int codeTesouroDireto, BigDecimal investmentValue, int quantidade, InvestimentClient client) {
        this.codeTesouroDireto = codeTesouroDireto;
        this.investmentValue = investmentValue;
        this.quantidade = quantidade;
        this.client = client;
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
