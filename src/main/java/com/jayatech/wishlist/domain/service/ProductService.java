package com.jayatech.wishlist.domain.service;

import com.jayatech.wishlist.domain.exception.ResourceNotFoundException;
import com.jayatech.wishlist.domain.model.Product;
import com.jayatech.wishlist.domain.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductService {

    public static final String PRODUCT_NOT_FOUND_EXCEPTION_MESSAGE = Product.class.getName() + ".not.found";

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    /**
     * Retrieves a list of products
     *
     * @return a list of all products.
     */
    public List<Product> findAll() {
        return productRepository.findAll();
    }


    /**
     * Retrieves a Product by the ProductId
     *
     * @param id is the product identifier
     * @return a Product related to the id
     * @throws ResourceNotFoundException when the Product is not found.
     */
    public Product findById(String id) {
        return productRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(PRODUCT_NOT_FOUND_EXCEPTION_MESSAGE));
    }
}
