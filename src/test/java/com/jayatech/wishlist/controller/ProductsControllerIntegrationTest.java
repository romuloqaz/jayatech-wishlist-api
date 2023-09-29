package com.jayatech.wishlist.controller;

import com.jayatech.wishlist.domain.exception.ResourceNotFoundException;
import com.jayatech.wishlist.domain.model.Product;
import com.jayatech.wishlist.domain.model.Wishlist;
import com.jayatech.wishlist.domain.service.ProductService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ProductsControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @SpyBean
    private ProductService productService;

    @Test
    @SneakyThrows
    @DisplayName("Should return  products return status 200 OK")
    void products() {
        mvc.perform(MockMvcRequestBuilders.get("/products"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @DisplayName("Should return a list of products return status 200 OK")
    void products_listed() {
        doReturn(
                Arrays.asList(Product.builder()
                        .id("100")
                        .name("product test")
                        .price(BigDecimal.valueOf(12.45))
                        .description("product test")
                        .build(),
                        Product.builder()
                                .id("101")
                                .name("product test 2")
                                .price(BigDecimal.valueOf(20.60))
                                .description("product test 2")
                                .build())
        ).when(productService).findAll();

        mvc.perform(MockMvcRequestBuilders.get("/products"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*]", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", equalTo("100")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", equalTo("product test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].price", equalTo(12.45)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", equalTo("product test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", equalTo("101")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", equalTo("product test 2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].price", equalTo(20.60)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description", equalTo("product test 2")));
    }

    @Test
    @SneakyThrows
    @DisplayName("Should return a product's empty list and status 200 OK")
    void products_empty() {
        doReturn(
                Collections.emptyList()
        ).when(productService).findAll();
        mvc.perform(MockMvcRequestBuilders.get("/products"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*]", hasSize(0)));
    }


    @Test
    @SneakyThrows
    @DisplayName("Should return a status 404 not found")
    void product_by_id_not_found() {
        mvc.perform(MockMvcRequestBuilders.get("/products/100"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }


    @Test
    @SneakyThrows
    @DisplayName("Should return ResourceNotFoundException and status 404 not found")
    void product_by_id_not_found_exception() {
        doThrow(new ResourceNotFoundException(Wishlist.class.getName() +".not.found")).when(productService).findById("100");
        mvc.perform(MockMvcRequestBuilders.get("/v1/patient/100"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    @DisplayName("Should return a product and status 200 OK")
    void product_by_id() {
        doReturn(Product.builder()
                                .id("100")
                                .name("product test")
                                .price(BigDecimal.valueOf(12.45))
                                .description("product test")
                                .build()
        ).when(productService).findById("100");

        mvc.perform(MockMvcRequestBuilders.get("/products/100"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo("100")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", equalTo("product test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", equalTo(12.45)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", equalTo("product test")));
    }
}
