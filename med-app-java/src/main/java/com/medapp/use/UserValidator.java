package com.medapp.use;

import com.medapp.models.User;
import com.medapp.utils.PasswordException;
import com.medapp.utils.UserException;

public class UserValidator {
    public static void validate(User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new UserException("Username cannot be empty.");
        }

        if (user.getPassword() == null || user.getPassword().length() < 4) {
            throw new PasswordException("Password is too weak.");
        }
    }
}
