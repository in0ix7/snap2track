package com.fitcoach.snap2track.exception;

public class InvalidRoleException extends RuntimeException {

    public InvalidRoleException(Long userId, String expectedRole) {
        super(String.format("User with id %d is not a %s", userId, expectedRole));
    }
}