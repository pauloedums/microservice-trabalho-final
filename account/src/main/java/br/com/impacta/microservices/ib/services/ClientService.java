package br.com.impacta.microservices.ib.services;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import br.com.impacta.microservices.ib.interfaces.InvestmentsRestClient;
import br.com.impacta.microservices.ib.model.Client;
import br.com.impacta.microservices.ib.model.TesouroDireto;

@ApplicationScoped
public class ClientService {

    @Inject
    @RestClient
    InvestmentsRestClient investmentsRestClient;
    
    @Transactional
    public Client addClient(Client client){
        Client.persist(client);
        return client;
    }

    @Transactional
    public Client getClientById(Client client){
        return Client.findById(client.id);
    }

    @Transactional
    public List<Client> listClient(){
        return Client.listAll();
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
        return Client.find("accountNumber", client.getAccountNumber()).firstResult();
    }

    @Transactional
    public Client getClientByCpf(int cpf){
        return Client.find("cpf", cpf).firstResult();
    }

    @Transactional
    public TesouroDireto getTesouroByCode(int cd){
        return investmentsRestClient.getTesouroDireto(cd);
    }

}