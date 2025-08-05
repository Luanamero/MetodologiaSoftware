package com.medapp.utils.repository;

public class RepositoryTimeoutException extends RepositoryException {
    public RepositoryTimeoutException(String operation) {
        super("Repository operation '" + operation + "' timed out. Please check network connection or increase timeout settings.");
    }
    
    public RepositoryTimeoutException(String operation, int timeoutSeconds) {
        super("Repository operation '" + operation + "' timed out after " + timeoutSeconds + " seconds. Please check network connection or increase timeout settings.");
    }
    
    public RepositoryTimeoutException(String operation, Throwable cause) {
        super("Repository operation '" + operation + "' timed out. Please check network connection or increase timeout settings.", cause);
    }
}
