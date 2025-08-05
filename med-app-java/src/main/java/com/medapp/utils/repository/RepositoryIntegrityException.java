package com.medapp.utils.repository;

public class RepositoryIntegrityException extends RepositoryException {
    public RepositoryIntegrityException(String entityType, String constraint) {
        super("Repository integrity constraint violation for " + entityType + ": " + constraint + ". Operation cannot be completed.");
    }
    
    public RepositoryIntegrityException(String entityType, String constraint, String value) {
        super("Repository integrity constraint violation for " + entityType + ": " + constraint + " with value '" + value + "'. Operation cannot be completed.");
    }
    
    public RepositoryIntegrityException(String message, Throwable cause) {
        super("Repository integrity constraint violation: " + message, cause);
    }
}
