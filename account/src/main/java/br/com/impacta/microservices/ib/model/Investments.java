package br.com.impacta.microservices.ib.model;

import java.math.BigDecimal;
import java.util.Date;

public class Investments {
    
    public String name;

    public String type;

    public BigDecimal value;

    public Date data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    
}
