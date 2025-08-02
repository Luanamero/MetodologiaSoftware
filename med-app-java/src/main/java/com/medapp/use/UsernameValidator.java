package com.medapp.use;

import com.medapp.utils.user.EmptyUsernameException;
import com.medapp.utils.user.UsernameContainsNumbersException;
import com.medapp.utils.user.UsernameTooLongException;

public class UsernameValidator {
    public static void validate(String username) {
        if (username == null || username.isEmpty()) {
            throw new EmptyUsernameException();
        }
        
        if (username.length() > 12) {
            throw new UsernameTooLongException();
        }
        
        if (username.matches(".*\\d.*")) {
            throw new UsernameContainsNumbersException();
        }
    }
}
