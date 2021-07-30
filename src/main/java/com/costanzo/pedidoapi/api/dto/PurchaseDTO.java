package com.costanzo.pedidoapi.api.dto;

import com.costanzo.pedidoapi.model.entity.Client;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PurchaseDTO {

    private Long id;
    private String name;
    private ClientDTO client;
}
