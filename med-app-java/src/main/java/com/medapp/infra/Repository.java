package com.medapp.infra;

import com.medapp.models.User;
import com.medapp.models.Sala;
import com.medapp.models.Relatorio;
import java.util.List;

/**
 * Interface Repository - Define o contrato para persistência de dados
 * 
 * Esta interface segue o padrão Repository e é implementada por:
 * - RAMRepository: Armazenamento em memória
 * - DBRepository: Armazenamento em banco de dados simulado
 * - FileRepository: Armazenamento em arquivos
 */
public interface Repository {
    
    // Métodos para User
    void saveUser(User user);
    User loadUser(String username);
    List<User> getAllUsers();
    void deleteUser(String username);
    
    // Métodos para Sala
    void saveSala(Sala sala);
    Sala loadSala(String id);
    List<Sala> getAllSalas();
    void deleteSala(String id);
    
    // Métodos para Relatorio
    void saveRelatorio(Relatorio relatorio);
    Relatorio loadRelatorio(String id);
    List<Relatorio> getAllRelatorios();
    void deleteRelatorio(String id);
    List<Relatorio> getRelatoriosByAutor(String autorUsername);
}
