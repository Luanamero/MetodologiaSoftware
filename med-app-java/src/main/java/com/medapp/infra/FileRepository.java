package com.medapp.infra;

import com.medapp.models.User;
import com.medapp.utils.storage.*;

import java.io.*;
import java.util.*;

public class FileRepository implements Repository {
    private static final String USER_FOLDER = "users";

    public FileRepository() {
        File folder = new File(USER_FOLDER);
        if (!folder.exists() && !folder.mkdirs()) {
            throw new StoragePermissionException("create directory", USER_FOLDER);
        }
    }

    @Override
    public void saveUser(User user) {
        File file = new File(USER_FOLDER, user.getUsername() + ".bin");

        if (file.exists()) {
            throw new UserAlreadyExistsException(user.getUsername());
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(user);
            System.out.println("Saved user to file: " + file.getName());

        } catch (FileNotFoundException e) {
            throw new StoragePermissionException("write file", file.getAbsolutePath());

        } catch (EOFException e) {
            throw new StorageCorruptedException(file.getName(), e);

        } catch (IOException e) {
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("no space left")) {
                throw new InsufficientStorageSpaceException("save user");
            }
            throw new FileStorageException("write", file.getAbsolutePath(), e);
        }
    }

    @Override
    public User loadUser(String username) {
        File file = new File(USER_FOLDER, username + ".bin");

        if (!file.exists()) {
            throw new UserNotFoundException(username);
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (User) ois.readObject();

        } catch (FileNotFoundException e) {
            throw new StoragePermissionException("read file", file.getAbsolutePath());

        } catch (EOFException e) {
            throw new StorageCorruptedException(file.getName(), e);

        } catch (ClassNotFoundException e) {
            throw new StorageCorruptedException("Class not found during deserialization of user: " + username, e);

        } catch (IOException e) {
            throw new FileStorageException("read", file.getAbsolutePath(), e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        File folder = new File(USER_FOLDER);

        if (!folder.exists()) {
            throw new StoragePermissionException("access directory", folder.getAbsolutePath());
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".bin"));

        if (files == null) {
            throw new StoragePermissionException("list files", folder.getAbsolutePath());
        }

        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                users.add((User) ois.readObject());
            } catch (EOFException e) {
                System.out.println("Warning: Skipping corrupted file (EOF): " + file.getName() + " -> " + e.getMessage());
            } catch (ClassNotFoundException e) {
                System.out.println("Warning: Class not found while reading: " + file.getName() + " -> " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Warning: I/O error while reading file: " + file.getName() + " -> " + e.getMessage());
            }
        }


        return users;
    }

    @Override
    public void deleteUser(String username) {
        File file = new File(USER_FOLDER, username + ".bin");

        if (!file.exists()) {
            throw new UserNotFoundException(username);
        }

        if (!file.delete()) {
            throw new StoragePermissionException("delete file", file.getAbsolutePath());
        }

        System.out.println("Deleted user file for: " + username);
    }

    public void updateUser(User user) {
        File file = new File(USER_FOLDER, user.getUsername() + ".bin");

        if (!file.exists()) {
            throw new UserNotFoundException(user.getUsername());
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(user);
            System.out.println("Updated user file: " + file.getName());

        } catch (FileNotFoundException e) {
            throw new StoragePermissionException("write file", file.getAbsolutePath());

        } catch (EOFException e) {
            throw new StorageCorruptedException(file.getName(), e);

        } catch (IOException e) {
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("no space left")) {
                throw new InsufficientStorageSpaceException("update user");
            }
            throw new FileStorageException("update", file.getAbsolutePath(), e);
        }
    }
}
