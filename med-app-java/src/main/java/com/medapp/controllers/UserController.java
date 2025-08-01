package com.medapp.controllers;

import com.medapp.models.User;
import com.medapp.use.UserValidator;
import com.medapp.infra.Repository;

import java.util.ArrayList;
import java.util.List;

public class UserController {
    private Repository userRepository;

    public UserController(Repository repository) {
        this.userRepository = repository;
    }

    public String registerUser(User user) {
        UserValidator.validate(user);
        userRepository.saveUser(user);
        return String.format("User '%s' successfully registered.", user.getUsername());
    }

    public String registerUserByCredentials(String username, String password) {
        User newUser = new User(username, password);
        return registerUser(newUser);
    }

    public String listUsers() {
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
    }

    public void editUser(User user) {
        userRepository.saveUser(user); 
    }

    public void deleteUser(String username) {
        userRepository.deleteUser(username);
    }
}
