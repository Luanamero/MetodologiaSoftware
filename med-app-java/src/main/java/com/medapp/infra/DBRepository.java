package com.medapp.infra;

import com.medapp.models.User;
import java.util.List;
import java.util.ArrayList;

public class DBRepository implements Repository {
    @Override
    public void saveUser(User user) {
        System.out.println("Simulating save to database for user: " + user.getUsername());
    }

    @Override
    public User loadUser(String username) {
        System.out.println("Simulating load from database for user: " + username);
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        System.out.println("Simulating load all users from database");
        return new ArrayList<>();
    }

    @Override
    public void deleteUser(String username) {
        System.out.println("Simulating delete from database for user: " + username);
    }
}
