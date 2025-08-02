package com.medapp.views;

import com.medapp.controllers.UserController;

public class UserInterface {
    private UserController controller;

    public UserInterface(UserController controller) {
        this.controller = controller;
    }

    public String showUserList() {
        return controller.listUsers();
    }

    public String sendUserInfo(String username, String password, String email) {
        return controller.registerUserByCredentials(username, password, email);
    }

    public String sendUserInfo(String username, String password) {
        return controller.registerUserByCredentials(username, password);
    }
}
