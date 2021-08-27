package br.com.impacta.microservices.ib.services;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import br.com.impacta.microservices.ib.model.Client;

@ApplicationScoped
public class ClientService {
    
    @Transactional
    public Client addClient(Client client){
        Client.persist(client);
        return client;
    }

    @Transactional
    public Client getClientById(Client client){
        client = Client.findById(client.id);
        return client;
    }

    @Transactional
    public List<Client> listClient(){
        List<Client> clientList = Client.listAll();
        return clientList;
    }
    

    @Transactional
    public Client deleteClient(Client client){
        client = Client.findById(client.id);
        Client.deleteById(client.id);
        return client;
    }

    @Transactional
    public Client updateClient(Client client){
        Client clientEntity = Client.findById(client.id);
        if (clientEntity != null){
            clientEntity.setFirstName(client.getFirstName());
            clientEntity.setLastName(client.getLastName());
            clientEntity.setAccountNumber(client.getAccountNumber());
        }
        return clientEntity;
    }

    @Transactional
    public Client getClientByAccount(Client client){
        client = Client.findByAccount(client);
        return client;
    }

    @Transactional
    public Client getClientByCpf(Client client){
        client = Client.findByCpf(client);
        return client;
    }
}