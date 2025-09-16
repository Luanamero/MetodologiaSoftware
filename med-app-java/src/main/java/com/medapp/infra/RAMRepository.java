package com.medapp.infra;

import com.medapp.models.User;
import com.medapp.models.Sala;
import com.medapp.models.Relatorio;
import com.medapp.utils.repository.*;
import com.medapp.utils.storage.UserNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class RAMRepository implements Repository {
    private Map<String, User> users = new HashMap<>();
    private Map<String, Sala> salas = new HashMap<>();
    private Map<String, Relatorio> relatorios = new HashMap<>();

    @Override
    public void saveUser(User user) {
        try {
            // Permitir tanto criação quanto atualização de usuários
            users.put(user.getUsername(), user);
        } catch (OutOfMemoryError e) {
            throw new RepositoryException("Not enough memory to save user: " + user.getUsername(), e);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw e;
            }
            throw new RepositoryException("Failed to save user: " + user.getUsername(), e);
        }
    }

    @Override
    public User loadUser(String username) {
        try {
            User user = users.get(username);
            if (user == null) {
                throw new UserNotFoundException(username);
            }
            return user;
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw e;
            }
            throw new RepositoryException("Failed to load user: " + username, e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        try {
            return new ArrayList<>(users.values());
        } catch (OutOfMemoryError e) {
            throw new RepositoryException("Not enough memory to load all users", e);
        } catch (Exception e) {
            throw new RepositoryException("Failed to load all users", e);
        }
    }

    @Override
    public void deleteUser(String username) {
        try {
            if (!users.containsKey(username)) {
                throw new UserNotFoundException(username);
            }
            users.remove(username);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw e;
            }
            throw new RepositoryException("Failed to delete user: " + username, e);
        }
    }

    // Implementação das operações com Sala
    @Override
    public void saveSala(Sala sala) {
        try {
            if (salas.containsKey(sala.getId())) {
                // Atualiza sala existente
                salas.put(sala.getId(), sala);
            } else {
                // Cria nova sala
                salas.put(sala.getId(), sala);
            }
        } catch (OutOfMemoryError e) {
            throw new RepositoryException("Not enough memory to save sala: " + sala.getId(), e);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw e;
            }
            throw new RepositoryException("Failed to save sala: " + sala.getId(), e);
        }
    }

    @Override
    public Sala loadSala(String id) {
        try {
            Sala sala = salas.get(id);
            if (sala == null) {
                throw new RepositoryException("Sala not found: " + id);
            }
            return sala;
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw e;
            }
            throw new RepositoryException("Failed to load sala: " + id, e);
        }
    }

    @Override
    public List<Sala> getAllSalas() {
        try {
            return new ArrayList<>(salas.values());
        } catch (Exception e) {
            throw new RepositoryException("Failed to get all salas", e);
        }
    }

    @Override
    public void deleteSala(String id) {
        try {
            if (salas.remove(id) == null) {
                throw new RepositoryException("Sala not found: " + id);
            }
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw e;
            }
            throw new RepositoryException("Failed to delete sala: " + id, e);
        }
    }

    // Implementação das operações com Relatorio
    @Override
    public void saveRelatorio(Relatorio relatorio) {
        try {
            if (relatorio == null || relatorio.getId() == null) {
                throw new IllegalArgumentException("Relatório ou ID não pode ser nulo");
            }
            relatorios.put(relatorio.getId(), relatorio);
        } catch (OutOfMemoryError e) {
            throw new RepositoryException("Not enough memory to save relatorio: " + relatorio.getId(), e);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw e;
            }
            throw new RepositoryException("Failed to save relatorio: " + relatorio.getId(), e);
        }
    }

    @Override
    public Relatorio loadRelatorio(String id) {
        try {
            Relatorio relatorio = relatorios.get(id);
            if (relatorio == null) {
                throw new RepositoryException("Relatorio not found: " + id);
            }
            return relatorio;
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw e;
            }
            throw new RepositoryException("Failed to load relatorio: " + id, e);
        }
    }

    @Override
    public List<Relatorio> getAllRelatorios() {
        try {
            return new ArrayList<>(relatorios.values());
        } catch (Exception e) {
            throw new RepositoryException("Failed to get all relatorios", e);
        }
    }

    @Override
    public void deleteRelatorio(String id) {
        try {
            if (relatorios.remove(id) == null) {
                throw new RepositoryException("Relatorio not found: " + id);
            }
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw e;
            }
            throw new RepositoryException("Failed to delete relatorio: " + id, e);
        }
    }

    @Override
    public List<Relatorio> getRelatoriosByAutor(String autorUsername) {
        try {
            return relatorios.values().stream()
                    .filter(relatorio -> autorUsername.equals(relatorio.getAutorUsername()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RepositoryException("Failed to get relatorios by autor: " + autorUsername, e);
        }
    }
}
