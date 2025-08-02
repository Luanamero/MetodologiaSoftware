package com.medapp.utils.user;

public class EmptyUsernameException extends InvalidUsernameException {
    public EmptyUsernameException() {
        super("Username cannot be empty.");
    }
}
