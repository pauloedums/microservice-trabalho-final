package br.com.impacta.microservices.ib.model;

import java.math.BigDecimal;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Debit extends PanacheEntity {
    
    public BigDecimal debit;
    public int clientCpf;
}