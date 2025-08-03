package com.medapp.controllers;

import com.medapp.models.User;
import com.medapp.use.UsernameValidator;
import com.medapp.use.PasswordValidator;
import com.medapp.infra.Repository;
import com.medapp.utils.storage.*;

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
        } catch (StorageException e) {
            return String.format("Storage error during registration: %s", e.getMessage());
        }
    }

    public String registerUserByCredentials(String username, String password, String email) {
        User newUser = new User(username, password, email);
        return registerUser(newUser);
    }

    public String registerUserByCredentials(String username, String password) {
        // Default method for backward compatibility - requires email
        throw new IllegalArgumentException("Email is required for user registration. Use registerUserByCredentials(username, password, email) instead.");
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
        } catch (StorageException e) {
            return String.format("Storage error while listing users: %s", e.getMessage());
        }
    }

    public void editUser(User user) {
        try {
            userRepository.saveUser(user);
        } catch (StorageException e) {
            System.err.println("Storage error while editing user: " + e.getMessage());
            throw e; // Re-lançar para permitir tratamento específico pelo chamador
        }
    }

    public void deleteUser(String username) {
        try {
            userRepository.deleteUser(username);
        } catch (StorageException e) {
            System.err.println("Storage error while deleting user: " + e.getMessage());
            throw e; // Re-lançar para permitir tratamento específico pelo chamador
        }
    }
    
    /**
     * Busca um usuário específico por username
     */
    public User getUser(String username) {
        try {
            return userRepository.loadUser(username);
        } catch (StorageException e) {
            System.err.println("Storage error while loading user: " + e.getMessage());
            throw e; // Re-lançar para permitir tratamento específico pelo chamador
        }
    }
}
