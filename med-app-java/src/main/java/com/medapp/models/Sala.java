package com.medapp.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Sala implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String nome;
    private int capacidade;
    private String tipo; // CIRURGIA, CONSULTA, EMERGENCIA
    private boolean disponivel;
    private List<String> equipamentos;
    private LocalDateTime proximoAgendamento;

    public Sala() {
        this.equipamentos = new ArrayList<>();
        this.disponivel = true;
    }

    public Sala(String id, String nome, int capacidade, String tipo) {
        this();
        this.id = id;
        this.nome = nome;
        this.capacidade = capacidade;
        this.tipo = tipo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public List<String> getEquipamentos() {
        return equipamentos;
    }

    public void setEquipamentos(List<String> equipamentos) {
        this.equipamentos = equipamentos;
    }

    public void adicionarEquipamento(String equipamento) {
        this.equipamentos.add(equipamento);
    }

    public void removerEquipamento(String equipamento) {
        this.equipamentos.remove(equipamento);
    }

    public LocalDateTime getProximoAgendamento() {
        return proximoAgendamento;
    }

    public void setProximoAgendamento(LocalDateTime proximoAgendamento) {
        this.proximoAgendamento = proximoAgendamento;
    }
}
