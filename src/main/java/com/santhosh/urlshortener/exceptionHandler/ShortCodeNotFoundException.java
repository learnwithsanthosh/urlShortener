package com.santhosh.urlshortener.exceptionHandler;

public class ShortCodeNotFoundException extends RuntimeException {
    public ShortCodeNotFoundException(String shortCode) {
        super("Short code not found: " + shortCode);
    }
}