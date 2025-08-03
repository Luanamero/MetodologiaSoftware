package com.medapp.utils.storage;

public class StorageCorruptedException extends StorageException {
    public StorageCorruptedException(String filename) {
        super("Storage data is corrupted or in invalid format in file: " + filename + ". Data cannot be read properly.");
    }
    
    public StorageCorruptedException(String filename, Throwable cause) {
        super("Storage data is corrupted or in invalid format in file: " + filename + ". Data cannot be read properly.", cause);
    }
}
