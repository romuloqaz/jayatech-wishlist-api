package com.jayatech.wishlist.controller;

import com.jayatech.wishlist.api.controller.ProductController;
import com.jayatech.wishlist.domain.model.Product;
import com.jayatech.wishlist.domain.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductsControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(
                productService
        );
    }

    @Test
    void getProductId() {
        Optional<Product> product = Optional.of(new Product());
        when(productService.findById("1")).thenReturn(product);
        ResponseEntity<Optional<Product>> responseEntity = productController.getProductById("1");
        verify(productService).findById("1");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertSame(product, responseEntity.getBody());
    }

    @Test
    void getListProduct() {
        List<Product> products = new ArrayList<>();
        when(productService.findAll()).thenReturn(products);
        ResponseEntity<List<Product>> responseEntity = productController.getAllProducts();
        verify(productService).findAll();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertSame(products, responseEntity.getBody());
    }
}
