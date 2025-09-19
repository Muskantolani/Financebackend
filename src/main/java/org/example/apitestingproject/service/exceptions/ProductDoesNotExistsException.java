package org.example.apitestingproject.service.exceptions;

public class ProductDoesNotExistsException extends RuntimeException {
    public ProductDoesNotExistsException(String message) {
        super(message);
    }
}
