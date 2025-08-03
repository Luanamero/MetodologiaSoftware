package com.medapp.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medapp.models.User;
import com.medapp.utils.storage.*;

import java.io.*;
import java.util.*;

public class FileRepository implements Repository {
    private static final String USER_FOLDER = "users";
    private final ObjectMapper mapper = new ObjectMapper();

    public FileRepository() {
        File folder = new File(USER_FOLDER);
        if (!folder.exists() && !folder.mkdirs()) {
            throw new StoragePermissionException("create directory", USER_FOLDER);
        }
    }

    @Override
    public void saveUser(User user) {
        File file = new File(USER_FOLDER, user.getUsername() + ".json");
        
        if (file.exists()) {
            throw new UserAlreadyExistsException(user.getUsername());
        }
        
        try {
            mapper.writeValue(file, user);
            System.out.println("Saved user to file: " + file.getName());
        } catch (IOException e) {
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("no space left")) {
                throw new InsufficientStorageSpaceException("save user");
            }
            if (e instanceof FileNotFoundException || 
                (e.getMessage() != null && e.getMessage().toLowerCase().contains("permission denied"))) {
                throw new StoragePermissionException("write file", file.getAbsolutePath());
            }
            throw new FileStorageException("write", file.getAbsolutePath(), e);
        }
    }

    @Override
    public User loadUser(String username) {
        File file = new File(USER_FOLDER, username + ".json");
        if (!file.exists()) {
            throw new UserNotFoundException(username);
        }
        
        try {
            return mapper.readValue(file, User.class);
        } catch (IOException e) {
            if (e instanceof FileNotFoundException || 
                (e.getMessage() != null && e.getMessage().toLowerCase().contains("permission denied"))) {
                throw new StoragePermissionException("read file", file.getAbsolutePath());
            }
            throw new StorageCorruptedException(file.getName(), e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        File folder = new File(USER_FOLDER);
        
        if (!folder.exists()) {
            throw new StoragePermissionException("access directory", folder.getAbsolutePath());
        }
        
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));

        if (files != null) {
            for (File file : files) {
                try {
                    users.add(mapper.readValue(file, User.class));
                } catch (IOException e) {
                    System.out.println("Warning: Skipping corrupted file: " + file.getName() + " -> " + e.getMessage());
                }
            }
        } else {
            throw new StoragePermissionException("list files", folder.getAbsolutePath());
        }

        return users;
    }

    @Override
    public void deleteUser(String username) {
        File file = new File(USER_FOLDER, username + ".json");
        
        if (!file.exists()) {
            throw new UserNotFoundException(username);
        }
        
        if (!file.delete()) {
            throw new StoragePermissionException("delete file", file.getAbsolutePath());
        }
        
        System.out.println("Deleted user file for: " + username);
    }
    
    public void updateUser(User user) {
        File file = new File(USER_FOLDER, user.getUsername() + ".json");
        
        if (!file.exists()) {
            throw new UserNotFoundException(user.getUsername());
        }
        
        try {
            mapper.writeValue(file, user);
            System.out.println("Updated user file: " + file.getName());
        } catch (IOException e) {
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("no space left")) {
                throw new InsufficientStorageSpaceException("update user");
            }
            if (e instanceof FileNotFoundException || 
                (e.getMessage() != null && e.getMessage().toLowerCase().contains("permission denied"))) {
                throw new StoragePermissionException("write file", file.getAbsolutePath());
            }
            throw new FileStorageException("update", file.getAbsolutePath(), e);
        }
    }
}

