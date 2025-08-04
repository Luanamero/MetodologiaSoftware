package com.medapp.utils.user;

public class InvalidUsernameException extends UserException {
    public InvalidUsernameException(String message) {
        super(message);
    }
}