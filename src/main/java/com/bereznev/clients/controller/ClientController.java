package com.bereznev.clients.controller;
/*
    =====================================
    @project ClientsMicroservice
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.clients.entity.Client;
import com.bereznev.clients.exception.BadArgumentsException;
import com.bereznev.clients.exception.ExceptionConverter;
import com.bereznev.clients.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        super();
        this.clientService = clientService;
    }

    @PostMapping("/add")
    public ResponseEntity<Client> save(@RequestBody @Valid Client client, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadArgumentsException("Client save", ExceptionConverter.convertErrorsToString(bindingResult));
        }
        return new ResponseEntity<Client>(clientService.save(client), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Client> getAll() {
        return clientService.getAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Client> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<Client>(clientService.findById(id), HttpStatus.OK);
    }

    @PutMapping("{id}/update")
    public ResponseEntity<Client> update(@PathVariable("id") Long id, @RequestBody Client client) {
        return new ResponseEntity<Client>(clientService.update(id, client), HttpStatus.OK);
    }

    @DeleteMapping("{id}/delete")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        clientService.delete(id);
        return new ResponseEntity<String>("Client deleted successfully, id: " + id, HttpStatus.OK);
    }
}
