package com.medapp.models;

import java.time.LocalDate;

public class Paciente extends User {
    private static final long serialVersionUID = 1L;
    private String cpf;
    private LocalDate dataNascimento;
    private String telefone;
    private String endereco;

    public Paciente() {
        super();
    }

    public Paciente(String username, String password, String email, String cpf, 
                   LocalDate dataNascimento, String telefone, String endereco) {
        super(username, password, email);
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    @Override
    public String getTipoUsuario() {
        return "Paciente";
    }
}
