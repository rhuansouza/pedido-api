package com.costanzo.pedidoapi.service.impl;

import com.costanzo.pedidoapi.model.entity.Client;
import com.costanzo.pedidoapi.model.entity.Purchase;
import com.costanzo.pedidoapi.model.repository.PurchaseRepository;
import com.costanzo.pedidoapi.service.PurchaseService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private PurchaseRepository purchaseRepository;

    public PurchaseServiceImpl(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    public Purchase save(Purchase purchase) {
        return this.purchaseRepository.save(purchase);
    }

    @Override
    public Optional<Purchase> getById(Long id) {
        return  this.purchaseRepository.findById(id);
    }

    @Override
    public void delete(Purchase purchase) {
        if (purchase == null || purchase.getId() == null) {
            throw new IllegalArgumentException("Client id cant be null");
        }
        this.purchaseRepository.delete(purchase);
    }

    @Override
    public Purchase update(Purchase purchase) {
        if (purchase == null || purchase.getId() == null) {
            throw new IllegalArgumentException("Client id cant be null");
        }
        return this.purchaseRepository.save(purchase);
    }

    @Override
    public Page<Purchase> find(Purchase filter, Pageable pageRequest) {
        Example<Purchase> example = Example.of(filter,
                ExampleMatcher
                        .matching()
                        .withIgnoreCase()
                        .withIgnoreNullValues()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
        return purchaseRepository.findAll(example, pageRequest);
    }

    @Override
    public List<Purchase> findALL() {
        return purchaseRepository.findAll();
    }
}
