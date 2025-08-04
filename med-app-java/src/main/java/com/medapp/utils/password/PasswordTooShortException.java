package com.medapp.utils.password;

public class PasswordTooShortException extends PasswordException {
    public PasswordTooShortException(int minLength) {
        super("Password must be at least " + minLength + " characters long.");
    }
}