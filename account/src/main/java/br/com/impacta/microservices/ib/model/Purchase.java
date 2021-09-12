package br.com.impacta.microservices.ib.model;

import java.math.BigDecimal;

public class Purchase {
    
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
