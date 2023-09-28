package com.jayatech.wishlist.domain.service;

import com.jayatech.wishlist.domain.exception.ProductNotFoundException;
import com.jayatech.wishlist.domain.model.Product;
import com.jayatech.wishlist.domain.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(String id) {
        return productRepository.findById(id);
    }
    public Product insert (Product product){
        return productRepository.save(product);
    }
}
