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
    public Integer id;

    public Integer codeTesouroDireto;
    public BigDecimal investmentValue;
    public Integer quantidade;

    @ManyToOne(cascade = CascadeType.ALL, targetEntity = Client.class, fetch = FetchType.EAGER)
    public Client client;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCodeTesouroDireto() {
        return codeTesouroDireto;
    }

    public void setCodeTesouroDireto(Integer codeTesouroDireto) {
        this.codeTesouroDireto = codeTesouroDireto;
    }

    public BigDecimal getInvestmentValue() {
        return investmentValue;
    }

    public void setInvestmentValue(BigDecimal investmentValue) {
        this.investmentValue = investmentValue;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
    
   
    
}
