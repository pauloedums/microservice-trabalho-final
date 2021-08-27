package br.com.impacta.microservices.ib.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Table;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name = "debits")
public class Debit extends PanacheEntity {
    
    private BigDecimal debit;

    public Debit() {
        super();
    }

    public Debit(BigDecimal debit){
        this.debit = debit;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }
}