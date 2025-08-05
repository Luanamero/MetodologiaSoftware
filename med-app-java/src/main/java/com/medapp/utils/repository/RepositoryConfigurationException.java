package com.medapp.utils.repository;

public class RepositoryConfigurationException extends RepositoryException {
    public RepositoryConfigurationException(String configProperty) {
        super("Repository configuration error: Invalid or missing property '" + configProperty + "'. Please check configuration settings.");
    }
    
    public RepositoryConfigurationException(String configProperty, String reason) {
        super("Repository configuration error: Property '" + configProperty + "' is invalid: " + reason + ". Please check configuration settings.");
    }
    
    public RepositoryConfigurationException(String message, Throwable cause) {
        super("Repository configuration error: " + message, cause);
    }
}
