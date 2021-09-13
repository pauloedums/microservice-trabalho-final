package br.com.impacta.microservices.ib.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

public class CreditCardClient extends PanacheEntity{

    public String firstName;
    public String lastName;
    private int cpf;
        
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
    public int getCpf() {
        return cpf;
    }
    public void setCpf(int cpf) {
        this.cpf = cpf;
    }   
    
}
