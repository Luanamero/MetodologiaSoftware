package com.medapp.infra;

import com.medapp.models.User;
import com.medapp.utils.storage.*;
import com.medapp.utils.repository.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DBRepository implements Repository {
    private Map<String, User> simulatedDatabase = new HashMap<>();
    private boolean isConfigured = true;
    private boolean isAvailable = true;
    
    @Override
    public void saveUser(User user) {
        try {
            if (!isConfigured) {
                throw new RepositoryConfigurationException("database.url", "Database URL not configured");
            }
            
            if (!isAvailable) {
                throw new RepositoryUnavailableException("database", "Database maintenance in progress");
            }
            
            if (Math.random() < 0.05) {
                throw new RepositoryTimeoutException("saveUser", 30);
            }
            
            if (simulatedDatabase.containsKey(user.getUsername())) {
                throw new UserAlreadyExistsException(user.getUsername());
            }
            
            simulatedDatabase.put(user.getUsername(), user);
            System.out.println("Simulating save to database for user: " + user.getUsername());
            
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw e;
            }
            throw new RepositoryException("Failed to save user: " + user.getUsername(), e);
        }
    }

    @Override
    public User loadUser(String username) {
        try {
            if (!isConfigured) {
                throw new RepositoryConfigurationException("database.url", "Database URL not configured");
            }
            
            if (Math.random() < 0.03) {
                throw new RepositoryTimeoutException("loadUser", 30);
            }
            
            User user = simulatedDatabase.get(username);
            if (user == null) {
                throw new UserNotFoundException(username);
            }
            
            System.out.println("Simulating load from database for user: " + username);
            return user;
            
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw e;
            }
            throw new RepositoryException("Failed to load user: " + username, e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        try {
            if (!isAvailable) {
                throw new RepositoryUnavailableException("database", "Database overloaded");
            }
            
            System.out.println("Simulating load all users from database");
            return new ArrayList<>(simulatedDatabase.values());
            
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw e;
            }
            throw new RepositoryException("Failed to load all users", e);
        }
    }

    @Override
    public void deleteUser(String username) {
        try {
            if ("admin".equals(username)) {
                throw new RepositoryIntegrityException("User", "cannot delete system admin", username);
            }
            
            if (!simulatedDatabase.containsKey(username)) {
                throw new UserNotFoundException(username);
            }
            
            simulatedDatabase.remove(username);
            System.out.println("Simulating delete from database for user: " + username);
            
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw e;
            }
            throw new RepositoryException("Failed to delete user: " + username, e);
        }
    }
    
    public void setConfigured(boolean configured) {
        this.isConfigured = configured;
    }
    
    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }
}

