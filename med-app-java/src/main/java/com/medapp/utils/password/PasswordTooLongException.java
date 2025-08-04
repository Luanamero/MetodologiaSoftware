package com.medapp.utils.password;

public class PasswordTooLongException extends PasswordException {
    public PasswordTooLongException(int maxLength) {
        super("Password cannot exceed " + maxLength + " characters.");
    }
}