package com.jayatech.wishlist.domain.repository;

import com.jayatech.wishlist.domain.model.WishListItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WishListItemRepository extends MongoRepository<WishListItem, Long> {
}
