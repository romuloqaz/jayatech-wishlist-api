package com.jayatech.wishlist.domain.exception;

import org.springframework.core.NestedRuntimeException;

public class ProductNotFoundException extends NestedRuntimeException {
    public static final String FAILED_TO_QUERY = "The product doesn't exist";

    public ProductNotFoundException(String msg) {
        super(msg);
    }

    public ProductNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
