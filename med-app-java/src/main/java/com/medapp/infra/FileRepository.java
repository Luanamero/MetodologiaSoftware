package com.medapp.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medapp.models.User;

import java.io.*;
import java.util.*;

public class FileRepository implements Repository {
    private static final String USER_FOLDER = "users";
    private final ObjectMapper mapper = new ObjectMapper();

    public FileRepository() {
        File folder = new File(USER_FOLDER);
        if (!folder.exists() && !folder.mkdirs()) {
            System.out.println("Warning: Could not create user directory.");
        }
    }

    @Override
    public void saveUser(User user) {
        File file = new File(USER_FOLDER, user.getUsername() + ".json");
        try {
            mapper.writeValue(file, user);
            System.out.println("Saved user to file: " + file.getName());
        } catch (IOException e) {
            System.out.println("Error saving user: " + e.getMessage());
        }
    }

    @Override
    public User loadUser(String username) {
        File file = new File(USER_FOLDER, username + ".json");
        if (!file.exists()) {
            System.out.println("User file not found for: " + username);
            return null;
        }
        try {
            return mapper.readValue(file, User.class);
        } catch (IOException e) {
            System.out.println("Error loading user: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        File folder = new File(USER_FOLDER);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));

        if (files != null) {
            for (File file : files) {
                try {
                    users.add(mapper.readValue(file, User.class));
                } catch (IOException e) {
                    System.out.println("Error reading file: " + file.getName() + " -> " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        return users;
    }

    @Override
    public void deleteUser(String username) {
        File file = new File(USER_FOLDER, username + ".json");
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Deleted user file for: " + username);
            } else {
                System.out.println("Failed to delete user file for: " + username);
            }
        } else {
            System.out.println("User file not found for deletion: " + username);
        }
    }
}

