package com.medapp.infra;

import com.medapp.models.User;
import java.util.List;
import java.util.ArrayList;

public class FileRepository implements Repository {
    @Override
    public void saveUser(User user) {
        System.out.println("Simulating save to file for user: " + user.getUsername());
    }

    @Override
    public User loadUser(String username) {
        System.out.println("Simulating load from file for user: " + username);
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        System.out.println("Simulating load all users from file");
        return new ArrayList<>();
    }

    @Override
    public void deleteUser(String username) {
        System.out.println("Simulating delete from file for user: " + username);
    }
}
