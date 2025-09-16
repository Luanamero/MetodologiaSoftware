package com.medapp.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Relatorio implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String titulo;
    private String conteudo;
    private String tipoRelatorio; // PDF, HTML, etc.
    private String autorUsername;
    private LocalDateTime dataGeracao;
    private String status; // PENDENTE, PROCESSANDO, CONCLUIDO, ERRO
    private String descricao;

    public Relatorio() {
        // Construtor padrão para serialização
        this.dataGeracao = LocalDateTime.now();
        this.status = "PENDENTE";
    }

    public Relatorio(String id, String titulo, String conteudo, String tipoRelatorio, String autorUsername) {
        this.id = id;
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.tipoRelatorio = tipoRelatorio;
        this.autorUsername = autorUsername;
        this.dataGeracao = LocalDateTime.now();
        this.status = "PENDENTE";
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getTipoRelatorio() {
        return tipoRelatorio;
    }

    public void setTipoRelatorio(String tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
    }

    public String getAutorUsername() {
        return autorUsername;
    }

    public void setAutorUsername(String autorUsername) {
        this.autorUsername = autorUsername;
    }

    public LocalDateTime getDataGeracao() {
        return dataGeracao;
    }

    public void setDataGeracao(LocalDateTime dataGeracao) {
        this.dataGeracao = dataGeracao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return String.format("Relatorio{id='%s', titulo='%s', tipo='%s', autor='%s', status='%s', data=%s}", 
                            id, titulo, tipoRelatorio, autorUsername, status, dataGeracao);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Relatorio relatorio = (Relatorio) obj;
        return Objects.equals(id, relatorio.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
