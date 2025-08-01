package com.medapp.views;

import com.medapp.controllers.UserController;
import com.medapp.models.User;

public class UserInterface {
    private UserController controller;

    public UserInterface(UserController controller) {
        this.controller = controller;
    }

    public String showUserList() {
        return controller.listUsers();
    }

    public String sendUserInfo(String username, String password) {
        return controller.registerUserByCredentials(username, password);
    }
}
