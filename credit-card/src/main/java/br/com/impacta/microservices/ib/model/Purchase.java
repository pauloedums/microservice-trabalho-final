package br.com.impacta.microservices.ib.model;

import java.math.BigDecimal;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Purchase extends PanacheEntity {
    
    public String purchaseName;
    public BigDecimal value;
        
    public String getPurchaseName() {
        return purchaseName;
    }
    public void setPurchaseName(String purchaseName) {
        this.purchaseName = purchaseName;
    }
    
    public BigDecimal getValue() {
        return value;
    }
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    
}
