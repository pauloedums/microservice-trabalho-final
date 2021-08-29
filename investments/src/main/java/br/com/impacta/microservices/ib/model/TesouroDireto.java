package br.com.impacta.microservices.ib.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
public class TesouroDireto extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore(value = true)
    public int id;
    
    public int cd;
    public String nm;
    public String featrs;
    public Date mtrtyDt;
    public BigDecimal minInvstmtAmt;
    public BigDecimal untrInvstmtVal;
    public String invstmtStbl;
    public String rcvgIncm;
    
}
