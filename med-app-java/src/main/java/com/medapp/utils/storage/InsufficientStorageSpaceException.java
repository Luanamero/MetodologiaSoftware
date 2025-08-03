package com.medapp.utils.storage;

public class InsufficientStorageSpaceException extends StorageException {
    public InsufficientStorageSpaceException() {
        super("Insufficient storage space available. Cannot save data to storage system.");
    }
    
    public InsufficientStorageSpaceException(String operation) {
        super("Insufficient storage space available for operation: " + operation + ". Cannot save data to storage system.");
    }
}
