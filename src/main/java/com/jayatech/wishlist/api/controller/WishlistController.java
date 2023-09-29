package com.jayatech.wishlist.api.controller;

import com.jayatech.wishlist.domain.model.Wishlist;
import com.jayatech.wishlist.domain.model.dto.ProductCheckResponse;
import com.jayatech.wishlist.domain.model.dto.ProductDTO;
import com.jayatech.wishlist.domain.model.dto.UserDTO;
import com.jayatech.wishlist.domain.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/wishlist")
public class WishlistController {
    //TODO ADD redis to get wishlist to not verify the database always

    private final WishlistService wishlistService;

    @Autowired
    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping()
    public ResponseEntity<Wishlist> createWishlist(@RequestBody UserDTO user) {
        //TODO treat duplicated wishlist
        return ResponseEntity.status(HttpStatus.CREATED).body(wishlistService.saveWishList(user.getId()));
    }

    @GetMapping("/{wishlistId}")
    public ResponseEntity<Wishlist> getWishList(@PathVariable String wishlistId) {
        return ResponseEntity.ok().body(wishlistService.findById(wishlistId));
    }

    @GetMapping("/{wishlistId}/check/{productId}")
    public ResponseEntity<ProductCheckResponse> checkWishList(@PathVariable String wishlistId,
                                                              @PathVariable String productId) {
        Wishlist list = wishlistService.findById(wishlistId);
        ProductCheckResponse checkResponse = wishlistService.checkProduct(list,productId);
        return ResponseEntity.ok().body(checkResponse);
    }
    //TODO check possible to insert not found

    //TODO create DTO to update values,
    @PostMapping("/{wishlistId}/items")
    public ResponseEntity<Wishlist> incrementWishlist(@PathVariable String wishlistId,
                                                      @RequestBody ProductDTO product) {
        Wishlist list = wishlistService.updateWishList(wishlistService.findById(wishlistId), product);
        return ResponseEntity.status(HttpStatus.CREATED).body(list);
    }

    @DeleteMapping("{wishlistId}/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductWishList(@PathVariable String wishlistId,
                                                          @PathVariable String itemId) {
        wishlistService.removeWishListProduct(wishlistService.findById(wishlistId), itemId);
    }
    //TODO search if product in the wishlist by name
}
