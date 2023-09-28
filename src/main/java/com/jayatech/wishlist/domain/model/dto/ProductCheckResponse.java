package com.jayatech.wishlist.domain.model.dto;

import com.jayatech.wishlist.domain.model.Product;
import lombok.Data;

@Data
public class ProductCheckResponse {

    private Product product;
    private boolean hasProductInWishlist;
    private String message;

    public ProductCheckResponse() {
        this.hasProductInWishlist = false;
        this.message = "product.not.found";
    }

    public ProductCheckResponse(Product product) {
        this.product = product;
        this.hasProductInWishlist = true;
        this.message = "product.found";
    }
}
