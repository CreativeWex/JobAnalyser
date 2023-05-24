package com.bereznev.clients.service;
/*
    =====================================
    @project ClientsMicroservice
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.clients.exception.AlreadyExistsException;
import com.bereznev.clients.exception.ResourceNotFoundException;
import com.bereznev.clients.entity.Client;
import com.bereznev.clients.repository.ClientRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j

@Service
public class ClientServiceImpl implements ClientService {

    private static final String RESOURCE_NAME = "Client";
    private final ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client save(Client client) {
        String email = client.getEmail();
        if (clientRepository.findClient(email).isPresent()) {
            throw new AlreadyExistsException(RESOURCE_NAME, "email", email);
        }
        log.debug("saved: " + client);
        return clientRepository.save(client);
    }

    @Override
    public List<Client> getAll() {
        log.debug("getAll invoked");
        return clientRepository.findAll();
    }

    @Override
    public Client findById(Long id) {
        log.debug("findById invoked: " + id);
        return clientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "Id", id));
    }

    @Override
    public Client update(Long id, Client updatedClient) {
        Client existedClient = clientRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RESOURCE_NAME, "Id", id));
        String email = updatedClient.getEmail();
        if (clientRepository.findClient(email).isPresent()) {
            throw new AlreadyExistsException(RESOURCE_NAME, "email", email);
        }

        existedClient.setFirstName(updatedClient.getFirstName());
        existedClient.setLastName(updatedClient.getLastName());
        existedClient.setEmail(updatedClient.getEmail());

        clientRepository.save(existedClient);
        log.debug("updated client: " + updatedClient);
        return existedClient;
    }

    @Override
    public void delete(Long id) {
        clientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "Id", id));
        clientRepository.deleteById(id);
        log.debug("deleted, id: " + id);
    }
}
