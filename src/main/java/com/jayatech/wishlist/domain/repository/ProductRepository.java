package com.jayatech.wishlist.domain.repository;

import com.jayatech.wishlist.domain.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository  extends MongoRepository<Product, String> {
}
