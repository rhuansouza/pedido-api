package com.costanzo.pedidoapi.service;


import com.costanzo.pedidoapi.model.entity.Client;
import com.costanzo.pedidoapi.model.entity.Purchase;
import com.costanzo.pedidoapi.model.repository.PurchaseRepository;
import com.costanzo.pedidoapi.service.impl.PurchaseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class PurchaseServiceTest {

    @MockBean
    PurchaseRepository purchaseRepository;

    PurchaseService purchaseService;

    @BeforeEach
    public void setup(){
        this.purchaseService = new PurchaseServiceImpl(purchaseRepository);
    }


    @Test
    @DisplayName("Should save a purchase")
    public void shouldSavedCLient(){
        //cenário
        Purchase purchaseSave = createValidPurchase();

        when(purchaseRepository.save(any(Purchase.class))).thenReturn(savedPurchase());
        //execução
        Purchase purchase = this.purchaseService.save(purchaseSave);
        //verificacao
         assertThat(purchase.getId()).isNotNull();
         assertThat(purchase.getName()).isEqualTo("developers");
         assertThat(purchase.getClient().getName()).isEqualTo("rhuan");

    }


    @Test
    @DisplayName("Should delete client")
    public void shouldDeleteClient(){
        //cenario
        Long id = 1l;
        Purchase purchase = createValidPurchase();
        purchase.setId(id);
        doNothing().when(purchaseRepository).delete(any(Purchase.class));
        //execução
        purchaseService.delete(purchase);
        //verificação
        verify(purchaseRepository).delete(any(Purchase.class));

    }


    @Test
    @DisplayName("Should not delete purchase and throw IllegalArgumentException")
    public void shouldNotDeleteClientAndThrowException() {
        //cenario
        Purchase purchase = createValidPurchase();
        //execução
        assertThrows(IllegalArgumentException.class,() ->  purchaseService.delete(purchase));
        //verificação
        verify(purchaseRepository, never()).delete(purchase);

    }

    @Test
    @DisplayName("Should update client")
    public void shouldUpdatePurchase(){

        //cenário
        Long id = 1l;
        Purchase purchaseSave = createValidPurchase();
        purchaseSave.setId(1l);

        when(purchaseRepository.save(any(Purchase.class))).thenReturn(savedPurchase());
        //execução
        Purchase purchase = this.purchaseService.update(purchaseSave);
        //verificacao
        assertThat(purchase.getId()).isEqualTo(savedPurchase().getId());
        assertThat(purchase.getName()).isEqualTo(savedPurchase().getName());


    }


    @Test
    @DisplayName("Should not update client and throw IllegalArgumentException")
    public void shouldNotUpdatePurchaseAndThrowException(){
        //cenario
        Purchase purchase = createValidPurchase();
        //execução
        assertThrows(IllegalArgumentException.class,() ->  purchaseService.update(purchase));
        //verificação
        verify(purchaseRepository, never()).save(purchase);
    }


    @Test
    @DisplayName("Should filter purchase by properties")
    public void shouldFilterPurchaseByProperties(){
        //cenario
        Purchase purchaseSave = createValidPurchase();
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Purchase> lista = Arrays.asList(purchaseSave);
        Page<Purchase> page = new PageImpl<Purchase>(Arrays.asList(purchaseSave), pageRequest, 1);
        when(purchaseRepository.findAll(any(Example.class), any(PageRequest.class))).thenReturn(page);
        //execução
        Page<Purchase> result = purchaseService.find(purchaseSave, pageRequest);
        //verificação
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(lista);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);

    }


    @Test
    @DisplayName("Should list all purchases")
    public void shoulListAllPurchases(){
        //cenario
        Purchase purchaseSave = createValidPurchase();
        List<Purchase> listaPurchase = Arrays.asList(purchaseSave);
        when(purchaseRepository.findAll()).thenReturn(listaPurchase);
        //execução
        List<Purchase> result = purchaseService.findALL();
        //verificação
        assertThat(result.size()).isEqualTo(1);
        assertThat(result).isEqualTo(listaPurchase);
    }


    @Test
    @DisplayName("Should return purchase by Id")
    public void shouldGetPurchaseById(){
        //cenário
        Long id = 1l;
        Purchase purchaseSave = createValidPurchase();
        purchaseSave.setId(id);
        when(purchaseRepository.findById(anyLong())).thenReturn(Optional.of(purchaseSave));
        //execução
        Optional<Purchase> foundClient = purchaseService.getById(id);
        //verificação
        assertThat(foundClient.isPresent()).isTrue();
        assertThat(foundClient.get().getId()).isEqualTo(id);
        assertThat(foundClient.get().getName()).isEqualTo(purchaseSave.getName());
    }



    private Purchase createValidPurchase() {
        Client client = Client.builder().id(1l).name("rhuan").build();
        return Purchase.builder().name("developers").client(client).build();
    }


    private Purchase savedPurchase() {
        Client client = Client.builder().id(1l).name("rhuan").build();
        return Purchase.builder()
                .id(1l)
                .name("developers")
                .purchaseDate(LocalDate.now())
                .client(client)
                .build();
    }



}
