package com.fitcoach.snap2track.exception;

public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String email) {
        super("User with email " + email + " already exists");
    }

    public DuplicateEmailException(String email, Throwable cause) {
        super("User with email " + email + " already exists", cause);
    }
}