package com.medapp.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Modelo que representa um agendamento no sistema médico.
 * 
 * Seguindo princípios SOLID:
 * - SRP: Responsabilidade única de representar dados de agendamento
 * - OCP: Extensível através de herança se necessário
 * - LSP: Substitui adequadamente Serializable
 */
public class Agendamento implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final String id;
    private final String pacienteUsername;
    private final String profissionalUsername;
    private final String salaId;
    private final LocalDateTime dataHora;
    private final String tipoConsulta;
    private final String observacoes;
    private StatusAgendamento status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    /**
     * Construtor para criar novo agendamento
     */
    public Agendamento(String id, String pacienteUsername, String profissionalUsername, 
                      String salaId, LocalDateTime dataHora, String tipoConsulta, String observacoes) {
        this.id = Objects.requireNonNull(id, "ID não pode ser nulo");
        this.pacienteUsername = Objects.requireNonNull(pacienteUsername, "Username do paciente não pode ser nulo");
        this.profissionalUsername = Objects.requireNonNull(profissionalUsername, "Username do profissional não pode ser nulo");
        this.salaId = Objects.requireNonNull(salaId, "ID da sala não pode ser nulo");
        this.dataHora = Objects.requireNonNull(dataHora, "Data/hora não pode ser nulo");
        this.tipoConsulta = Objects.requireNonNull(tipoConsulta, "Tipo da consulta não pode ser nulo");
        this.observacoes = observacoes != null ? observacoes : "";
        this.status = StatusAgendamento.AGENDADO;
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }

    // Getters
    public String getId() { return id; }
    public String getPacienteUsername() { return pacienteUsername; }
    public String getProfissionalUsername() { return profissionalUsername; }
    public String getSalaId() { return salaId; }
    public LocalDateTime getDataHora() { return dataHora; }
    public String getTipoConsulta() { return tipoConsulta; }
    public String getObservacoes() { return observacoes; }
    public StatusAgendamento getStatus() { return status; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }

    // Métodos de negócio - Keep It Simple
    public void confirmar() {
        if (this.status != StatusAgendamento.AGENDADO) {
            throw new IllegalStateException("Apenas agendamentos com status AGENDADO podem ser confirmados");
        }
        this.status = StatusAgendamento.CONFIRMADO;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public void cancelar() {
        if (this.status == StatusAgendamento.CANCELADO || this.status == StatusAgendamento.FINALIZADO) {
            throw new IllegalStateException("Agendamento não pode ser cancelado no status atual: " + this.status);
        }
        this.status = StatusAgendamento.CANCELADO;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public void finalizar() {
        if (this.status != StatusAgendamento.CONFIRMADO) {
            throw new IllegalStateException("Apenas agendamentos confirmados podem ser finalizados");
        }
        this.status = StatusAgendamento.FINALIZADO;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public boolean isAtivo() {
        return status == StatusAgendamento.AGENDADO || status == StatusAgendamento.CONFIRMADO;
    }

    public boolean isNaData(LocalDateTime data) {
        return dataHora.toLocalDate().equals(data.toLocalDate());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Agendamento that = (Agendamento) obj;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Agendamento{id='%s', paciente='%s', profissional='%s', sala='%s', data=%s, tipo='%s', status=%s}",
                id, pacienteUsername, profissionalUsername, salaId, dataHora, tipoConsulta, status);
    }

    /**
     * Enum para status do agendamento
     * Mantém a simplicidade e clareza
     */
    public enum StatusAgendamento {
        AGENDADO("Agendado"),
        CONFIRMADO("Confirmado"), 
        CANCELADO("Cancelado"),
        FINALIZADO("Finalizado");

        private final String descricao;

        StatusAgendamento(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }

        @Override
        public String toString() {
            return descricao;
        }
    }
}