package com.medapp.use;

import com.medapp.utils.UserException;

public class UsernameValidator {
    public static void validate(String username) {
        if (username == null || username.isEmpty()) {
            throw new UserException("Username cannot be empty.");
        }
        
        if (username.length() > 12) {
            throw new UserException("Username cannot exceed 12 characters.");
        }
        
        if (username.matches(".*\\d.*")) {
            throw new UserException("Username cannot contain numbers.");
        }
    }
}
