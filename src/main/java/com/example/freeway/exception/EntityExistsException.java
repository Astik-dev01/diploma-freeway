package com.example.freeway.exception;

public class EntityExistsException extends RuntimeException {
    public EntityExistsException(String e) {
        super(e);
    }
}
