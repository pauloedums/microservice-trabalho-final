package br.com.impacta.microservices.ib.model;

import java.math.BigDecimal;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

public class InvestimentClient extends PanacheEntity{

    public String firstName;
    public String lastName;
    public int accountNumber;
    public int cpf;
    public BigDecimal investimentValue;
    public InvestimentClient() {}
    public InvestimentClient(String firstName, String lastName, int accountNumber, int cpf) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.accountNumber = accountNumber;
        this.cpf = cpf;
    }
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
    public int getCpf() {
        return cpf;
    }
    public void setCpf(int cpf) {
        this.cpf = cpf;
    }
    public BigDecimal getInvestimentValue() {
        return investimentValue;
    }
    public void setInvestimentValue(BigDecimal investimentValue) {
        this.investimentValue = investimentValue;
    }
    
       
    
    
}
