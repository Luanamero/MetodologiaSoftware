package com.medapp.controllers;

import com.medapp.models.User;
import com.medapp.models.Sala;
import com.medapp.models.Agendamento;
import com.medapp.infra.Repository;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class FacadeSingleton {
    private static volatile FacadeSingleton instance;
    private static volatile String repositoryType;
    private final UsuarioGerenciador usuarioGerenciador;
    private final SalaGerenciador salaGerenciador;
    private final RelatorioGerenciador relatorioGerenciador;
    private final AgendamentoGerenciador agendamentoGerenciador;

    private FacadeSingleton(Repository repository) {
        this.usuarioGerenciador = new UsuarioGerenciador(repository);
        this.salaGerenciador = new SalaGerenciador(repository);
        this.relatorioGerenciador = new RelatorioGerenciador(repository);
        this.agendamentoGerenciador = AgendamentoGerenciador.getInstance(repository);
    }

    public static FacadeSingleton getInstance(Repository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository cannot be null");
        }
        
        String currentRepositoryType = repository.getClass().getSimpleName();
        
        if (instance == null) {
            synchronized (FacadeSingleton.class) {
                if (instance == null) {
                    instance = new FacadeSingleton(repository);
                    repositoryType = currentRepositoryType;
                }
            }
        } else {
            // Verifica se o tipo de repositório é consistente
            if (!currentRepositoryType.equals(repositoryType)) {
                throw new IllegalStateException(
                    String.format("FacadeSingleton already initialized with %s repository. " +
                                "Cannot reinitialize with %s repository. " +
                                "Use reset() method if you need to change repository type.",
                                repositoryType, currentRepositoryType)
                );
            }
        }
        return instance;
    }
    
    /**
     * Reset the singleton instance (mainly for testing purposes)
     * WARNING: This should only be used in controlled environments like tests
     */
    public static synchronized void reset() {
        instance = null;
        repositoryType = null;
        AgendamentoGerenciador.reset();
    }
    
    /**
     * Get the current repository type (for debugging/monitoring purposes)
     */
    public static String getCurrentRepositoryType() {
        return repositoryType;
    }

    // Métodos da interface especificada
    public String agendar(String salaId, LocalDateTime dataHora) {
        return salaGerenciador.agendarSala(salaId, dataHora);
    }

    public String mostrarListaUsuario(User usuario) {
        if (usuario == null) {
            return usuarioGerenciador.listarUsuariosFormatado();
        }
        return formatarInformacoesUsuario(usuario);
    }

    public User enviarInfoUsuario(String username) {
        return usuarioGerenciador.buscarUsuario(username);
    }

    // Delegação para UsuarioGerenciador
    public String criarAdministrador(String username, String password, String email, String nivelPermissao) {
        return usuarioGerenciador.criarAdministrador(username, password, email, nivelPermissao);
    }

    public String criarPaciente(String username, String password, String email, String cpf, 
                              LocalDate dataNascimento, String telefone, String endereco) {
        return usuarioGerenciador.criarPaciente(username, password, email, cpf, dataNascimento, telefone, endereco);
    }

    public String criarProfissionalSaude(String username, String password, String email, 
                                       String crm, String especialidade, String departamento) {
        return usuarioGerenciador.criarProfissionalSaude(username, password, email, crm, especialidade, departamento);
    }

    public List<User> listarUsuarios() {
        return usuarioGerenciador.listarUsuarios();
    }

    public String editarUsuario(User usuario) {
        return usuarioGerenciador.editarUsuario(usuario);
    }

    public String removerUsuario(String username) {
        return usuarioGerenciador.removerUsuario(username);
    }

    public User buscarUsuario(String username) {
        return usuarioGerenciador.buscarUsuario(username);
    }

    // Delegação para SalaGerenciador
    public String criarSala(String id, String nome, int capacidade, String tipo) {
        return salaGerenciador.criarSala(id, nome, capacidade, tipo);
    }

    public List<Sala> listarSalas() {
        return salaGerenciador.listarSalas();
    }

    public List<Sala> listarSalasDisponiveis() {
        return salaGerenciador.listarSalasDisponiveis();
    }

    public String listarSalasFormatado() {
        return salaGerenciador.listarSalasFormatado();
    }

    public Sala buscarSala(String id) {
        return salaGerenciador.buscarSala(id);
    }

    public String liberarSala(String salaId) {
        return salaGerenciador.liberarSala(salaId);
    }

    public String adicionarEquipamento(String salaId, String equipamento) {
        return salaGerenciador.adicionarEquipamento(salaId, equipamento);
    }

    public String mostrarStatusSistema() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== STATUS DO SISTEMA ===\n\n");
        sb.append(usuarioGerenciador.listarUsuariosFormatado());
        sb.append("\n");
        sb.append(salaGerenciador.listarSalasFormatado());
        sb.append("\n");
        sb.append(agendamentoGerenciador.listarAgendamentosFormatado());
        return sb.toString();
    }

    // Delegação para RelatorioGerenciador
    public RelatorioGerenciador getRelatorioGerenciador() {
        return relatorioGerenciador;
    }

    // Delegação para AgendamentoGerenciador
    public String criarAgendamento(String pacienteUsername, String profissionalUsername, 
                                 String salaId, LocalDateTime dataHora, String tipoConsulta, String observacoes) {
        return agendamentoGerenciador.criarAgendamento(pacienteUsername, profissionalUsername, 
                                                     salaId, dataHora, tipoConsulta, observacoes);
    }

    public Optional<Agendamento> buscarAgendamento(String id) {
        return agendamentoGerenciador.buscarAgendamento(id);
    }

    public List<Agendamento> listarAgendamentosPorPaciente(String pacienteUsername) {
        return agendamentoGerenciador.listarAgendamentosPorPaciente(pacienteUsername);
    }

    public List<Agendamento> listarAgendamentosPorProfissional(String profissionalUsername) {
        return agendamentoGerenciador.listarAgendamentosPorProfissional(profissionalUsername);
    }

    public List<Agendamento> listarAgendamentosAtivos() {
        return agendamentoGerenciador.listarAgendamentosAtivos();
    }

    public String listarAgendamentosFormatado() {
        return agendamentoGerenciador.listarAgendamentosFormatado();
    }

    public String confirmarAgendamento(String id) {
        return agendamentoGerenciador.confirmarAgendamento(id);
    }

    public String cancelarAgendamento(String id) {
        return agendamentoGerenciador.cancelarAgendamento(id);
    }

    public String finalizarAgendamento(String id) {
        return agendamentoGerenciador.finalizarAgendamento(id);
    }

    public AgendamentoGerenciador getAgendamentoGerenciador() {
        return agendamentoGerenciador;
    }

    private String formatarInformacoesUsuario(User usuario) {
        StringBuilder sb = new StringBuilder();
        sb.append("Informações do usuário:\n");
        sb.append(String.format("Username: %s\n", usuario.getUsername()));
        sb.append(String.format("Email: %s\n", usuario.getEmail()));
        sb.append(String.format("Tipo: %s\n", usuario.getTipoUsuario()));
        return sb.toString();
    }
}
