package com.medapp.utils.password;

public class PasswordMatchesUsernameException extends PasswordException {
    public PasswordMatchesUsernameException() {
        super("Password cannot be identical to username.");
    }
}
