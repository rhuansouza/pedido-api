package com.costanzo.pedidoapi.model.repository;

import com.costanzo.pedidoapi.model.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ClientRepository extends JpaRepository<Client, Long> {

}
