package br.com.impacta.microservices.ib.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Client extends PanacheEntity{

    public String firstName;
    public String lastName;
    public int accountNumber;
    public Integer cpf;
    public BigDecimal investimentValue;
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
    public int getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(int accountNumber) {
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
