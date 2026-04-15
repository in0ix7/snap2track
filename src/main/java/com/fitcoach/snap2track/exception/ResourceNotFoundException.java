package com.fitcoach.snap2track.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s not found with id: %d", resourceName, id));
    }

    public ResourceNotFoundException(String resourceName, String email) {
        super(String.format("%s not found with email: %s", resourceName, email));
    }
}