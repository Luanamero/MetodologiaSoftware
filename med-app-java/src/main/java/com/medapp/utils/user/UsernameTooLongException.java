package com.medapp.utils.user;

public class UsernameTooLongException extends InvalidUsernameException {
    public UsernameTooLongException() {
        super("Username cannot exceed 12 characters.");
    }
}