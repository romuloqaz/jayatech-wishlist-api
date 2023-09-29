package com.jayatech.wishlist.domain.exception;

public class WishlistMaxSizeException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public static final String EXCEPTION_MESSAGE = "wishlist.maximum.size.exception";

    public WishlistMaxSizeException(String msg) {
        super(msg);
    }
}
