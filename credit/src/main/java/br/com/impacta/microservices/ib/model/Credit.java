package br.com.impacta.microservices.ib.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name = "credits")
public class Credit extends PanacheEntity {
    
    private BigDecimal credit;

    public Credit() {
        super();
    }

    public Credit(BigDecimal credit){
        this.credit = credit;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }
}

