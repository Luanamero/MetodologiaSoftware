package com.medapp.infra;

import com.medapp.models.User;
import java.util.List;

public interface Repository {
    void saveUser(User user);
    User loadUser(String username);
    List<User> getAllUsers();
    void deleteUser(String username);
}
