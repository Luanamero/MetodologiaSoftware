package com.medapp.utils.storage;

public class FileStorageException extends StorageException {
    public FileStorageException(String operation, String path) {
        super("Failed to perform file operation: " + operation + " on path '" + path + "'.");
    }
    
    public FileStorageException(String operation, String path, Throwable cause) {
        super("Failed to perform file operation: " + operation + " on path '" + path + "'.", cause);
    }
}
