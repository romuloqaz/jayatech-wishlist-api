package com.jayatech.wishlist.api.controller;

import com.jayatech.wishlist.api.openapi.WishlistControllerOpenApi;
import com.jayatech.wishlist.domain.model.Wishlist;
import com.jayatech.wishlist.domain.model.dto.ProductCheckResponse;
import com.jayatech.wishlist.domain.model.dto.UserDTO;
import com.jayatech.wishlist.domain.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/wishlist")
public class WishlistController implements WishlistControllerOpenApi {

    private final WishlistService wishlistService;

    @Autowired
    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @Override
    @PostMapping()
    public ResponseEntity<Wishlist> createWishlist(@RequestBody UserDTO user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(wishlistService.saveWishList(user.getUserId()));
    }

    @Override
    @GetMapping("/{wishlistId}")
    public ResponseEntity<Wishlist> getWishList(@PathVariable String wishlistId) {
        return ResponseEntity.ok().body(wishlistService.findById(wishlistId));
    }

    @Override
    @GetMapping("/{wishlistId}/check/{productId}")
    public ResponseEntity<ProductCheckResponse> checkWishList(@PathVariable String wishlistId, @PathVariable String productId) {
        Wishlist list = wishlistService.findById(wishlistId);
        ProductCheckResponse checkResponse = wishlistService.checkProduct(list, productId);
        return ResponseEntity.ok().body(checkResponse);
    }

    @Override
    @PostMapping("/{wishlistId}/items/{productId}")
    public ResponseEntity<Wishlist> incrementWishlist(@PathVariable String wishlistId, @PathVariable String productId) {
        Wishlist list = wishlistService.updateWishList(wishlistService.findById(wishlistId), productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(list);
    }

    @Override
    @DeleteMapping("{wishlistId}/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductWishList(@PathVariable String wishlistId, @PathVariable String itemId) {
        wishlistService.removeWishListProduct(wishlistService.findById(wishlistId), itemId);
    }
}
