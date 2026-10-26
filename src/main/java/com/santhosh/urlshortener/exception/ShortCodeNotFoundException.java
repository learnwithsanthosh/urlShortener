package com.santhosh.urlshortener.exception;

public class ShortCodeNotFoundException extends RuntimeException {
    public ShortCodeNotFoundException(String shortCode) {
        super("Short code not found: " + shortCode);
    }
}