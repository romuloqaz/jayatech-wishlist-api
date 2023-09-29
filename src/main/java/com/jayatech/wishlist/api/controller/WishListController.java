package com.jayatech.wishlist.api.controller;

import com.jayatech.wishlist.domain.model.Product;
import com.jayatech.wishlist.domain.model.Wishlist;
import com.jayatech.wishlist.domain.model.dto.ProductCheckResponse;
import com.jayatech.wishlist.domain.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/wishlist")
public class WishListController {
    //TODO ADD redis to get wishlist to not verify the database always

    private final WishListService wishlistService;

    @Autowired
    public WishListController(WishListService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Wishlist> createWishlist(@PathVariable String userId) {
        return ResponseEntity.ok().body(wishlistService.findByUserId(userId));
    }

    @GetMapping("/{wishlistId}")
    public ResponseEntity<Wishlist> getWishList(@PathVariable String wishlistId) {
        return ResponseEntity.ok().body(wishlistService.findById(wishlistId));
    }

    @GetMapping("/{wishlistId}/check/{productId}")
    public ResponseEntity<ProductCheckResponse> checkWishList(@PathVariable String wishlistId,
                                                              @PathVariable String productId) {
        ProductCheckResponse checkResponse = wishlistService.checkProduct(wishlistService.findById(wishlistId), productId);
        return ResponseEntity.ok().body(checkResponse);
    }

    //TODO create DTO to update values,
    @PostMapping("/{wishlistId}/items")
    public ResponseEntity<Wishlist> incrementWishList(@PathVariable String wishlistId,
                                                      @RequestBody Product product) {
        Wishlist list = wishlistService.updateWishList(wishlistService.findById(wishlistId), product);
        return ResponseEntity.status(HttpStatus.CREATED).body(list);
    }

    @DeleteMapping("{wishlistId}/items/{itemId}")
    public void deleteProductWishList(@PathVariable String wishlistId,
                                                          @PathVariable String itemId) {
        wishlistService.removeWishListProduct(wishlistService.findById(wishlistId), itemId);
    }
    //TODO search if product in the wishlist by name
}
