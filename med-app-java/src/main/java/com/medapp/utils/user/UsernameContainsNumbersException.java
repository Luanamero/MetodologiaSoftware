package com.medapp.utils.user;

public class UsernameContainsNumbersException extends InvalidUsernameException {
    public UsernameContainsNumbersException() {
        super("Username cannot contain numbers.");
    }
}