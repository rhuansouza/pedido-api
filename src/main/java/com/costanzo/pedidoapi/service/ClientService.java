package com.costanzo.pedidoapi.service;

import com.costanzo.pedidoapi.model.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    Client save(Client client);

    Optional<Client> getById(Long id);

    void delete(Client client);

    Client update(Client client);

    Page<Client> find(Client filter, Pageable pageRequest);

    List<Client> findALL();



}
