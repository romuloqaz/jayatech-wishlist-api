package com.jayatech.wishlist.api.controller;

import com.jayatech.wishlist.domain.service.ProductService;
import com.jayatech.wishlist.domain.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping()
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> list = productService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Optional<Product>> getProductById(@PathVariable String productId) {
        Optional<Product> product = productService.findById(productId);
        return ResponseEntity.ok().body(product);
    }

    //TODO remove this later
    @PostMapping()
    public ResponseEntity<Product> insertProduct(@RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.insert(product));
    }
}
