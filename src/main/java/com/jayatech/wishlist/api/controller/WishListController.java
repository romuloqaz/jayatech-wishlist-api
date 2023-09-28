package com.jayatech.wishlist.api.controller;

import com.jayatech.wishlist.domain.model.Product;
import com.jayatech.wishlist.domain.model.Wishlist;
import com.jayatech.wishlist.domain.model.dto.ProductCheckResponse;
import com.jayatech.wishlist.domain.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping(value = "/wishlist")
public class WishListController {

    @Autowired
    private WishListService wishlistService;

    //TODO inject class in a constructor
    //TODO ADD redis to get wishlist to not verify the database always

    @GetMapping("/{userId}")
    public ResponseEntity<Optional<Wishlist>> getWishList(@PathVariable String userId) {
        Optional<Wishlist> wishlist = wishlistService.findByUserId(userId);
        //TODO verify if there is a wishList for the user just here
        return ResponseEntity.ok().body(wishlist);
    }

    @GetMapping("/{userId}/check/{productId}")
    public ProductCheckResponse checkWishList(@PathVariable String userId,
                                              @PathVariable String productId) {
        Wishlist wishlist = wishlistService.findByUserId(userId).orElse(null);
        return wishlistService.checkProduct(wishlist, productId);
    }

    @PostMapping("/{id}/items")
    public Wishlist incrementWishList(@PathVariable String id,
                                                       @RequestBody Product product) {
        Wishlist wishlist = wishlistService.findById(id).orElse(null);
        //TODO null validation and wishlist size, create DTO to update values, verify if there is product in the wishlist
       return wishlistService.updateWishList(wishlist, product);
    }

    @DeleteMapping("{id}/items/{itemId}")
    public Wishlist deleteProductWishList(@PathVariable String id,
                                          @PathVariable String itemId) {
        //TODO validation if user has wishlist and if params is not null
        Wishlist wishlist = wishlistService.findById(id).orElse(null);
        return wishlistService.remove(wishlist, itemId);
    }

}
