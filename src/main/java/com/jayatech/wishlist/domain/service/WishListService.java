package com.jayatech.wishlist.domain.service;

import com.jayatech.wishlist.domain.exception.InternalErrorException;
import com.jayatech.wishlist.domain.exception.RegisteredProductException;
import com.jayatech.wishlist.domain.exception.ResourceNotFoundException;
import com.jayatech.wishlist.domain.exception.WishListMaxSizeException;
import com.jayatech.wishlist.domain.model.Product;
import com.jayatech.wishlist.domain.model.WishListItem;
import com.jayatech.wishlist.domain.model.Wishlist;
import com.jayatech.wishlist.domain.model.dto.ProductCheckResponse;
import com.jayatech.wishlist.domain.repository.WishListRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@Slf4j
public class WishListService {

    public static final String WISHLIST_NOT_FOUND_EXCEPTION_MESSAGE = Wishlist.class.getName() +".not.found";
    public static final String REGISTERED_PRODUCT_EXCEPTION_MESSAGE = Product.class.getName() +".registered";
    public static final String WISHLIST_MAX_SIZE_EXCEPTION_MESSAGE = Wishlist.class.getName() +".maximum.size";
    private static final int WISHLIST_MAX_SIZE = 20;

    private final WishListRepository wishListRepository;

    @Autowired
    public WishListService(WishListRepository wishListRepository) {
        this.wishListRepository = wishListRepository;
    }

    public Wishlist findByUserId(String userId) {
        return wishListRepository.findByUserId(userId).orElseGet(() ->
                this.saveWishList(userId));
    }

    public Wishlist saveWishList(String userId) {
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

    public Wishlist findById(String id) {
        return wishListRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(WISHLIST_NOT_FOUND_EXCEPTION_MESSAGE));
    }

    public Wishlist updateWishList (Wishlist wishlist, Product product){
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
            throw new WishListMaxSizeException(WISHLIST_MAX_SIZE_EXCEPTION_MESSAGE);
        }
    }
}
