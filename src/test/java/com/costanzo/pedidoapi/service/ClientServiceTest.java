package com.costanzo.pedidoapi.service;

import com.costanzo.pedidoapi.model.entity.Client;
import com.costanzo.pedidoapi.model.repository.ClientRepository;
import com.costanzo.pedidoapi.service.impl.ClientServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ClientServiceTest {


    @MockBean
    ClientRepository clientRepository;

    ClientService clientService;

    @BeforeEach
    public void setup(){
        this.clientService = new ClientServiceImpl(clientRepository);
    }

    @Test
    @DisplayName("Should save a client")
    public void shouldSavedCLient(){
        //cenário
        Client clientSave = createValidClient();

        when(clientRepository.save(any(Client.class))).thenReturn(savedClient());
        //execução
        Client client = this.clientService.save(clientSave);
        //verificacao
        assertThat(client.getId()).isNotNull();
        assertThat(client.getName()).isEqualTo("rhuan");
        assertThat(client.getAddress()).isEqualTo("professora");
        assertThat(client.getCity()).isEqualTo("gxp");
        assertThat(client.getDistrict()).isEqualTo("ouro");
        assertThat(client.getPhone()).isEqualTo("123");
    }

    private Client createValidClient() {
        return Client.builder().name("rhuan")
                .address("professora")
                .city("gxp")
                .district("ouro")
                .phone("123")
                .build();
    }


    @Test
    @DisplayName("Should return client by Id")
    public void shouldGetCLientById(){
        //cenário
        Long id = 1l;
        Client clientSave = createValidClient();
        clientSave.setId(id);
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(clientSave));
        //execução
       Optional<Client> foundClient = clientService.getById(id);
        //verificação
        assertThat(foundClient.isPresent()).isTrue();
        assertThat(foundClient.get().getId()).isEqualTo(id);
        assertThat(foundClient.get().getName()).isEqualTo(clientSave.getName());
    }

    @Test
    @DisplayName("Should delete client")
    public void shouldDeleteClient(){
        //cenario
        Long id = 1l;
        Client client= createValidClient();
        client.setId(id);
        doNothing().when(clientRepository).delete(any(Client.class));
        //execução
        clientService.delete(client);
        //verificação
        verify(clientRepository).delete(any(Client.class));

    }

    @Test
    @DisplayName("Should not delete client and throw IllegalArgumentException")
    public void shouldNotDeleteClientAndThrowException() {
        //cenario
        Client client= createValidClient();
        //execução
        assertThrows(IllegalArgumentException.class,() ->  clientService.delete(client));
        //verificação
        verify(clientRepository, never()).delete(client);

    }

    @Test
    @DisplayName("Should update client")
    public void shouldUpdateClient(){

        //cenário
        Long id = 1l;
        Client clientSave = createValidClient();
        clientSave.setId(1l);

        when(clientRepository.save(any(Client.class))).thenReturn(savedClient());
        //execução
        Client client = this.clientService.update(clientSave);
        //verificacao
        assertThat(client.getId()).isEqualTo(savedClient().getId());
        assertThat(client.getName()).isEqualTo(savedClient().getName());
        assertThat(client.getAddress()).isEqualTo(savedClient().getAddress());
        assertThat(client.getCity()).isEqualTo(savedClient().getCity());
        assertThat(client.getDistrict()).isEqualTo(savedClient().getDistrict());
        assertThat(client.getPhone()).isEqualTo(savedClient().getPhone());

    }


    @Test
    @DisplayName("Should not update client and throw IllegalArgumentException")
    public void shouldNotUpdateClientAndThrowException(){
        //cenario
        Client client= createValidClient();
        //execução
        assertThrows(IllegalArgumentException.class,() ->  clientService.update(client));
        //verificação
        verify(clientRepository, never()).save(client);
    }


    @Test
    @DisplayName("Should filter customers by properties")
    public void shouldFilterClientByProperties(){
        //cenario
        Client clientSave = createValidClient();
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Client> lista = Arrays.asList(clientSave);
        Page<Client> page = new PageImpl<Client>(Arrays.asList(clientSave), pageRequest, 1);
        when(clientRepository.findAll(any(Example.class), any(PageRequest.class))).thenReturn(page);
        //execução
        Page<Client> result = clientService.find(clientSave, pageRequest);
        //verificação
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(lista);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);

    }

   @Test
   @DisplayName("Should list all Clients")
    public void shoulListAllClients(){
        //cenario
       Client clientSave = createValidClient();
       List<Client> listaCliente = Arrays.asList(clientSave);
       when(clientRepository.findAll()).thenReturn(listaCliente);
       //execução
      List<Client> result = clientService.findALL();
       //verificação
       assertThat(result.size()).isEqualTo(1);
       assertThat(result).isEqualTo(listaCliente);
    }



    private Client savedClient() {
        return Client.builder()
                .id(1l)
                .name("rhuan")
                .address("professora")
                .city("gxp")
                .district("ouro")
                .phone("123")
                .build();
    }




}
