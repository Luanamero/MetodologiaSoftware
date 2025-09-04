package com.medapp.views;

import com.medapp.controllers.FacadeSingleton;
import com.medapp.models.User;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserInterface {
    private FacadeSingleton facade;

    public UserInterface(FacadeSingleton facade) {
        this.facade = facade;
    }

    public String mostrarListaUsuarios() {
        return facade.mostrarListaUsuario(null);
    }

    public String mostrarInformacoesUsuario(String username) {
        User usuario = facade.enviarInfoUsuario(username);
        if (usuario != null) {
            return facade.mostrarListaUsuario(usuario);
        }
        return "Usuário não encontrado.";
    }

    public String criarAdministrador(String username, String password, String email, String nivelPermissao) {
        return facade.criarAdministrador(username, password, email, nivelPermissao);
    }

    public String criarPaciente(String username, String password, String email, String cpf, 
                              LocalDate dataNascimento, String telefone, String endereco) {
        return facade.criarPaciente(username, password, email, cpf, dataNascimento, telefone, endereco);
    }

    public String criarProfissionalSaude(String username, String password, String email, 
                                       String crm, String especialidade, String departamento) {
        return facade.criarProfissionalSaude(username, password, email, crm, especialidade, departamento);
    }

    public String agendarSala(String salaId, LocalDateTime dataHora) {
        return facade.agendar(salaId, dataHora);
    }

    public String mostrarSalas() {
        return facade.listarSalasFormatado();
    }

    public String mostrarStatusCompleto() {
        return facade.mostrarStatusSistema();
    }

    public String editarUsuario(User usuario) {
        return facade.editarUsuario(usuario);
    }

    public String removerUsuario(String username) {
        return facade.removerUsuario(username);
    }

    public User buscarUsuario(String username) {
        return facade.buscarUsuario(username);
    }
}
