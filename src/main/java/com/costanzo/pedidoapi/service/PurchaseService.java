package com.costanzo.pedidoapi.service;

import com.costanzo.pedidoapi.api.dto.PurchaseDTO;
import com.costanzo.pedidoapi.model.entity.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PurchaseService {

    Purchase save(Purchase purchase);

    Optional<Purchase> getById(Long id);

    void delete(Purchase purchase);

    Purchase update(Purchase purchase);

    Page<Purchase> find(Purchase filter, Pageable pageRequest);

    List<Purchase> findALL();
}
