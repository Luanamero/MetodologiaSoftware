package com.medapp.infra;

import com.medapp.models.User;
import com.medapp.models.Sala;
import java.util.List;

public interface Repository {
    // Operações com User (existentes)
    void saveUser(User user);
    User loadUser(String username);
    List<User> getAllUsers();
    void deleteUser(String username);
    
    // Operações com Sala (novas)
    void saveSala(Sala sala);
    Sala loadSala(String id);
    List<Sala> getAllSalas();
    void deleteSala(String id);
}
