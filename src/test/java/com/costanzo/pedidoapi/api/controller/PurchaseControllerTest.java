package com.costanzo.pedidoapi.api.controller;

import com.costanzo.pedidoapi.api.dto.ClientDTO;
import com.costanzo.pedidoapi.api.dto.PurchaseDTO;
import com.costanzo.pedidoapi.model.entity.Client;
import com.costanzo.pedidoapi.model.entity.Purchase;
import com.costanzo.pedidoapi.service.ClientService;
import com.costanzo.pedidoapi.service.PurchaseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class) //versão do junit 5
@ActiveProfiles("test")//rodar com perfil de teste
@WebMvcTest(controllers = PurchaseController.class)
@AutoConfigureMockMvc
public class PurchaseControllerTest {


    static String PURCHASE_API = "/api/purchases";

    @Autowired
    MockMvc mvc;

    @MockBean
    PurchaseService purchaseService;


    @MockBean
    ClientService clientService;



    @Test
    @DisplayName("Should create Client with Success.")
    public void createdPurchaseTest() throws Exception{

        PurchaseDTO dto = createNewPurchaseDTO();
        Long id = 1l;


        Client clientReturn = Client.builder()
                .id(1l)
                .name("rhuan")
                .address("professora")
                .city("gxp")
                .district("ouro")
                .phone("123")
                .build();

        Purchase purchase = Purchase.builder()
                .id(id)
                .name("DBZ")
                .client(clientReturn)
                .build();
        when(clientService.getById(id)).thenReturn(Optional.of(clientReturn));
        when(purchaseService.save(any(Purchase.class))).thenReturn(purchase);

        //transforma um objeto em json
        String json = new ObjectMapper().writeValueAsString(dto);

        //definir uma requisição
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(PURCHASE_API)
                .contentType(MediaType.APPLICATION_JSON) //conteudo que estou passando
                .accept(MediaType.APPLICATION_JSON)//conteudo que o servidor aceita
                .content(json);//corpo da requisição

        mvc
                .perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("name").value("DBZ"))
                .andExpect(jsonPath("client.name").value("rhuan"));

    }


    private PurchaseDTO createNewPurchaseDTO() {

         return PurchaseDTO.builder().id(1l)
                .name("DBZ")
                .client(createNewClientDTO())
                .build();
    }

    private ClientDTO createNewClientDTO() {
        return ClientDTO.builder()
                .id(1l)
                .name("rhuan")
                .address("professora")
                .city("gxp")
                .district("ouro")
                .phone("123")
                .build();
    }

}
