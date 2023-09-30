package com.jayatech.wishlist.service;

import com.jayatech.wishlist.domain.exception.ResourceNotFoundException;
import com.jayatech.wishlist.domain.model.Product;
import com.jayatech.wishlist.domain.repository.ProductRepository;
import com.jayatech.wishlist.domain.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    @DisplayName("Should get a product by Id")
    void getProduct_byId() {
        String id = "1";
        Product product = Product.builder()
                .id(id)
                .name("product test 1")
                .price(BigDecimal.valueOf(12.5))
                .description("product description 1")
                .build();
        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        Product result = productService.findById(id);
        assertEquals(product, result);
        assertThat(result).usingRecursiveComparison().isEqualTo(product);
    }

    @Test
    @DisplayName("Should throw product not found")
    void getProductId_ProductNotFound() {
        String id = "2";
        when(productRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productService.findById(id));
    }

    @Test
    @DisplayName("Should get all products")
    void getAllProducts() {
        List<Product> products = Arrays.asList(Product.builder()
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
                        .build());
        when(productRepository.findAll()).thenReturn(products);
        List<Product> productServiceAll = productService.findAll();

        assertEquals(productServiceAll, products);
        assertThat(products).usingRecursiveComparison().isEqualTo(productServiceAll);
    }

    @Test
    @DisplayName("Should retrieve empty list of products")
    void getAllProducts_emptyList() {
        when(productRepository.findAll()).thenReturn(new ArrayList<>());
        List<Product> products = productService.findAll();

        assertEquals(products, new ArrayList<>());
        assertThat(new ArrayList<>()).usingRecursiveComparison().isEqualTo(products);
    }
}