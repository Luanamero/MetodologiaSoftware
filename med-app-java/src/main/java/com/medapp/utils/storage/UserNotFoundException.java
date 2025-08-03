package com.medapp.utils.storage;

public class UserNotFoundException extends StorageException {
    public UserNotFoundException(String username) {
        super("User '" + username + "' not found in the storage system.");
    }
}
