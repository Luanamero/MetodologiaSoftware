package com.medapp.infra;

import com.medapp.models.User;
import com.medapp.models.Sala;
import com.medapp.models.Relatorio;
import com.medapp.models.Agendamento;
import com.medapp.utils.storage.*;
import com.medapp.utils.repository.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class DBRepository implements Repository {
    private Map<String, User> simulatedDatabase = new HashMap<>();
    private Map<String, Sala> simulatedSalaDatabase = new HashMap<>();
    private Map<String, Relatorio> simulatedRelatorioDatabase = new HashMap<>();
    private Map<String, Agendamento> simulatedAgendamentoDatabase = new HashMap<>();
    private boolean isConfigured = true;
    private boolean isAvailable = true;
    
    @Override
    public void saveUser(User user) {
        try {
            if (!isConfigured) {
                throw new RepositoryConfigurationException("database.url", "Database URL not configured");
            }
            
            if (!isAvailable) {
                throw new RepositoryUnavailableException("database", "Database maintenance in progress");
            }
            
            if (Math.random() < 0.05) {
                throw new RepositoryTimeoutException("saveUser", 30);
            }
            
            if (simulatedDatabase.containsKey(user.getUsername())) {
                throw new UserAlreadyExistsException(user.getUsername());
            }
            
            simulatedDatabase.put(user.getUsername(), user);
            System.out.println("Simulating save to database for user: " + user.getUsername());
            
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
            if (!isConfigured) {
                throw new RepositoryConfigurationException("database.url", "Database URL not configured");
            }
            
            if (Math.random() < 0.03) {
                throw new RepositoryTimeoutException("loadUser", 30);
            }
            
            User user = simulatedDatabase.get(username);
            if (user == null) {
                throw new UserNotFoundException(username);
            }
            
            System.out.println("Simulating load from database for user: " + username);
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
            if (!isAvailable) {
                throw new RepositoryUnavailableException("database", "Database overloaded");
            }
            
            System.out.println("Simulating load all users from database");
            return new ArrayList<>(simulatedDatabase.values());
            
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw e;
            }
            throw new RepositoryException("Failed to load all users", e);
        }
    }

    @Override
    public void deleteUser(String username) {
        try {
            if ("admin".equals(username)) {
                throw new RepositoryIntegrityException("User", "cannot delete system admin", username);
            }
            
            if (!simulatedDatabase.containsKey(username)) {
                throw new UserNotFoundException(username);
            }
            
            simulatedDatabase.remove(username);
            System.out.println("Simulating delete from database for user: " + username);
            
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw e;
            }
            throw new RepositoryException("Failed to delete user: " + username, e);
        }
    }
    
    public void setConfigured(boolean configured) {
        this.isConfigured = configured;
    }
    
    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    // Implementação das operações com Sala
    @Override
    public void saveSala(Sala sala) {
        try {
            if (!isConfigured) {
                throw new RepositoryConfigurationException("database.url", "Database URL not configured");
            }
            
            if (!isAvailable) {
                throw new RepositoryUnavailableException("database", "Database maintenance in progress");
            }
            
            if (Math.random() < 0.05) {
                throw new RepositoryTimeoutException("saveSala", 30);
            }
            
            simulatedSalaDatabase.put(sala.getId(), sala);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Failed to save sala in database: " + sala.getId(), e);
        }
    }

    @Override
    public Sala loadSala(String id) {
        try {
            if (!isConfigured) {
                throw new RepositoryConfigurationException("database.url", "Database URL not configured");
            }
            
            if (!isAvailable) {
                throw new RepositoryUnavailableException("database", "Database maintenance in progress");
            }
            
            if (Math.random() < 0.05) {
                throw new RepositoryTimeoutException("loadSala", 30);
            }
            
            Sala sala = simulatedSalaDatabase.get(id);
            if (sala == null) {
                throw new RepositoryException("Sala not found in database: " + id);
            }
            return sala;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Failed to load sala from database: " + id, e);
        }
    }

    @Override
    public List<Sala> getAllSalas() {
        try {
            if (!isConfigured) {
                throw new RepositoryConfigurationException("database.url", "Database URL not configured");
            }
            
            if (!isAvailable) {
                throw new RepositoryUnavailableException("database", "Database maintenance in progress");
            }
            
            if (Math.random() < 0.05) {
                throw new RepositoryTimeoutException("getAllSalas", 30);
            }
            
            return new ArrayList<>(simulatedSalaDatabase.values());
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Failed to get all salas from database", e);
        }
    }

    @Override
    public void deleteSala(String id) {
        try {
            if (!isConfigured) {
                throw new RepositoryConfigurationException("database.url", "Database URL not configured");
            }
            
            if (!isAvailable) {
                throw new RepositoryUnavailableException("database", "Database maintenance in progress");
            }
            
            if (Math.random() < 0.05) {
                throw new RepositoryTimeoutException("deleteSala", 30);
            }
            
            if (simulatedSalaDatabase.remove(id) == null) {
                throw new RepositoryException("Sala not found in database: " + id);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Failed to delete sala from database: " + id, e);
        }
    }

    // Implementação das operações com Relatorio
    @Override
    public void saveRelatorio(Relatorio relatorio) {
        try {
            if (!isConfigured) {
                throw new RepositoryConfigurationException("database.url", "Database URL not configured");
            }
            
            if (!isAvailable) {
                throw new RepositoryUnavailableException("database", "Database maintenance in progress");
            }
            
            if (Math.random() < 0.05) {
                throw new RepositoryTimeoutException("saveRelatorio", 30);
            }
            
            simulatedRelatorioDatabase.put(relatorio.getId(), relatorio);
            System.out.println("Simulating save to database for relatorio: " + relatorio.getTitulo());
            
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Failed to save relatorio to database: " + relatorio.getId(), e);
        }
    }

    @Override
    public Relatorio loadRelatorio(String id) {
        try {
            if (!isConfigured) {
                throw new RepositoryConfigurationException("database.url", "Database URL not configured");
            }
            
            if (!isAvailable) {
                throw new RepositoryUnavailableException("database", "Database maintenance in progress");
            }
            
            if (Math.random() < 0.05) {
                throw new RepositoryTimeoutException("loadRelatorio", 30);
            }
            
            Relatorio relatorio = simulatedRelatorioDatabase.get(id);
            if (relatorio == null) {
                throw new RepositoryException("Relatorio not found in database: " + id);
            }
            
            return relatorio;
            
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Failed to load relatorio from database: " + id, e);
        }
    }

    @Override
    public List<Relatorio> getAllRelatorios() {
        try {
            if (!isConfigured) {
                throw new RepositoryConfigurationException("database.url", "Database URL not configured");
            }
            
            if (!isAvailable) {
                throw new RepositoryUnavailableException("database", "Database maintenance in progress");
            }
            
            if (Math.random() < 0.03) {
                throw new RepositoryTimeoutException("getAllRelatorios", 30);
            }
            
            return new ArrayList<>(simulatedRelatorioDatabase.values());
            
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Failed to get all relatorios from database", e);
        }
    }

    @Override
    public void deleteRelatorio(String id) {
        try {
            if (!isConfigured) {
                throw new RepositoryConfigurationException("database.url", "Database URL not configured");
            }
            
            if (!isAvailable) {
                throw new RepositoryUnavailableException("database", "Database maintenance in progress");
            }
            
            if (Math.random() < 0.05) {
                throw new RepositoryTimeoutException("deleteRelatorio", 30);
            }
            
            if (simulatedRelatorioDatabase.remove(id) == null) {
                throw new RepositoryException("Relatorio not found in database: " + id);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Failed to delete relatorio from database: " + id, e);
        }
    }

    @Override
    public List<Relatorio> getRelatoriosByAutor(String autorUsername) {
        try {
            if (!isConfigured) {
                throw new RepositoryConfigurationException("database.url", "Database URL not configured");
            }
            
            if (!isAvailable) {
                throw new RepositoryUnavailableException("database", "Database maintenance in progress");
            }
            
            if (Math.random() < 0.03) {
                throw new RepositoryTimeoutException("getRelatoriosByAutor", 30);
            }
            
            return simulatedRelatorioDatabase.values().stream()
                    .filter(relatorio -> autorUsername.equals(relatorio.getAutorUsername()))
                    .collect(Collectors.toList());
                    
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Failed to get relatorios by autor from database: " + autorUsername, e);
        }
    }

    // ============= MÉTODOS PARA AGENDAMENTO =============

    @Override
    public void saveAgendamento(Agendamento agendamento) {
        try {
            if (!isConfigured) {
                throw new RepositoryConfigurationException("database.url", "Database URL not configured");
            }
            
            if (!isAvailable) {
                throw new RepositoryUnavailableException("database", "Database maintenance in progress");
            }
            
            if (Math.random() < 0.05) {
                throw new RepositoryTimeoutException("saveAgendamento", 30);
            }
            
            simulatedAgendamentoDatabase.put(agendamento.getId(), agendamento);
            System.out.println("Simulating save agendamento to database: " + agendamento.getId());
            
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Failed to save agendamento to database: " + agendamento.getId(), e);
        }
    }

    @Override
    public Agendamento loadAgendamento(String id) {
        try {
            if (!isConfigured) {
                throw new RepositoryConfigurationException("database.url", "Database URL not configured");
            }
            
            if (!isAvailable) {
                throw new RepositoryUnavailableException("database", "Database maintenance in progress");
            }
            
            if (Math.random() < 0.03) {
                throw new RepositoryTimeoutException("loadAgendamento", 30);
            }
            
            Agendamento agendamento = simulatedAgendamentoDatabase.get(id);
            if (agendamento == null) {
                throw new RepositoryException("Agendamento not found in database: " + id);
            }
            
            return agendamento;
            
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Failed to load agendamento from database: " + id, e);
        }
    }

    @Override
    public List<Agendamento> getAllAgendamentos() {
        try {
            if (!isConfigured) {
                throw new RepositoryConfigurationException("database.url", "Database URL not configured");
            }
            
            if (!isAvailable) {
                throw new RepositoryUnavailableException("database", "Database maintenance in progress");
            }
            
            if (Math.random() < 0.03) {
                throw new RepositoryTimeoutException("getAllAgendamentos", 30);
            }
            
            return new ArrayList<>(simulatedAgendamentoDatabase.values());
            
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Failed to get all agendamentos from database", e);
        }
    }

    @Override
    public void deleteAgendamento(String id) {
        try {
            if (!isConfigured) {
                throw new RepositoryConfigurationException("database.url", "Database URL not configured");
            }
            
            if (!isAvailable) {
                throw new RepositoryUnavailableException("database", "Database maintenance in progress");
            }
            
            if (Math.random() < 0.03) {
                throw new RepositoryTimeoutException("deleteAgendamento", 30);
            }
            
            if (simulatedAgendamentoDatabase.remove(id) == null) {
                throw new RepositoryException("Agendamento not found in database: " + id);
            }
            
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Failed to delete agendamento from database: " + id, e);
        }
    }

    @Override
    public List<Agendamento> getAgendamentosByPaciente(String pacienteUsername) {
        try {
            if (!isConfigured) {
                throw new RepositoryConfigurationException("database.url", "Database URL not configured");
            }
            
            if (!isAvailable) {
                throw new RepositoryUnavailableException("database", "Database maintenance in progress");
            }
            
            if (Math.random() < 0.03) {
                throw new RepositoryTimeoutException("getAgendamentosByPaciente", 30);
            }
            
            return simulatedAgendamentoDatabase.values().stream()
                    .filter(agendamento -> pacienteUsername.equals(agendamento.getPacienteUsername()))
                    .collect(Collectors.toList());
                    
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Failed to get agendamentos by paciente from database: " + pacienteUsername, e);
        }
    }

    @Override
    public List<Agendamento> getAgendamentosByProfissional(String profissionalUsername) {
        try {
            if (!isConfigured) {
                throw new RepositoryConfigurationException("database.url", "Database URL not configured");
            }
            
            if (!isAvailable) {
                throw new RepositoryUnavailableException("database", "Database maintenance in progress");
            }
            
            if (Math.random() < 0.03) {
                throw new RepositoryTimeoutException("getAgendamentosByProfissional", 30);
            }
            
            return simulatedAgendamentoDatabase.values().stream()
                    .filter(agendamento -> profissionalUsername.equals(agendamento.getProfissionalUsername()))
                    .collect(Collectors.toList());
                    
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Failed to get agendamentos by profissional from database: " + profissionalUsername, e);
        }
    }

    @Override
    public List<Agendamento> getAgendamentosBySala(String salaId) {
        try {
            if (!isConfigured) {
                throw new RepositoryConfigurationException("database.url", "Database URL not configured");
            }
            
            if (!isAvailable) {
                throw new RepositoryUnavailableException("database", "Database maintenance in progress");
            }
            
            if (Math.random() < 0.03) {
                throw new RepositoryTimeoutException("getAgendamentosBySala", 30);
            }
            
            return simulatedAgendamentoDatabase.values().stream()
                    .filter(agendamento -> salaId.equals(agendamento.getSalaId()))
                    .collect(Collectors.toList());
                    
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("Failed to get agendamentos by sala from database: " + salaId, e);
        }
    }
}

