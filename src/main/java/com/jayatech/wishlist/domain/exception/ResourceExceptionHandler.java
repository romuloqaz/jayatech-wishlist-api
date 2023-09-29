package com.jayatech.wishlist.domain.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ResourceExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> objectNotFound(ResourceNotFoundException e,
                                                        HttpServletRequest request ) {
        StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.NOT_FOUND.value(), ResourceNotFoundException.EXCEPTION_MESSAGE, e.getMessage(), request.getRequestURI());
        log.error(e.getMessage() + " " + ResourceNotFoundException.class.getName());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);

    }

    @ExceptionHandler(RegisteredProductException.class)
    public ResponseEntity<StandardError> registeredProduct(RegisteredProductException e,
                                                        HttpServletRequest request ) {
        StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), RegisteredProductException.EXCEPTION_MESSAGE, e.getMessage(), request.getRequestURI());
        log.error(e.getMessage() + " " + RegisteredProductException.class.getName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);

    }

    @ExceptionHandler(WishListMaxSizeException.class)
    public ResponseEntity<StandardError> wishlistMaximumSize(WishListMaxSizeException e,
                                                           HttpServletRequest request ) {
        StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), WishListMaxSizeException.EXCEPTION_MESSAGE, e.getMessage(), request.getRequestURI());
        log.error(e.getMessage() + " " + WishListMaxSizeException.class.getName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);

    }

    @ExceptionHandler(InternalErrorException.class)
    public ResponseEntity<StandardError> internalError(InternalErrorException e,
                                                             HttpServletRequest request ) {
        StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), InternalErrorException.EXCEPTION_MESSAGE, e.getMessage(), request.getRequestURI());
        log.error(e.getMessage() + " " + InternalErrorException.class.getName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);

    }
}
