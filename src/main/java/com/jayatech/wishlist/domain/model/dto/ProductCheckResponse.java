package com.jayatech.wishlist.domain.model.dto;

import com.jayatech.wishlist.domain.model.Product;
import lombok.Data;

@Data
public class ProductCheckResponse {

    private Product product;
    private boolean hasProductInWishlist;
    private String message;
    public static final String PRODUCT_NOT_FOUND = "product.not.found";
    public static final String PRODUCT_FOUND = "product.found";

    public ProductCheckResponse() {
        this.hasProductInWishlist = false;
        this.message = PRODUCT_NOT_FOUND;
    }

    public ProductCheckResponse(Product product) {
        this.product = product;
        this.hasProductInWishlist = true;
        this.message = PRODUCT_FOUND;
    }
}
