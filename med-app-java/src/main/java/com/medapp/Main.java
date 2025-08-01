package com.medapp;

import com.medapp.controllers.UserController;
import com.medapp.infra.RAMRepository;
import com.medapp.views.UserInterface;

public class Main {
    public static void main(String[] args) {
        UserController controller = new UserController(new RAMRepository());
        UserInterface ui = new UserInterface(controller);

        System.out.println(ui.sendUserInfo("alice", "1234"));
        System.out.println(ui.sendUserInfo("bob", "abcd"));
        System.out.println(ui.showUserList());
    }
}
