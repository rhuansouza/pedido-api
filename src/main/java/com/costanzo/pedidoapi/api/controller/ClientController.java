package com.costanzo.pedidoapi.api.controller;

import com.costanzo.pedidoapi.api.dto.ClientDTO;
import com.costanzo.pedidoapi.model.entity.Client;
import com.costanzo.pedidoapi.service.ClientService;
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
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private final ModelMapper modelMapper;

    private final ClientService clientService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientDTO create(@RequestBody @Valid ClientDTO clientDTO){
        log.info("creating a client",clientDTO.toString());
        Client client = modelMapper.map(clientDTO, Client.class);
        client = clientService.save(client);
        return modelMapper.map(client, ClientDTO.class);
    }

    @GetMapping("{id}")
    public ClientDTO get(@PathVariable Long id){
        log.info("obtaining details for client by id: {}", id);
        return clientService
                .getById(id)
                .map(client -> modelMapper.map(client, ClientDTO.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id){
        log.info("deleteting client of id: {}", id);
       Client client =  clientService.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
       clientService.delete(client);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ClientDTO update(@PathVariable Long id, @RequestBody @Valid ClientDTO clientDTO){
        log.info("updating client of id: {}", id);
        return clientService.getById(id).map(client -> {
            client.setName(clientDTO.getName());
            client.setAddress(clientDTO.getAddress());
            client.setDistrict(clientDTO.getDistrict());
            client.setCity(clientDTO.getCity());
            client.setPhone(clientDTO.getPhone());
            client = clientService.update(client);
            return modelMapper.map(client, ClientDTO.class);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


    @GetMapping
    public Page<ClientDTO> find(ClientDTO clientDTO, Pageable pageRequest){
        log.info("search client by parameters");
        Client filter = modelMapper.map(clientDTO, Client.class);
        Page<Client> result = clientService.find(filter, pageRequest);
        List<ClientDTO> list = result
                .get()
                .map(entity -> modelMapper.map(entity, ClientDTO.class)).collect(Collectors.toList());

        return new PageImpl<ClientDTO>(list, pageRequest, result.getTotalElements());
    }

    @GetMapping("list")
    public List<ClientDTO> list(){
       List<Client> listClient =  clientService.findALL();
       return listClient.stream().map(entity -> modelMapper.map(entity, ClientDTO.class)).collect(Collectors.toList());
    }



}
