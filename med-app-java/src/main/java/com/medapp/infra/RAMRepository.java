package com.medapp.infra;

import com.medapp.models.User;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class RAMRepository implements Repository {
    private Map<String, User> users = new HashMap<>();

    @Override
    public void saveUser(User user) {
        users.put(user.getUsername(), user);
    }

    @Override
    public User loadUser(String username) {
        return users.get(username);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteUser(String username) {
        users.remove(username);
    }
}
