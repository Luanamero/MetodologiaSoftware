package com.medapp.utils.password;

public class PasswordMatchesEmailException extends PasswordException {
    public PasswordMatchesEmailException() {
        super("Password cannot be identical to email address.");
    }
}
