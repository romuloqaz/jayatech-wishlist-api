package com.jayatech.wishlist.domain.service;

import com.jayatech.wishlist.domain.exception.*;
import com.jayatech.wishlist.domain.model.Product;
import com.jayatech.wishlist.domain.model.WishListItem;
import com.jayatech.wishlist.domain.model.Wishlist;
import com.jayatech.wishlist.domain.model.dto.ProductCheckResponse;
import com.jayatech.wishlist.domain.repository.WishlistRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@Slf4j
public class WishlistService {

    public static final String WISHLIST_NOT_FOUND_EXCEPTION_MESSAGE = Wishlist.class.getName() + ".not.found";
    public static final String WISHLIST_ITEM_NOT_FOUND_EXCEPTION_MESSAGE = WishListItem.class.getName() + ".not.found";
    public static final String REGISTERED_PRODUCT_EXCEPTION_MESSAGE = Product.class.getName() + ".registered";
    public static final String WISHLIST_MAX_SIZE_EXCEPTION_MESSAGE = Wishlist.class.getName() + ".maximum.size";
    public static final String WISHLIST_FOUND_EXCEPTION_MESSAGE = Wishlist.class.getName() + ".found";
    public static final int WISHLIST_MAX_SIZE = 20;

    private final WishlistRepository wishlistRepository;

    private final ProductService productService;

    @Autowired
    public WishlistService(WishlistRepository wishListRepository, ProductService productService) {
        this.wishlistRepository = wishListRepository;
        this.productService = productService;
    }

    /**
     * Saves a new Wishlist based on the userId
     *
     * @param userId is the identifier of the user the wishlist belongs to
     * @return a created Wishlist
     * @throws WishlistFoundException when there is a Wishlist saved with the same userId
     * @throws InternalErrorException when an internal error occurs
     */
    public Wishlist saveWishList(String userId) {
        if (wishlistRepository.findByUserId(userId).isPresent()) {
            throw new WishlistFoundException(WISHLIST_FOUND_EXCEPTION_MESSAGE);
        }
        try {
            return wishlistRepository.save(Wishlist.builder()
                    .userId(userId)
                    .createdAt(Instant.now())
                    .wishListItems(new ArrayList<>())
                    .build());
        } catch (Exception e) {
            log.error("Failed to create wishlist", e);
            throw new InternalErrorException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Retrieves a Wishlist by the wishlistId
     *
     * @param wishlistId is the wishlist identifier
     * @return a Wishlist related to the id
     * @throws ResourceNotFoundException when the Wishlist is not found.
     */
    public Wishlist findById(String wishlistId) {
        Wishlist wishlist = wishlistRepository.findById(wishlistId).orElseThrow(() ->
                new ResourceNotFoundException(WISHLIST_NOT_FOUND_EXCEPTION_MESSAGE));

        //The next line sorts each item on the wishlist item based on the createdAt attribute.
        wishlist.getWishListItems().sort(Comparator.comparing(WishListItem::getCreatedAt));
        return wishlist;
    }

    /**
     * Includes a new WishlistItem on a Wishlist
     *
     * @param wishlist is the user's wishlist
     * @param productId is the product identifier that will be included from the wishlist
     * @return a Wishlist related to the id
     * @throws InternalErrorException when an internal error occurs
     */
    public Wishlist updateWishList(Wishlist wishlist, String productId) {
        Product product = productService.findById(productId);

        this.validateProduct(wishlist, product.getId());
        wishlist.getWishListItems().add(WishListItem.builder()
                .id(UUID.randomUUID().toString())
                .createdAt(Instant.now())
                .product(product)
                .build());
        wishlist.setUpdatedAt(Instant.now());
        try {
            return wishlistRepository.save(wishlist);
        } catch (Exception e) {
            log.error("Failed to update wishlist", e);
            throw new InternalErrorException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Removes the product from a wishlist
     *
     * @param wishlist is the user's wishlist
     * @param wishListItemId is the wishlist item identifier that will be removed from the wishlist
     * @throws ResourceNotFoundException when a wishlist item is not found
     * @throws InternalErrorException when an internal error occurs
     */
    public void removeWishListProduct(Wishlist wishlist, String wishListItemId) {
        if (Objects.nonNull(wishlist.getWishListItems())) {
            List<WishListItem> updatedItems = new ArrayList<>(wishlist.getWishListItems());
            boolean hasProduct = updatedItems.removeIf(item ->
                    item.getId().equals(wishListItemId));
            if (!hasProduct) {
                throw new ResourceNotFoundException(WISHLIST_ITEM_NOT_FOUND_EXCEPTION_MESSAGE);
            }
            wishlist.setWishListItems(updatedItems);
            wishlist.setUpdatedAt(Instant.now());
        }
        try {
            wishlistRepository.save(wishlist);
        } catch (Exception e) {
            log.error("Failed to remove wishlist product", e);
            throw new InternalErrorException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Checks if the product is on the Wishlist
     *
     * @param wishlist is the user's wishlist
     * @param productId is the product item identifier that will be verified
     * @return a ProductCheck response with the product if the product is on the Wishlist
     */
    public ProductCheckResponse checkProduct(Wishlist wishlist, String productId) {
        Product product = this.hasProductInWishlist(wishlist, productId);
        if (Objects.nonNull(product.getId())) {
            return new ProductCheckResponse(product);
        }
        return new ProductCheckResponse();
    }

    /**
     * Verifies if the product is on the Wishlist
     *
     * @param wishlist is the user's wishlist
     * @param productId is the product item identifier that will be verified
     * @return a product with its values if there is a product in a wishlist as the productId
     */
    public Product hasProductInWishlist(Wishlist wishlist, String productId) {
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

    /**
     * Validates whether the product is on the wish list
     * Validates the maximum size of the wish list
     *
     * @param wishlist is the user's wishlist
     * @param productId is the product that will be validated
     * @throws RegisteredProductException when the product is on the wishlist
     * @throws WishlistMaxSizeException when insert more products than the wishlist size
     */
    public void validateProduct(Wishlist wishlist, String productId) {
        Product hasProduct = this.hasProductInWishlist(wishlist, productId);
        if (hasProduct.getId() != null) {
            throw new RegisteredProductException(REGISTERED_PRODUCT_EXCEPTION_MESSAGE);
        }
        if (wishlist.getWishListItems().size() == WISHLIST_MAX_SIZE) {
            throw new WishlistMaxSizeException(WISHLIST_MAX_SIZE_EXCEPTION_MESSAGE);
        }
    }
}
