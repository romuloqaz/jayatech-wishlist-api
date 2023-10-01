package com.jayatech.wishlist.api.controller;

import com.jayatech.wishlist.api.openapi.ProductControllerOpenApi;
import com.jayatech.wishlist.domain.service.ProductService;
import com.jayatech.wishlist.domain.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/products")
public class ProductController implements ProductControllerOpenApi {

    private final ProductService productService;


    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Override
    @GetMapping()
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok().body(productService.findAll());
    }

    @Override
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable String productId) {
        return ResponseEntity.ok().body(productService.findById(productId));
    }
}
