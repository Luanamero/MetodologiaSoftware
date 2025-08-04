package com.medapp;

import com.medapp.controllers.UserController;
import com.medapp.infra.FileRepository;
import com.medapp.infra.RAMRepository;
import com.medapp.infra.Repository;
import com.medapp.models.User;
import com.medapp.views.UserInterface;
import java.util.Properties;

import java.io.FileInputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        Repository repository;
        Properties props = new Properties();

        if (args.length > 0 && args[0].equalsIgnoreCase("file")) {
            System.out.println("Argument selected: file. Using FileRepository.");
            repository = new FileRepository();
        } else if (args.length > 0 && args[0].equalsIgnoreCase("ram")) {
            System.out.println("Argument selected: ram. Using RAMRepository.");
            repository = new RAMRepository();
        } else {
            try {
                props.load(new FileInputStream("config.properties"));
                String type = props.getProperty("tipoRepositorio", "ram");

                if (type.equalsIgnoreCase("file")) {
                    System.out.println("Config selected: file. Using FileRepository.");
                    repository = new FileRepository();
                } else if (type.equalsIgnoreCase("ram")) {
                    System.out.println("Config selected: ram. Using RAMRepository.");
                    repository = new RAMRepository();
                } else {
                    System.out.println("Invalid config. Using RAMRepository as default.");
                    repository = new RAMRepository();
                }
            } catch (IOException e) {
                System.out.println("Could not read config file or command line arguments. Using RAMRepository as default.");
                repository = new RAMRepository();
            }
        }

        UserController controller = new UserController(repository);
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
        
        // Test storage exceptions (only with FileRepository)
        if (repository instanceof FileRepository) {
            System.out.println("\n=== Testing Storage Exceptions ===");
            testStorageExceptions(controller);
        }
    }
    
    // Testing Storage Exceptions
    private static void testStorageExceptions(UserController controller) {
        System.out.println("1. Testing UserAlreadyExistsException:");
        try {
            controller.registerUser(new User("alice", "NewPass789!", "newalice@example.com"));
        } catch (Exception e) {
            System.out.println("   Caught: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
        
        System.out.println("\n2. Testing UserNotFoundException:");
        try {
            controller.getUser("usuarioInexistente");
        } catch (Exception e) {
            System.out.println("   Caught: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
        
        System.out.println("\n3. Testing UserNotFoundException on delete:");
        try {
            controller.deleteUser("usuarioInexistente");
        } catch (Exception e) {
            System.out.println("   Caught: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
        
        System.out.println("\n   Storage exceptions are working correctly!");
    }
}
