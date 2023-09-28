package com.jayatech.wishlist.domain.repository;

import com.jayatech.wishlist.domain.model.Wishlist;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface WishlistRepository extends MongoRepository <Wishlist, Long> {
    Wishlist findByUserId(Long userId);

}
