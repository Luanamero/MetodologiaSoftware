package com.medapp.use;

import com.medapp.models.User;
import com.medapp.utils.PasswordException;

public class PasswordValidator {
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 128;
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()_+-=[]{}|'";
    
    public static void validate(User user) {
        String password = user.getPassword();
        String username = user.getUsername();
        String email = user.getEmail();
        
        if (password == null) {
            throw new PasswordException("Password cannot be null.");
        }
        
        // Check minimum and maximum length
        if (password.length() < MIN_LENGTH) {
            throw new PasswordException("Password must be at least " + MIN_LENGTH + " characters long.");
        }
        
        if (password.length() > MAX_LENGTH) {
            throw new PasswordException("Password cannot exceed " + MAX_LENGTH + " characters.");
        }
        
        // Check for at least 3 of the 4 character types
        int characterTypeCount = 0;
        
        if (hasUppercase(password)) {
            characterTypeCount++;
        }
        
        if (hasLowercase(password)) {
            characterTypeCount++;
        }
        
        if (hasNumbers(password)) {
            characterTypeCount++;
        }
        
        if (hasSpecialCharacters(password)) {
            characterTypeCount++;
        }
        
        if (characterTypeCount < 3) {
            throw new PasswordException("Password must contain at least 3 of the following: uppercase letters, lowercase letters, numbers, and special characters (!@#$%^&*()_+-=[]{}|').");
        }
        
        // Check that password is not identical to username
        if (username != null && password.equalsIgnoreCase(username)) {
            throw new PasswordException("Password cannot be identical to username.");
        }
        
        // Check that password is not identical to email
        if (email != null && password.equalsIgnoreCase(email)) {
            throw new PasswordException("Password cannot be identical to email address.");
        }
        
        // Note: Password never expires - this is a policy note, no validation needed
    }
    
    private static boolean hasUppercase(String password) {
        return password.matches(".*[A-Z].*");
    }
    
    private static boolean hasLowercase(String password) {
        return password.matches(".*[a-z].*");
    }
    
    private static boolean hasNumbers(String password) {
        return password.matches(".*\\d.*");
    }
    
    private static boolean hasSpecialCharacters(String password) {
        return password.matches(".*[" + java.util.regex.Pattern.quote(SPECIAL_CHARACTERS) + "].*");
    }
}
