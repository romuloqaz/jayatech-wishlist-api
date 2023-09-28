package com.jayatech.wishlist.domain.repository;

import com.jayatech.wishlist.domain.model.Wishlist;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WishListRepository extends MongoRepository<Wishlist, String> {
    Optional<Wishlist> findByUserId(String userId);
}
