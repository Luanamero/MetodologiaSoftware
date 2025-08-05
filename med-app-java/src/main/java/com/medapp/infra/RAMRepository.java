package com.medapp.infra;

import com.medapp.models.User;
import com.medapp.utils.repository.*;
import com.medapp.utils.storage.UserAlreadyExistsException;
import com.medapp.utils.storage.UserNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class RAMRepository implements Repository {
    private Map<String, User> users = new HashMap<>();

    @Override
    public void saveUser(User user) {
        try {
            if (users.containsKey(user.getUsername())) {
                throw new UserAlreadyExistsException(user.getUsername());
            }
            users.put(user.getUsername(), user);
        } catch (OutOfMemoryError e) {
            throw new RepositoryException("Not enough memory to save user: " + user.getUsername(), e);
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
            User user = users.get(username);
            if (user == null) {
                throw new UserNotFoundException(username);
            }
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
            return new ArrayList<>(users.values());
        } catch (OutOfMemoryError e) {
            throw new RepositoryException("Not enough memory to load all users", e);
        } catch (Exception e) {
            throw new RepositoryException("Failed to load all users", e);
        }
    }

    @Override
    public void deleteUser(String username) {
        try {
            if (!users.containsKey(username)) {
                throw new UserNotFoundException(username);
            }
            users.remove(username);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw e;
            }
            throw new RepositoryException("Failed to delete user: " + username, e);
        }
    }
}
