package com.medapp.controllers;

import com.medapp.models.User;
import com.medapp.models.Administrador;
import com.medapp.models.Paciente;
import com.medapp.models.ProfissionalSaude;
import com.medapp.use.UsuarioValidador;
import com.medapp.infra.Repository;
import com.medapp.utils.user.*;
import com.medapp.utils.password.*;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

public class UsuarioGerenciador {
    private final Repository userRepository;
    private List<User> usuarios;

    public UsuarioGerenciador(Repository repository) {
        this.userRepository = repository;
        this.usuarios = new ArrayList<>();
        inicializarUsuarios();
    }

    private void inicializarUsuarios() {
        try {
            this.usuarios = userRepository.getAllUsers();
        } catch (Exception e) {
            this.usuarios = new ArrayList<>();
        }
    }

    public String criarAdministrador(String username, String password, String email, String nivelPermissao) {
        try {
            validarECriarUsuario(() -> {
                UsuarioValidador.validarCredenciais(username, password, email);
                Administrador admin = new Administrador(username, password, email, nivelPermissao);
                UsuarioValidador.validarUsuario(admin);
                return admin;
            });
            
            return formatarSucessoCreation("Administrador", username);
        } catch (Exception e) {
            return formatarErro("criar administrador", e);
        }
    }

    public String criarPaciente(String username, String password, String email, String cpf, 
                              LocalDate dataNascimento, String telefone, String endereco) {
        try {
            validarECriarUsuario(() -> {
                UsuarioValidador.validarCredenciais(username, password, email);
                Paciente paciente = new Paciente(username, password, email, cpf, dataNascimento, telefone, endereco);
                UsuarioValidador.validarUsuario(paciente);
                return paciente;
            });
            
            return formatarSucessoCreation("Paciente", username);
        } catch (Exception e) {
            return formatarErro("criar paciente", e);
        }
    }

    public String criarProfissionalSaude(String username, String password, String email, 
                                       String crm, String especialidade, String departamento) {
        try {
            validarECriarUsuario(() -> {
                UsuarioValidador.validarCredenciais(username, password, email);
                ProfissionalSaude profissional = new ProfissionalSaude(username, password, email, crm, especialidade, departamento);
                UsuarioValidador.validarUsuario(profissional);
                return profissional;
            });
            
            return formatarSucessoCreation("Profissional de Saúde", username);
        } catch (Exception e) {
            return formatarErro("criar profissional de saúde", e);
        }
    }

    public List<User> listarUsuarios() {
        inicializarUsuarios();
        return new ArrayList<>(usuarios);
    }

    public String listarUsuariosFormatado() {
        List<User> users = listarUsuarios();
        
        if (users.isEmpty()) {
            return "Nenhum usuário registrado.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Usuários registrados:\n");
        
        users.forEach(user -> 
            sb.append(String.format("- %s (%s)\n", user.getUsername(), user.getTipoUsuario()))
        );

        return sb.toString();
    }

    public User buscarUsuario(String username) {
        try {
            return userRepository.loadUser(username);
        } catch (Exception e) {
            return null;
        }
    }

    public String editarUsuario(User usuario) {
        try {
            UsuarioValidador.validarUsuario(usuario);
            userRepository.saveUser(usuario);
            inicializarUsuarios();
            return formatarSucessoOperation("editado", usuario.getUsername());
        } catch (Exception e) {
            return formatarErro("editar usuário", e);
        }
    }

    public String removerUsuario(String username) {
        try {
            userRepository.deleteUser(username);
            usuarios.removeIf(user -> user.getUsername().equals(username));
            return formatarSucessoOperation("removido", username);
        } catch (Exception e) {
            return formatarErro("remover usuário", e);
        }
    }

    // Métodos privados para reduzir duplicação de código
    private void validarECriarUsuario(UserCreator creator) throws Exception {
        User usuario = creator.create();
        userRepository.saveUser(usuario);
        
        // Remove usuário existente da lista local se houver, antes de adicionar o novo
        usuarios.removeIf(u -> u.getUsername().equals(usuario.getUsername()));
        usuarios.add(usuario);
    }

    private String formatarSucessoCreation(String tipo, String username) {
        return String.format("%s '%s' criado com sucesso.", tipo, username);
    }

    private String formatarSucessoOperation(String operacao, String username) {
        return String.format("Usuário '%s' %s com sucesso.", username, operacao);
    }

    private String formatarErro(String operacao, Exception e) {
        if (e instanceof UserException || e instanceof PasswordException) {
            return String.format("Erro de validação: %s", e.getMessage());
        }
        return String.format("Erro ao %s: %s", operacao, e.getMessage());
    }

    @FunctionalInterface
    private interface UserCreator {
        User create() throws Exception;
    }
}
