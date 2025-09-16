package com.medapp.infra;

import com.medapp.models.User;
import com.medapp.models.Sala;
import com.medapp.models.Relatorio;
import com.medapp.utils.repository.*;
import com.medapp.utils.storage.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * FileRepository - Implementação de Repository para armazenamento em arquivos
 * 
 * Armazena dados em arquivos binários no diretório "users/"
 * Cada entidade é persistida em arquivos separados usando serialização Java
 */
public class FileRepository implements Repository {
    
    private static final String USERS_DIR = "users";
    private static final String SALAS_DIR = "salas";
    private static final String RELATORIOS_DIR = "relatorios";
    
    public FileRepository() {
        try {
            // Criar diretórios se não existem
            Files.createDirectories(Paths.get(USERS_DIR));
            Files.createDirectories(Paths.get(SALAS_DIR));
            Files.createDirectories(Paths.get(RELATORIOS_DIR));
        } catch (IOException e) {
            throw new RepositoryConfigurationException("directory.creation", 
                "Failed to create storage directories: " + e.getMessage());
        }
    }

    // ============= MÉTODOS PARA USER =============
    
    @Override
    public void saveUser(User user) {
        try {
            String filename = USERS_DIR + File.separator + user.getUsername() + ".bin";
            
            // Verificar se usuário já existe
            if (Files.exists(Paths.get(filename))) {
                throw new UserAlreadyExistsException(user.getUsername());
            }
            
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
                oos.writeObject(user);
                System.out.println("User saved to file: " + filename);
            }
            
        } catch (UserAlreadyExistsException e) {
            throw e;
        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {
                throw new RepositoryConfigurationException("storage.directory", 
                    "Storage directory not accessible: " + USERS_DIR);
            }
            throw new RepositoryException("Failed to save user: " + user.getUsername(), e);
        } catch (Exception e) {
            throw new RepositoryException("Unexpected error saving user: " + user.getUsername(), e);
        }
    }

    @Override
    public User loadUser(String username) {
        try {
            String filename = USERS_DIR + File.separator + username + ".bin";
            
            if (!Files.exists(Paths.get(filename))) {
                throw new UserNotFoundException(username);
            }
            
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
                return (User) ois.readObject();
            }
            
        } catch (UserNotFoundException e) {
            throw e;
        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {
                throw new UserNotFoundException(username);
            }
            throw new RepositoryException("Failed to load user: " + username, e);
        } catch (ClassNotFoundException e) {
            throw new RepositoryIntegrityException("user.data", 
                "User data corrupted for: " + username);
        } catch (Exception e) {
            throw new RepositoryException("Unexpected error loading user: " + username, e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        try {
            List<User> users = new ArrayList<>();
            File usersDir = new File(USERS_DIR);
            
            if (!usersDir.exists()) {
                return users;
            }
            
            File[] files = usersDir.listFiles((dir, name) -> name.endsWith(".bin"));
            if (files != null) {
                for (File file : files) {
                    try {
                        String username = file.getName().replace(".bin", "");
                        User user = loadUser(username);
                        users.add(user);
                    } catch (Exception e) {
                        System.err.println("Warning: Could not load user from file " + file.getName() + ": " + e.getMessage());
                    }
                }
            }
            
            return users;
            
        } catch (Exception e) {
            throw new RepositoryException("Failed to get all users", e);
        }
    }

    @Override
    public void deleteUser(String username) {
        try {
            String filename = USERS_DIR + File.separator + username + ".bin";
            Path path = Paths.get(filename);
            
            if (!Files.exists(path)) {
                throw new UserNotFoundException(username);
            }
            
            Files.delete(path);
            
        } catch (UserNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw new RepositoryException("Failed to delete user: " + username, e);
        } catch (Exception e) {
            throw new RepositoryException("Unexpected error deleting user: " + username, e);
        }
    }

    // ============= MÉTODOS PARA SALA =============
    
    @Override
    public void saveSala(Sala sala) {
        try {
            String filename = SALAS_DIR + File.separator + sala.getId() + ".bin";
            
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
                oos.writeObject(sala);
                System.out.println("Sala saved to file: " + filename);
            }
            
        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {
                throw new RepositoryConfigurationException("storage.directory", 
                    "Storage directory not accessible: " + SALAS_DIR);
            }
            throw new RepositoryException("Failed to save sala: " + sala.getId(), e);
        } catch (Exception e) {
            throw new RepositoryException("Unexpected error saving sala: " + sala.getId(), e);
        }
    }

    @Override
    public Sala loadSala(String id) {
        try {
            String filename = SALAS_DIR + File.separator + id + ".bin";
            
            if (!Files.exists(Paths.get(filename))) {
                throw new RepositoryException("Sala not found: " + id);
            }
            
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
                return (Sala) ois.readObject();
            }
            
        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {
                throw new RepositoryException("Sala not found: " + id);
            }
            throw new RepositoryException("Failed to load sala: " + id, e);
        } catch (ClassNotFoundException e) {
            throw new RepositoryIntegrityException("sala.data", 
                "Sala data corrupted for: " + id);
        } catch (Exception e) {
            throw new RepositoryException("Unexpected error loading sala: " + id, e);
        }
    }

    @Override
    public List<Sala> getAllSalas() {
        try {
            List<Sala> salas = new ArrayList<>();
            File salasDir = new File(SALAS_DIR);
            
            if (!salasDir.exists()) {
                return salas;
            }
            
            File[] files = salasDir.listFiles((dir, name) -> name.endsWith(".bin"));
            if (files != null) {
                for (File file : files) {
                    try {
                        String id = file.getName().replace(".bin", "");
                        Sala sala = loadSala(id);
                        salas.add(sala);
                    } catch (Exception e) {
                        System.err.println("Warning: Could not load sala from file " + file.getName() + ": " + e.getMessage());
                    }
                }
            }
            
            return salas;
            
        } catch (Exception e) {
            throw new RepositoryException("Failed to get all salas", e);
        }
    }

    @Override
    public void deleteSala(String id) {
        try {
            String filename = SALAS_DIR + File.separator + id + ".bin";
            Path path = Paths.get(filename);
            
            if (!Files.exists(path)) {
                throw new RepositoryException("Sala not found: " + id);
            }
            
            Files.delete(path);
            
        } catch (IOException e) {
            throw new RepositoryException("Failed to delete sala: " + id, e);
        } catch (Exception e) {
            throw new RepositoryException("Unexpected error deleting sala: " + id, e);
        }
    }

    // ============= MÉTODOS PARA RELATORIO =============
    
    @Override
    public void saveRelatorio(Relatorio relatorio) {
        try {
            String filename = RELATORIOS_DIR + File.separator + relatorio.getId() + ".bin";
            
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
                oos.writeObject(relatorio);
                System.out.println("Relatorio saved to file: " + filename);
            }
            
        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {
                throw new RepositoryConfigurationException("storage.directory", 
                    "Storage directory not accessible: " + RELATORIOS_DIR);
            }
            throw new RepositoryException("Failed to save relatorio: " + relatorio.getId(), e);
        } catch (Exception e) {
            throw new RepositoryException("Unexpected error saving relatorio: " + relatorio.getId(), e);
        }
    }

    @Override
    public Relatorio loadRelatorio(String id) {
        try {
            String filename = RELATORIOS_DIR + File.separator + id + ".bin";
            
            if (!Files.exists(Paths.get(filename))) {
                throw new RepositoryException("Relatorio not found: " + id);
            }
            
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
                return (Relatorio) ois.readObject();
            }
            
        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {
                throw new RepositoryException("Relatorio not found: " + id);
            }
            throw new RepositoryException("Failed to load relatorio: " + id, e);
        } catch (ClassNotFoundException e) {
            throw new RepositoryIntegrityException("relatorio.data", 
                "Relatorio data corrupted for: " + id);
        } catch (Exception e) {
            throw new RepositoryException("Unexpected error loading relatorio: " + id, e);
        }
    }

    @Override
    public List<Relatorio> getAllRelatorios() {
        try {
            List<Relatorio> relatorios = new ArrayList<>();
            File relatoriosDir = new File(RELATORIOS_DIR);
            
            if (!relatoriosDir.exists()) {
                return relatorios;
            }
            
            File[] files = relatoriosDir.listFiles((dir, name) -> name.endsWith(".bin"));
            if (files != null) {
                for (File file : files) {
                    try {
                        String id = file.getName().replace(".bin", "");
                        Relatorio relatorio = loadRelatorio(id);
                        relatorios.add(relatorio);
                    } catch (Exception e) {
                        System.err.println("Warning: Could not load relatorio from file " + file.getName() + ": " + e.getMessage());
                    }
                }
            }
            
            return relatorios;
            
        } catch (Exception e) {
            throw new RepositoryException("Failed to get all relatorios", e);
        }
    }

    @Override
    public void deleteRelatorio(String id) {
        try {
            String filename = RELATORIOS_DIR + File.separator + id + ".bin";
            Path path = Paths.get(filename);
            
            if (!Files.exists(path)) {
                throw new RepositoryException("Relatorio not found: " + id);
            }
            
            Files.delete(path);
            
        } catch (IOException e) {
            throw new RepositoryException("Failed to delete relatorio: " + id, e);
        } catch (Exception e) {
            throw new RepositoryException("Unexpected error deleting relatorio: " + id, e);
        }
    }

    @Override
    public List<Relatorio> getRelatoriosByAutor(String autorUsername) {
        try {
            return getAllRelatorios().stream()
                    .filter(relatorio -> autorUsername.equals(relatorio.getAutorUsername()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RepositoryException("Failed to get relatorios by autor: " + autorUsername, e);
        }
    }
}
