package com.jayatech.wishlist.controller;

import com.jayatech.wishlist.api.controller.ProductController;
import com.jayatech.wishlist.domain.exception.ResourceNotFoundException;
import com.jayatech.wishlist.domain.model.Product;
import com.jayatech.wishlist.domain.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
    @DisplayName("Should get product by Id")
    void getProductId() {
        String id = "1";
        Product product = new Product();
        when(productService.findById(id)).thenReturn(product);
        ResponseEntity<Product> responseEntity = productController.getProductById(id);
        verify(productService).findById(id);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertSame(product, responseEntity.getBody());
    }

    @Test
    @DisplayName("Should throw a not found exception")
    void getProductId_ProductNotFound() {
        String id = "2";
        when(productService.findById(id)).thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () ->
            productController.getProductById(id)
        );
        verify(productService).findById(id);
    }

    @Test
    @DisplayName("Should get all products")
    void getListProduct() {
        List<Product> products = new ArrayList<>();
        when(productService.findAll()).thenReturn(products);
        ResponseEntity<List<Product>> responseEntity = productController.getAllProducts();
        verify(productService).findAll();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertSame(products, responseEntity.getBody());
    }
}
