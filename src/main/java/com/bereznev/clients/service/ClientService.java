package com.bereznev.clients.service;
/*
    =====================================
    @project ClientsMicroservice
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.clients.entity.Client;

import java.util.List;

public interface ClientService {
    Client save(Client client);

    List<Client> getAll();

    Client findById(Long id);

    Client update(Long id, Client updatedClient);

    void delete(Long id);
}
