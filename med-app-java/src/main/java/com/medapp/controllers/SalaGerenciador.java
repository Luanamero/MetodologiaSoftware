package com.medapp.controllers;

import com.medapp.models.Sala;
import com.medapp.infra.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class SalaGerenciador {
    private final Repository repository;
    private final List<Sala> salas;

    public SalaGerenciador(Repository repository) {
        this.repository = repository;
        this.salas = new ArrayList<>();
        inicializarSalas();
    }

    private void inicializarSalas() {
        try {
            // Tenta carregar salas do repositório
            List<Sala> salasRepositorio = repository.getAllSalas();
            if (!salasRepositorio.isEmpty()) {
                salas.clear();
                salas.addAll(salasRepositorio);
                return;
            }
        } catch (Exception e) {
            // Se falhar, cria salas padrão
        }
        
        if (salas.isEmpty()) {
            criarSalasPadrao();
        }
    }

    private void criarSalasPadrao() {
        try {
            Sala sala1 = criarSalaInterna("SALA001", "Sala de Cirurgia 1", 10, "CIRURGIA");
            Sala sala2 = criarSalaInterna("SALA002", "Sala de Consulta 1", 4, "CONSULTA");
            Sala sala3 = criarSalaInterna("SALA003", "Sala de Emergência", 8, "EMERGENCIA");
            
            repository.saveSala(sala1);
            repository.saveSala(sala2);
            repository.saveSala(sala3);
            
            salas.add(sala1);
            salas.add(sala2);
            salas.add(sala3);
        } catch (Exception e) {
            // Em caso de erro, mantém as salas apenas em memória
            salas.add(criarSalaInterna("SALA001", "Sala de Cirurgia 1", 10, "CIRURGIA"));
            salas.add(criarSalaInterna("SALA002", "Sala de Consulta 1", 4, "CONSULTA"));
            salas.add(criarSalaInterna("SALA003", "Sala de Emergência", 8, "EMERGENCIA"));
        }
    }

    private Sala criarSalaInterna(String id, String nome, int capacidade, String tipo) {
        return new Sala(id, nome, capacidade, tipo);
    }

    public String criarSala(String id, String nome, int capacidade, String tipo) {
        try {
            if (salaExiste(id)) {
                return formatarErroSala("Sala com ID '%s' já existe.", id);
            }

            Sala novaSala = criarSalaInterna(id, nome, capacidade, tipo);
            repository.saveSala(novaSala); // Persiste no repositório
            salas.add(novaSala);
            
            return formatarSucessoSala("Sala '%s' criada com sucesso.", nome);
        } catch (Exception e) {
            return formatarErroOperacao("criar sala", e);
        }
    }

    public List<Sala> listarSalas() {
        return new ArrayList<>(salas);
    }

    public List<Sala> listarSalasDisponiveis() {
        return salas.stream()
            .filter(Sala::isDisponivel)
            .collect(Collectors.toList());
    }

    public String listarSalasFormatado() {
        if (salas.isEmpty()) {
            return "Nenhuma sala registrada.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Salas registradas:\n");
        
        salas.forEach(sala -> {
            String status = sala.isDisponivel() ? "Disponível" : "Ocupada";
            sb.append(String.format("- %s (%s) - %s - Capacidade: %d - Status: %s\n", 
                     sala.getId(), sala.getNome(), sala.getTipo(), sala.getCapacidade(), status));
        });

        return sb.toString();
    }

    public Sala buscarSala(String id) {
        return findSalaById(id).orElse(null);
    }

    public String agendarSala(String salaId, LocalDateTime dataHora) {
        try {
            Optional<Sala> salaOpt = findSalaById(salaId);
            if (salaOpt.isEmpty()) {
                return formatarErroSala("Sala com ID '%s' não encontrada.", salaId);
            }

            Sala sala = salaOpt.get();
            if (!sala.isDisponivel()) {
                return formatarErroSala("Sala '%s' não está disponível.", sala.getNome());
            }

            agendarSalaInterna(sala, dataHora);
            repository.saveSala(sala); // Persiste as alterações
            return formatarSucessoSala("Sala '%s' agendada para %s.", sala.getNome(), dataHora);
        } catch (Exception e) {
            return formatarErroOperacao("agendar sala", e);
        }
    }

    public String liberarSala(String salaId) {
        try {
            Optional<Sala> salaOpt = findSalaById(salaId);
            if (salaOpt.isEmpty()) {
                return formatarErroSala("Sala com ID '%s' não encontrada.", salaId);
            }

            Sala sala = salaOpt.get();
            liberarSalaInterna(sala);
            repository.saveSala(sala); // Persiste as alterações
            
            return formatarSucessoSala("Sala '%s' liberada com sucesso.", sala.getNome());
        } catch (Exception e) {
            return formatarErroOperacao("liberar sala", e);
        }
    }

    public String adicionarEquipamento(String salaId, String equipamento) {
        try {
            Optional<Sala> salaOpt = findSalaById(salaId);
            if (salaOpt.isEmpty()) {
                return formatarErroSala("Sala com ID '%s' não encontrada.", salaId);
            }

            Sala sala = salaOpt.get();
            sala.adicionarEquipamento(equipamento);
            repository.saveSala(sala); // Persiste as alterações
            
            return formatarSucessoSala("Equipamento '%s' adicionado à sala '%s'.", 
                                     equipamento, sala.getNome());
        } catch (Exception e) {
            return formatarErroOperacao("adicionar equipamento", e);
        }
    }

    // Métodos privados para melhor organização
    private Optional<Sala> findSalaById(String id) {
        return salas.stream()
                   .filter(sala -> sala.getId().equals(id))
                   .findFirst();
    }

    private boolean salaExiste(String id) {
        return findSalaById(id).isPresent();
    }

    private void agendarSalaInterna(Sala sala, LocalDateTime dataHora) {
        sala.setProximoAgendamento(dataHora);
        sala.setDisponivel(false);
    }

    private void liberarSalaInterna(Sala sala) {
        sala.setDisponivel(true);
        sala.setProximoAgendamento(null);
    }

    private String formatarSucessoSala(String template, Object... args) {
        return String.format(template, args);
    }

    private String formatarErroSala(String template, Object... args) {
        return String.format(template, args);
    }

    private String formatarErroOperacao(String operacao, Exception e) {
        return String.format("Erro ao %s: %s", operacao, e.getMessage());
    }
}
