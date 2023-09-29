package com.jayatech.wishlist.domain.exception;

public class WishlistFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public static final String EXCEPTION_MESSAGE = "wishlist.found.exception";

    public WishlistFoundException(String msg) {
        super(msg);
    }
}