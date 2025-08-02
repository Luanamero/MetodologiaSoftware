package com.medapp.utils.password;

public class EmptyPasswordException extends PasswordException {
    public EmptyPasswordException() {
        super("Password cannot be null.");
    }
}
