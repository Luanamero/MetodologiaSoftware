package com.medapp.utils.storage;

public class StoragePermissionException extends StorageException {
    public StoragePermissionException(String operation) {
        super("Permission denied for storage operation: " + operation + ". Please check file system permissions.");
    }
    
    public StoragePermissionException(String operation, String path) {
        super("Permission denied for storage operation: " + operation + " on path '" + path + "'. Please check file system permissions.");
    }
}
