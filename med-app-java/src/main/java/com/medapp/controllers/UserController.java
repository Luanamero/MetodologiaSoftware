package com.medapp.controllers;

import com.medapp.models.User;
import com.medapp.use.UsernameValidator;
import com.medapp.use.PasswordValidator;
import com.medapp.infra.Repository;
import com.medapp.utils.storage.*;
import com.medapp.utils.repository.*;

import java.util.List;

public class UserController {
    private Repository userRepository;

    public UserController(Repository repository) {
        this.userRepository = repository;
    }

    public String registerUser(User user) {
        try {
            UsernameValidator.validate(user.getUsername());
            PasswordValidator.validate(user);
            userRepository.saveUser(user);
            return String.format("User '%s' successfully registered.", user.getUsername());
        } catch (UserAlreadyExistsException e) {
            return String.format("Registration failed: %s", e.getMessage());
        } catch (RepositoryConfigurationException e) {
            return String.format("Configuration error during registration: %s Please check system settings.", e.getMessage());
        } catch (RepositoryTimeoutException e) {
            return String.format("Timeout error during registration: %s Please try again.", e.getMessage());
        } catch (RepositoryUnavailableException e) {
            return String.format("Service unavailable during registration: %s Please try again later.", e.getMessage());
        } catch (RepositoryIntegrityException e) {
            return String.format("Data integrity error during registration: %s", e.getMessage());
        } catch (RepositoryException e) {
            return String.format("Repository error during registration: %s", e.getMessage());
        } catch (StorageException e) {
            return String.format("Storage error during registration: %s", e.getMessage());
        }
    }

    public String listUsers() {
        try {
            List<User> users = userRepository.getAllUsers();
            
            if (users.isEmpty()) {
                return "No users registered.";
            }

            StringBuilder names = new StringBuilder();
            for (int i = 0; i < users.size(); i++) {
                if (i > 0) names.append("\n");
                names.append(users.get(i).getUsername());
            }

            return "Registered users:\n" + names;
        } catch (RepositoryConfigurationException e) {
            return String.format("Configuration error while listing users: %s Please check system settings.", e.getMessage());
        } catch (RepositoryTimeoutException e) {
            return String.format("Timeout error while listing users: %s Please try again.", e.getMessage());
        } catch (RepositoryUnavailableException e) {
            return String.format("Service unavailable while listing users: %s Please try again later.", e.getMessage());
        } catch (RepositoryException e) {
            return String.format("Repository error while listing users: %s", e.getMessage());
        } catch (StorageException e) {
            return String.format("Storage error while listing users: %s", e.getMessage());
        }
    }

    public void editUser(User user) {
        try {
            userRepository.saveUser(user);
        } catch (RepositoryConfigurationException e) {
            System.err.println("Configuration error while editing user: " + e.getMessage());
            throw e; 
        } catch (RepositoryTimeoutException e) {
            System.err.println("Timeout error while editing user: " + e.getMessage());
            throw e; 
        } catch (RepositoryUnavailableException e) {
            System.err.println("Service unavailable while editing user: " + e.getMessage());
            throw e; 
        } catch (RepositoryIntegrityException e) {
            System.err.println("Integrity error while editing user: " + e.getMessage());
            throw e; 
        } catch (RepositoryException e) {
            System.err.println("Repository error while editing user: " + e.getMessage());
            throw e; 
        } catch (StorageException e) {
            System.err.println("Storage error while editing user: " + e.getMessage());
            throw e; 
        }
    }

    public void deleteUser(String username) {
        try {
            userRepository.deleteUser(username);
        } catch (RepositoryConfigurationException e) {
            System.err.println("Configuration error while deleting user: " + e.getMessage());
            throw e; 
        } catch (RepositoryIntegrityException e) {
            System.err.println("Integrity constraint error while deleting user: " + e.getMessage());
            throw e; 
        } catch (RepositoryTimeoutException e) {
            System.err.println("Timeout error while deleting user: " + e.getMessage());
            throw e; 
        } catch (RepositoryUnavailableException e) {
            System.err.println("Service unavailable while deleting user: " + e.getMessage());
            throw e; 
        } catch (RepositoryException e) {
            System.err.println("Repository error while deleting user: " + e.getMessage());
            throw e; 
        } catch (StorageException e) {
            System.err.println("Storage error while deleting user: " + e.getMessage());
            throw e; 
        }
    }
    
    /**
     * Busca um usuário específico por username
     */
    public User getUser(String username) {
        try {
            return userRepository.loadUser(username);
        } catch (RepositoryConfigurationException e) {
            System.err.println("Configuration error while loading user: " + e.getMessage());
            throw e; 
        } catch (RepositoryTimeoutException e) {
            System.err.println("Timeout error while loading user: " + e.getMessage());
            throw e; 
        } catch (RepositoryUnavailableException e) {
            System.err.println("Service unavailable while loading user: " + e.getMessage());
            throw e; 
        } catch (RepositoryException e) {
            System.err.println("Repository error while loading user: " + e.getMessage());
            throw e; 
        } catch (StorageException e) {
            System.err.println("Storage error while loading user: " + e.getMessage());
            throw e; 
        }
    }
}
