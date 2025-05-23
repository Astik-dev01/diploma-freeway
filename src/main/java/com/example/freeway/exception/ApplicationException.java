package com.example.freeway.exception;

public class ApplicationException extends RuntimeException {

    private final int statusCode;


    public ApplicationException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
