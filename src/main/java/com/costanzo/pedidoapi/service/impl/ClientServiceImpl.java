package com.costanzo.pedidoapi.service.impl;

import com.costanzo.pedidoapi.model.entity.Client;
import com.costanzo.pedidoapi.model.repository.ClientRepository;
import com.costanzo.pedidoapi.service.ClientService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    private ClientRepository repository;

    public ClientServiceImpl(ClientRepository repository) {
        this.repository = repository;
    }

    @Override
    public Client save(Client client) {
        return repository.save(client);
    }

    @Override
    public Optional<Client> getById(Long id) {
        return this.repository.findById(id);
    }

    @Override
    public void delete(Client client) {
        if (client == null || client.getId() == null) {
            throw new IllegalArgumentException("Client id cant be null");
        }
        this.repository.delete(client);
    }

    @Override
    public Client update(Client client) {
        if (client == null || client.getId() == null) {
            throw new IllegalArgumentException("Client id cant be null");
        }
        return this.repository.save(client);
    }

    @Override
    public Page<Client> find(Client filter, Pageable pageRequest) {
        Example<Client> example = Example.of(filter,
                ExampleMatcher
                        .matching()
                        .withIgnoreCase()
                        .withIgnoreNullValues()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
        return repository.findAll(example, pageRequest);
    }

    @Override
    public List<Client> findALL() {
        return repository.findAll();
    }


}
