package br.com.impacta.microservices.ib.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name = "client")
public class Client extends PanacheEntity{

    public String firstName;
    public String lastName;
    public int accountNumber;
    @JoinColumn(name="client_cpf")  
    public int cpf;
    private BigDecimal investimentValue;
    
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public Integer getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }
    public Integer getCpf() {
        return cpf;
    }
    public void setCpf(Integer cpf) {
        this.cpf = cpf;
    }
    public BigDecimal getInvestimentValue() {
        return investimentValue;
    }
    public void setInvestimentValue(BigDecimal investimentValue) {
        this.investimentValue = investimentValue;
    }
    
    
    
}
