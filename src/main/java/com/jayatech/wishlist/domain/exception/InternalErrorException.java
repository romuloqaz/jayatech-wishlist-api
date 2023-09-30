package com.jayatech.wishlist.domain.exception;

public class InternalErrorException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public static final String EXCEPTION_MESSAGE = "internal.error.exception";

    public InternalErrorException(String msg) {
        super(msg);
    }

    public InternalErrorException(String msg, Throwable cause) {
        super(msg, cause);
    }
}