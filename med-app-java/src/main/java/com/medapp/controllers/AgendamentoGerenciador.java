package com.medapp.controllers;

import com.medapp.models.Agendamento;
import com.medapp.models.Agendamento.StatusAgendamento;
import com.medapp.models.User;
import com.medapp.models.Sala;
import com.medapp.infra.Repository;
import com.medapp.utils.repository.RepositoryException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


public class AgendamentoGerenciador {
    private static volatile AgendamentoGerenciador instance;
    private final Repository repository;
    private final List<Agendamento> agendamentos;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private AgendamentoGerenciador(Repository repository) {
        this.repository = repository;
        this.agendamentos = new ArrayList<>();
        inicializarAgendamentos();
    }

    /**
     * Implementação thread-safe do Singleton
     */
    public static AgendamentoGerenciador getInstance(Repository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository não pode ser nulo");
        }
        
        if (instance == null) {
            synchronized (AgendamentoGerenciador.class) {
                if (instance == null) {
                    instance = new AgendamentoGerenciador(repository);
                }
            }
        }
        return instance;
    }

    public static synchronized void reset() {
        instance = null;
    }

    private void inicializarAgendamentos() {
        try {
            List<Agendamento> agendamentosRepositorio = repository.getAllAgendamentos();
            agendamentos.clear();
            agendamentos.addAll(agendamentosRepositorio);
        } catch (Exception e) {
            // Log do erro e inicialização com lista vazia
            agendamentos.clear();
        }
    }

    
    public String criarAgendamento(String pacienteUsername, String profissionalUsername, 
                                 String salaId, LocalDateTime dataHora, String tipoConsulta, String observacoes) {
        try {
            // Validações básicas
            validarParametrosAgendamento(pacienteUsername, profissionalUsername, salaId, dataHora, tipoConsulta);
            
            // Verificar conflitos
            if (temConflitoHorario(salaId, dataHora)) {
                return formatarErro("Já existe agendamento para esta sala no horário solicitado");
            }
            
            if (temConflitoMedico(profissionalUsername, dataHora)) {
                return formatarErro("Profissional já possui agendamento neste horário");
            }

            // Criar agendamento
            String id = gerarId();
            Agendamento agendamento = new Agendamento(id, pacienteUsername, profissionalUsername, 
                                                    salaId, dataHora, tipoConsulta, observacoes);
            
            // Persistir
            agendamentos.add(agendamento);
            repository.saveAgendamento(agendamento);
            
            return formatarSucesso("Agendamento criado", agendamento.getId(), dataHora);
            
        } catch (Exception e) {
            return formatarErroOperacao("criar agendamento", e);
        }
    }

    /**
     * Busca agendamento por ID
     */
    public Optional<Agendamento> buscarAgendamento(String id) {
        try {
            return agendamentos.stream()
                    .filter(agendamento -> agendamento.getId().equals(id))
                    .findFirst();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Lista agendamentos por paciente
     */
    public List<Agendamento> listarAgendamentosPorPaciente(String pacienteUsername) {
        return agendamentos.stream()
                .filter(agendamento -> agendamento.getPacienteUsername().equals(pacienteUsername))
                .collect(Collectors.toList());
    }

    /**
     * Lista agendamentos por profissional
     */
    public List<Agendamento> listarAgendamentosPorProfissional(String profissionalUsername) {
        return agendamentos.stream()
                .filter(agendamento -> agendamento.getProfissionalUsername().equals(profissionalUsername))
                .collect(Collectors.toList());
    }

    /**
     * Lista agendamentos ativos (AGENDADO ou CONFIRMADO)
     */
    public List<Agendamento> listarAgendamentosAtivos() {
        return agendamentos.stream()
                .filter(Agendamento::isAtivo)
                .collect(Collectors.toList());
    }

    /**
     * Lista agendamentos formatado para exibição
     */
    public String listarAgendamentosFormatado() {
        if (agendamentos.isEmpty()) {
            return "Nenhum agendamento encontrado.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== AGENDAMENTOS ===\n\n");
        
        agendamentos.forEach(agendamento -> {
            sb.append(formatarAgendamento(agendamento));
            sb.append("\n");
        });

        return sb.toString();
    }

    /**
     * Confirma um agendamento
     */
    public String confirmarAgendamento(String id) {
        try {
            Optional<Agendamento> agendamentoOpt = buscarAgendamento(id);
            if (agendamentoOpt.isEmpty()) {
                return formatarErro("Agendamento não encontrado: " + id);
            }

            Agendamento agendamento = agendamentoOpt.get();
            agendamento.confirmar();
            repository.saveAgendamento(agendamento);
            
            return formatarSucesso("Agendamento confirmado", id, agendamento.getDataHora());
            
        } catch (IllegalStateException e) {
            return formatarErro(e.getMessage());
        } catch (Exception e) {
            return formatarErroOperacao("confirmar agendamento", e);
        }
    }

    /**
     * Cancela um agendamento
     */
    public String cancelarAgendamento(String id) {
        try {
            Optional<Agendamento> agendamentoOpt = buscarAgendamento(id);
            if (agendamentoOpt.isEmpty()) {
                return formatarErro("Agendamento não encontrado: " + id);
            }

            Agendamento agendamento = agendamentoOpt.get();
            agendamento.cancelar();
            repository.saveAgendamento(agendamento);
            
            return formatarSucesso("Agendamento cancelado", id, agendamento.getDataHora());
            
        } catch (IllegalStateException e) {
            return formatarErro(e.getMessage());
        } catch (Exception e) {
            return formatarErroOperacao("cancelar agendamento", e);
        }
    }

    /**
     * Finaliza um agendamento
     */
    public String finalizarAgendamento(String id) {
        try {
            Optional<Agendamento> agendamentoOpt = buscarAgendamento(id);
            if (agendamentoOpt.isEmpty()) {
                return formatarErro("Agendamento não encontrado: " + id);
            }

            Agendamento agendamento = agendamentoOpt.get();
            agendamento.finalizar();
            repository.saveAgendamento(agendamento);
            
            return formatarSucesso("Agendamento finalizado", id, agendamento.getDataHora());
            
        } catch (IllegalStateException e) {
            return formatarErro(e.getMessage());
        } catch (Exception e) {
            return formatarErroOperacao("finalizar agendamento", e);
        }
    }

    // Métodos privados - Keep It Simple

    private void validarParametrosAgendamento(String pacienteUsername, String profissionalUsername, 
                                            String salaId, LocalDateTime dataHora, String tipoConsulta) {
        if (pacienteUsername == null || pacienteUsername.trim().isEmpty()) {
            throw new IllegalArgumentException("Username do paciente é obrigatório");
        }
        if (profissionalUsername == null || profissionalUsername.trim().isEmpty()) {
            throw new IllegalArgumentException("Username do profissional é obrigatório");
        }
        if (salaId == null || salaId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID da sala é obrigatório");
        }
        if (dataHora == null) {
            throw new IllegalArgumentException("Data/hora é obrigatória");
        }
        if (dataHora.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Data/hora deve ser futura");
        }
        if (tipoConsulta == null || tipoConsulta.trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo da consulta é obrigatório");
        }
    }

    private boolean temConflitoHorario(String salaId, LocalDateTime dataHora) {
        return agendamentos.stream()
                .filter(Agendamento::isAtivo)
                .anyMatch(agendamento -> 
                    agendamento.getSalaId().equals(salaId) && 
                    agendamento.getDataHora().equals(dataHora)
                );
    }

    private boolean temConflitoMedico(String profissionalUsername, LocalDateTime dataHora) {
        return agendamentos.stream()
                .filter(Agendamento::isAtivo)
                .anyMatch(agendamento -> 
                    agendamento.getProfissionalUsername().equals(profissionalUsername) && 
                    agendamento.getDataHora().equals(dataHora)
                );
    }

    private String gerarId() {
        return "AGD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String formatarAgendamento(Agendamento agendamento) {
        return String.format("ID: %s | Paciente: %s | Médico: %s | Sala: %s | Data: %s | Tipo: %s | Status: %s",
                agendamento.getId(),
                agendamento.getPacienteUsername(),
                agendamento.getProfissionalUsername(),
                agendamento.getSalaId(),
                agendamento.getDataHora().format(formatter),
                agendamento.getTipoConsulta(),
                agendamento.getStatus());
    }

    private String formatarSucesso(String acao, String id, LocalDateTime dataHora) {
        return String.format("%s com sucesso. ID: %s, Data: %s", 
                acao, id, dataHora.format(formatter));
    }

    private String formatarErro(String mensagem) {
        return "Erro: " + mensagem;
    }

    private String formatarErroOperacao(String operacao, Exception e) {
        return String.format("Erro ao %s: %s", operacao, e.getMessage());
    }
}