package com.jayatech.wishlist.api.controller;

import com.jayatech.wishlist.domain.model.Wishlist;
import com.jayatech.wishlist.domain.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/wishlist")
public class WishListController {

    @Autowired
    private WishlistService wishlistService;

    @GetMapping("/{userId}")
    public ResponseEntity<Wishlist> getWishList(@PathVariable Long userId) {
        Wishlist wishlist = wishlistService.getWishList(userId);
        return ResponseEntity.ok().body(wishlist);
    }

}
