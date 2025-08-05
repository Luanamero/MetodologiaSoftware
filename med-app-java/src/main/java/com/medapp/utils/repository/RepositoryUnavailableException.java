package com.medapp.utils.repository;

public class RepositoryUnavailableException extends RepositoryException {
    public RepositoryUnavailableException(String repositoryType) {
        super("Repository of type '" + repositoryType + "' is currently unavailable. Please try again later or contact administrator.");
    }
    
    public RepositoryUnavailableException(String repositoryType, String reason) {
        super("Repository of type '" + repositoryType + "' is currently unavailable: " + reason + ". Please try again later or contact administrator.");
    }
    
    public RepositoryUnavailableException(String repositoryType, Throwable cause) {
        super("Repository of type '" + repositoryType + "' is currently unavailable. Please try again later or contact administrator.", cause);
    }
}
