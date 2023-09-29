package com.jayatech.wishlist.domain.service;

import com.jayatech.wishlist.domain.exception.*;
import com.jayatech.wishlist.domain.model.Product;
import com.jayatech.wishlist.domain.model.WishListItem;
import com.jayatech.wishlist.domain.model.Wishlist;
import com.jayatech.wishlist.domain.model.dto.ProductCheckResponse;
import com.jayatech.wishlist.domain.model.dto.ProductDTO;
import com.jayatech.wishlist.domain.repository.WishlistRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@Slf4j
public class WishlistService {

    public static final String WISHLIST_NOT_FOUND_EXCEPTION_MESSAGE = Wishlist.class.getName() +".not.found";
    public static final String REGISTERED_PRODUCT_EXCEPTION_MESSAGE = Product.class.getName() +".registered";
    public static final String WISHLIST_MAX_SIZE_EXCEPTION_MESSAGE = Wishlist.class.getName() +".maximum.size";
    public static final String WISHLIST_FOUND_EXCEPTION_MESSAGE = Wishlist.class.getName() +".found";
    private static final int WISHLIST_MAX_SIZE = 20;

    private final WishlistRepository wishListRepository;

    @Autowired
    public WishlistService(WishlistRepository wishListRepository) {
        this.wishListRepository = wishListRepository;
    }

    public Wishlist saveWishList(String userId) {
        if(wishListRepository.findByUserId(userId) != null) {
            throw new WishlistFoundException(WISHLIST_FOUND_EXCEPTION_MESSAGE);
        }
        try {
            return wishListRepository.save(Wishlist.builder()
                    .userId(userId)
                    .createdAt(Instant.now())
                    .wishListItems(new ArrayList<>())
                    .build());
        } catch (Exception e) {
            log.error("Failed to create wishlist", e);
            throw new InternalErrorException(e.getMessage(), e.getCause());
        }
    }

    //TODO order by date
    //TODO search if product in the wishlist by name
    public Wishlist findById(String wishlistId) {
        return wishListRepository.findById(wishlistId).orElseThrow(() ->
                new ResourceNotFoundException(WISHLIST_NOT_FOUND_EXCEPTION_MESSAGE));
    }

    public Wishlist updateWishList (Wishlist wishlist, ProductDTO productDTO){
        Product product = Product.builder()
                .id(productDTO.getId())
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .build();
       this.validateProduct(wishlist, product.getId());
        wishlist.getWishListItems().add(WishListItem.builder()
                    .id(UUID.randomUUID().toString())
                    .createdAt(Instant.now())
                    .product(product)
                .build());
        wishlist.setUpdatedAt(Instant.now());
        try {
            return wishListRepository.save(wishlist);
        } catch (Exception e) {
            log.error("Failed to update wishlist", e);
            throw new InternalErrorException(e.getMessage(), e.getCause());
        }
    }

    public void removeWishListProduct (Wishlist wishlist, String wishListItemId){
        if (Objects.nonNull(wishlist.getWishListItems())) {
            boolean hasProduct = wishlist.getWishListItems().removeIf(item -> item.getId().equals(wishListItemId));
            if(!hasProduct) {
                throw new ResourceNotFoundException(REGISTERED_PRODUCT_EXCEPTION_MESSAGE);
            }
        }
        wishlist.setUpdatedAt(Instant.now());
        try {
            wishListRepository.save(wishlist);
        } catch (Exception e) {
            log.error("Failed to remove wishlist product", e);
            throw new InternalErrorException(e.getMessage(), e.getCause());
        }
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

    public void validateProduct(Wishlist wishlist, String productId){
        Product hasProduct = this.hasProductInWishlist(wishlist, productId);
        if(hasProduct.getId() != null){
            throw new RegisteredProductException(REGISTERED_PRODUCT_EXCEPTION_MESSAGE);
        }
        if(wishlist.getWishListItems().size() == WISHLIST_MAX_SIZE) {
            throw new WishlistMaxSizeException(WISHLIST_MAX_SIZE_EXCEPTION_MESSAGE);
        }
    }
}
