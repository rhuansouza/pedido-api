package com.costanzo.pedidoapi.model.repository;

import com.costanzo.pedidoapi.model.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
