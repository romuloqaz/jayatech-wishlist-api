package com.jayatech.wishlist.domain.service;

import com.jayatech.wishlist.domain.model.Product;
import com.jayatech.wishlist.domain.model.WishListItem;
import com.jayatech.wishlist.domain.model.Wishlist;
import com.jayatech.wishlist.domain.model.dto.ProductCheckResponse;
import com.jayatech.wishlist.domain.repository.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class WishListService {

    @Autowired
    private WishListRepository wishListRepository;

    public Optional<Wishlist> findByUserId(String userId) {
        return wishListRepository.findByUserId(userId);
    }

    public Optional<Wishlist> findById(String id) {
        return wishListRepository.findById(id);
    }
    public Wishlist updateWishList (Wishlist wishlist, Product product){
        wishlist.getWishListItems().add(WishListItem.builder()
                    .id(UUID.randomUUID().toString())
                    .updatedAt(LocalDate.now())
                    .product(product)
                .build());
        wishlist.setUpdatedAt(LocalDate.now());
        return wishListRepository.save(wishlist);
    }
    public Wishlist remove (Wishlist wishlist, String wishListItemId){
        if (Objects.nonNull(wishlist.getWishListItems())) {
            wishlist.getWishListItems().removeIf(item -> item.getId().equals(wishListItemId));
        }
        wishlist.setUpdatedAt(LocalDate.now());
       return wishListRepository.save(wishlist);
    }

    public ProductCheckResponse checkProduct(Wishlist wishlist, String productId) {
        Product product = this.hasProductInWishlist(wishlist, productId);
        if (Objects.nonNull(product.getId())){
            return new ProductCheckResponse(product);
        }
        return new ProductCheckResponse();
    }

    public Product hasProductInWishlist(Wishlist wishlist, String productId){
        Product product = new Product();
        if (Objects.nonNull(wishlist) && Objects.nonNull(wishlist.getWishListItems())) {
            for (WishListItem item : wishlist.getWishListItems()) {
                if (item.getProduct().getId().equals(productId)) {
                    product = item.getProduct();
                }
            }
        }
        return product;
    }
}
