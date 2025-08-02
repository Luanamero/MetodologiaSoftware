package com.medapp;

import com.medapp.controllers.UserController;
import com.medapp.infra.RAMRepository;
import com.medapp.views.UserInterface;

public class Main {
    public static void main(String[] args) {
        UserController controller = new UserController(new RAMRepository());
        UserInterface ui = new UserInterface(controller);

        // Test valid users with email
        System.out.println("=== Testing Valid Users ===");
        try {
            System.out.println(ui.sendUserInfo("alice", "StrongPass123!", "alice@example.com"));
            System.out.println(ui.sendUserInfo("bob", "MySecure456@", "bob@example.com"));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Test invalid cases to demonstrate validation
        System.out.println("\n=== Testing Username Validation ===");
        try {
            System.out.println(ui.sendUserInfo("alice123", "StrongPass789!", "alice123@example.com"));
        } catch (Exception e) {
            System.out.println("Username with numbers error: " + e.getMessage());
        }

        try {
            System.out.println(ui.sendUserInfo("verylongusername", "StrongPass789!", "long@example.com"));
        } catch (Exception e) {
            System.out.println("Username too long error: " + e.getMessage());
        }

        try {
            System.out.println(ui.sendUserInfo("", "StrongPass789!", "empty@example.com"));
        } catch (Exception e) {
            System.out.println("Empty username error: " + e.getMessage());
        }

        System.out.println("\n=== Testing Password Validation ===");
        try {
            System.out.println(ui.sendUserInfo("carol", "weak", "carol@example.com"));
        } catch (Exception e) {
            System.out.println("Weak password error: " + e.getMessage());
        }

        try {
            System.out.println(ui.sendUserInfo("dave", "dave", "dave@example.com"));
        } catch (Exception e) {
            System.out.println("Password same as username error: " + e.getMessage());
        }

        try {
            System.out.println(ui.sendUserInfo("eve", "eve@example.com", "eve@example.com"));
        } catch (Exception e) {
            System.out.println("Password same as email error: " + e.getMessage());
        }

        // Show final user list
        System.out.println("\n=== Final User List ===");
        System.out.println(ui.showUserList());
    }
}
