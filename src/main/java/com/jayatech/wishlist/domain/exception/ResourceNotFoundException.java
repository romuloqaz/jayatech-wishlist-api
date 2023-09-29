package com.jayatech.wishlist.domain.exception;

public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public static final String EXCEPTION_MESSAGE = "resource.not.found.exception";

    public ResourceNotFoundException(String msg) {
        super(msg);
    }
}
