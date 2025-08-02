package com.medapp.utils.password;

public class PasswordTooWeakException extends PasswordException {
    public PasswordTooWeakException() {
        super("Password must contain at least 3 of the following: uppercase letters, lowercase letters, numbers, and special characters (!@#$%^&*()_+-=[]{}|').");
    }
}
