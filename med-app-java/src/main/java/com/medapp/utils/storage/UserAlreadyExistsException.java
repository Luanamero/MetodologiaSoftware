package com.medapp.utils.storage;

public class UserAlreadyExistsException extends StorageException {
    public UserAlreadyExistsException(String username) {
        super("User '" + username + "' already exists in the storage system.");
    }
}
