package com.example.freeway.exception;

public class BadCredentialException extends RuntimeException {
    public BadCredentialException(String s) {
        super(s);
    }
}
