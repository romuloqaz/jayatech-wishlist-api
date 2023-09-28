package com.jayatech.wishlist.domain.service;

import com.jayatech.wishlist.domain.model.Wishlist;
import com.jayatech.wishlist.domain.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    public Wishlist getWishList(Long userId) {
        return wishlistRepository.findByUserId(userId);
    }

}
