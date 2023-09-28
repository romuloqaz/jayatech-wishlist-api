package com.jayatech.wishlist.controller;

import com.jayatech.wishlist.domain.model.Product;
import com.jayatech.wishlist.domain.service.ProductService;
import lombok.SneakyThrows;
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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc

class ProductsControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @SpyBean
    private ProductService productService;

    @Test
    @SneakyThrows
    void products() {
        mvc.perform(MockMvcRequestBuilders.get("/products"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void products_stubbed() {
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
    void products_stubbed2() {
        doReturn(
                Collections.emptyList()
        ).when(productService).findAll();

        mvc.perform(MockMvcRequestBuilders.get("/products"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*]", hasSize(0)));
    }


    @Test
    @SneakyThrows
    void product_by_id() {
        mvc.perform(MockMvcRequestBuilders.get("/products/100"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void products_stubbed_ok() {
        doReturn(null).when(productService).findById("100");
        mvc.perform(MockMvcRequestBuilders.get("/products/100"))
                .andExpect(status().isOk());
    }
}
