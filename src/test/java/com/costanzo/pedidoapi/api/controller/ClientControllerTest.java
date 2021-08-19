package com.costanzo.pedidoapi.api.controller;


import com.costanzo.pedidoapi.api.dto.ClientDTO;
import com.costanzo.pedidoapi.model.entity.Client;
import com.costanzo.pedidoapi.service.ClientService;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class) //versão do junit 5
@ActiveProfiles("test")//rodar com perfil de teste
@WebMvcTest(controllers = ClientController.class)
@AutoConfigureMockMvc
public class ClientControllerTest {


    static String CLIENT_API = "/api/clients";

    @Autowired
    MockMvc mvc;

    @MockBean
    ClientService clientService;


    @Test
    @DisplayName("Should create Client with Success.")
    public void createdClientTest() throws Exception{

        ClientDTO dto = createNewClientDTO();

        Client client = Client.builder()
                .id(10l)
                .name("rhuan")
                .address("professora")
                .city("gxp")
                .district("ouro")
                .phone("123")
                .build();

        when(clientService.save(any(Client.class))).thenReturn(client);

        //transforma um objeto em json
        String json = new ObjectMapper().writeValueAsString(dto);

        //definir uma requisição
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CLIENT_API)
                .contentType(MediaType.APPLICATION_JSON) //conteudo que estou passando
                .accept(MediaType.APPLICATION_JSON)//conteudo que o servidor aceita
                .content(json);//corpo da requisição

        mvc
                .perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("name").value("rhuan"))
                .andExpect(jsonPath("address").value("professora"))
                .andExpect(jsonPath("city").value("gxp"))
                .andExpect(jsonPath("district").value("ouro"))
                .andExpect(jsonPath("phone").value("123"));
    }



    @Test
    @DisplayName("Should get Client by Id.")
    public void getClientByIDTest() throws Exception{
        Long id = 10l;
        ClientDTO dto = createNewClientDTO();

        Client client = Client.builder()
                .id(10l)
                .name("rhuan")
                .address("professora")
                .city("gxp")
                .district("ouro")
                .phone("123")
                .build();

        when(clientService.getById(id)).thenReturn(Optional.of(client));

        //transforma um objeto em json
        String json = new ObjectMapper().writeValueAsString(dto);

        //definir uma requisição
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CLIENT_API+"/"+id)
                .accept(MediaType.APPLICATION_JSON)//conteudo que o servidor aceita
                .content(json);//corpo da requisição

        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("name").value("rhuan"))
                .andExpect(jsonPath("address").value("professora"))
                .andExpect(jsonPath("city").value("gxp"))
                .andExpect(jsonPath("district").value("ouro"))
                .andExpect(jsonPath("phone").value("123"));
    }


    @Test
    @DisplayName("Should get Client by Id Exception.")
    public void getClientByIDExceptionTest() throws Exception{
        Long id = 10l;
        ClientDTO dto = createNewClientDTO();


        when(clientService.getById(id)).thenReturn(Optional.empty());

        //transforma um objeto em json
        String json = new ObjectMapper().writeValueAsString(dto);

        //definir uma requisição
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CLIENT_API+"/"+id)
                .accept(MediaType.APPLICATION_JSON)//conteudo que o servidor aceita
                .content(json);//corpo da requisição

        mvc
                .perform(request)
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("Deve lançar erro de validação quando não houver dados suficientes para criação do cliente.")
    public void createdInvalidClientTest() throws Exception{

        String json = new ObjectMapper().writeValueAsString(new ClientDTO());


        //definir uma requisição
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CLIENT_API)
                .contentType(MediaType.APPLICATION_JSON) //conteudo que estou passando
                .accept(MediaType.APPLICATION_JSON)//conteudo que o servidor aceita
                .content(json);//corpo da requisição

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(5)));

    }

    @Test
    @DisplayName("Should delete client")
    public void deleteClientTest() throws Exception {

        Long id = 10l;
        Client clientReturn = Client.builder()
                .id(10l)
                .name("rhuan")
                .address("professora")
                .city("gxp")
                .district("ouro")
                .phone("123")
                .build();
        when(clientService.getById(id)).thenReturn(Optional.of(clientReturn));

        doNothing().when(clientService).delete(clientReturn);

        //execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(CLIENT_API.concat("/" + 10));

        mvc.perform(request)
                .andExpect(status().isNoContent());


    }



    @Test
    @DisplayName("deve retornar resource not found quando não encontrar  o livro para deletar")
    public void deleteNotfoundClientTest() throws Exception{
        //cenario
        when(clientService.getById(anyLong())).thenReturn(Optional.empty());

        //execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(CLIENT_API.concat("/" + 1));

        mvc.perform(request)
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("Deve atualizar um client")
    public void updateClientTest() throws Exception{
        //cenario
        Long id = 10L;

        Client updatingClient = Client.builder()
                .id(10l)
                .name("rhuan")
                .address("professora")
                .city("gxp")
                .district("ouro")
                .phone("123")
                .build();

        String json = new ObjectMapper().writeValueAsString(updatingClient);

        when(clientService.getById(anyLong())).thenReturn(Optional.of(updatingClient));

        Client updatedClient = Client.builder()
                .id(10l)
                .name("ana")
                .address("prof")
                .city("gxp")
                .district("ouro")
                .phone("123")
                .build();
        when(clientService.update(any(Client.class))).thenReturn(updatedClient);

        //execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(CLIENT_API.concat("/" + 10))
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);


        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("name").value(updatedClient.getName()));

    }

    @Test
    @DisplayName("Deve atualizar um client Exception")
    public void updateClientExceptionTest() throws Exception{
        //cenario
        Long id = 10L;

        Client updatingClient = Client.builder()
                .id(10l)
                .name("rhuan")
                .address("professora")
                .city("gxp")
                .district("ouro")
                .phone("123")
                .build();

        String json = new ObjectMapper().writeValueAsString(updatingClient);

        when(clientService.getById(anyLong())).thenReturn(Optional.empty());

        Client updatedClient = Client.builder()
                .id(10l)
                .name("ana")
                .address("prof")
                .city("gxp")
                .district("ouro")
                .phone("123")
                .build();
        when(clientService.update(any(Client.class))).thenReturn(updatedClient);

        //execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(CLIENT_API.concat("/" + 10))
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);


        mvc.perform(request)
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("Deve filtrar cliente")
    public void findClientTest() throws Exception{
        Long id = 1l;
        Client client = Client.builder()
                .id(id)
                .name(createNewClientDTO().getName())
                .address(createNewClientDTO().getAddress())
                .phone(createNewClientDTO().getPhone())
                .build();

        BDDMockito.given(clientService.find(Mockito.any(Client.class), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<Client>(Arrays.asList(client), PageRequest.of(0,100),1));

        String queryString = String.format("?name=%s&address=%s&page=0&size=100", client.getName(), client.getAddress());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CLIENT_API.concat(queryString))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("content", Matchers.hasSize(1)))
                .andExpect(jsonPath("totalElements").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(100))
                .andExpect(jsonPath("pageable.pageNumber").value(0));


    }


    @Test
    @DisplayName("Deve listar clientes")
    public void listClientTest() throws Exception{

        Client client = Client.builder()
                .id(10l)
                .name(createNewClientDTO().getName())
                .address(createNewClientDTO().getAddress())
                .phone(createNewClientDTO().getPhone())
                .build();

        List<Client> lista = Arrays.asList(client);
        when(clientService.findALL()).thenReturn(lista);


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CLIENT_API.concat("/list"))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isOk());

    }


    private ClientDTO createNewClientDTO() {
        return ClientDTO.builder()
                .name("rhuan")
                .address("professora")
                .city("gxp")
                .district("ouro")
                .phone("123")
                .build();
    }
}

