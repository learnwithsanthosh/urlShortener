package com.santhosh.urlshortener.exception;

public class MaxRetriesExceededException extends RuntimeException{
    public MaxRetriesExceededException(String message) {
        super(message);
    }
}
