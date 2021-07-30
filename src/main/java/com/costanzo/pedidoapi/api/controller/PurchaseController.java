package com.costanzo.pedidoapi.api.controller;

import com.costanzo.pedidoapi.api.dto.ClientDTO;
import com.costanzo.pedidoapi.api.dto.PurchaseDTO;
import com.costanzo.pedidoapi.model.entity.Client;
import com.costanzo.pedidoapi.model.entity.Purchase;
import com.costanzo.pedidoapi.service.ClientService;
import com.costanzo.pedidoapi.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
@Slf4j
public class PurchaseController {

    private final ModelMapper modelMapper;

    private final PurchaseService purchaseService;
    private final ClientService clientService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody @Valid PurchaseDTO purchaseDTO) {
        log.info("creating a purchase");
        Client client = clientService.getById(purchaseDTO.getClient().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Client not found for passed id"));

        Purchase entity = Purchase.builder()
                .client(client)
                .name(purchaseDTO.getName())
                .purchaseDate(LocalDate.now())
                .build();

        entity = purchaseService.save(entity);
        return entity.getId();

    }

    @GetMapping("{id}")
    public PurchaseDTO get(@PathVariable Long id){
        log.info("obtaining details for purchase by id: {}", id);
        /*return  purchaseService
                .getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));*/
        return  purchaseService
                .getById(id)
                .map(purchase -> modelMapper.map(purchase, PurchaseDTO.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }


    @GetMapping
    public Page<PurchaseDTO> find(PurchaseDTO dto, Pageable pageRequest){
        Purchase filter = modelMapper.map(dto, Purchase.class);
        Page<Purchase> result = purchaseService.find(filter, pageRequest);
        List<PurchaseDTO> list = result
                .get()
                .map(entity -> {
                    Client client = entity.getClient();
                    ClientDTO clientDTO = modelMapper.map(client, ClientDTO.class);
                    PurchaseDTO purchaseDTO =  modelMapper.map(entity, PurchaseDTO.class);
                    purchaseDTO.setClient(clientDTO);
                    return purchaseDTO;
                }).collect(Collectors.toList());

        return new PageImpl<PurchaseDTO>(list, pageRequest, result.getTotalElements());
    }



}
