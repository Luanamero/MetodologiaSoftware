
package com.medapp.views;

import com.medapp.controllers.FacadeSingleton;
import com.medapp.models.User;
import com.medapp.models.Administrador;
import com.medapp.models.ProfissionalSaude;
import com.medapp.models.Paciente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class UsuarioInterfaceGrafica extends JFrame {
    private final FacadeSingleton facade;
    private JTabbedPane tabbedPane;
    private JTable tabelaUsuarios;
    private DefaultTableModel modeloTabela;
    // Campos do formulário
    private JTextField campoUsername;
    private JPasswordField campoPassword;
    private JTextField campoEmail;
    private JComboBox<String> comboTipoUsuario;
    private JPanel painelCamposEspecificos;
    private JTextField campoNivelPermissao;
    private JTextField campoCrm;
    private JTextField campoEspecialidade;
    private JTextField campoDepartamento;
    private JTextField campoCpf;
    private JTextField campoDataNascimento;
    private JTextField campoTelefone;
    private JTextField campoEndereco;
    // Controle de edição
    private boolean modoEdicao = false;
    private User usuarioSelecionadoEdicao;

    public UsuarioInterfaceGrafica(FacadeSingleton facade) {
        this.facade = facade;
        inicializarInterface();
        configurarEventos();
        atualizarTabelaUsuarios();
    }

    private void inicializarInterface() {
        setTitle("MedApp - Gerenciamento de Usuários");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Usuários", criarPainelListagem());
        tabbedPane.addTab("Cadastro/Edição", criarPainelCriacao());
        add(tabbedPane);
    }

    private JPanel criarPainelListagem() {
        JPanel painel = new JPanel(new BorderLayout());
        String[] colunas = {"Username", "Email", "Tipo", "Detalhes"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaUsuarios = new JTable(modeloTabela);
        tabelaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tabelaUsuarios);
        painel.add(scrollPane, BorderLayout.CENTER);
        JPanel painelBotoes = new JPanel(new FlowLayout());
        JButton botaoAtualizar = new JButton("Atualizar");
        JButton botaoEditar = new JButton("Editar Selecionado");
        JButton botaoExcluir = new JButton("Excluir Selecionado");
        JButton botaoDetalhes = new JButton("Ver Detalhes");
        painelBotoes.add(botaoAtualizar);
        painelBotoes.add(botaoEditar);
        painelBotoes.add(botaoExcluir);
        painelBotoes.add(botaoDetalhes);
        painel.add(painelBotoes, BorderLayout.SOUTH);
        botaoAtualizar.addActionListener(e -> atualizarTabelaUsuarios());
        botaoEditar.addActionListener(e -> editarUsuarioSelecionado());
        botaoExcluir.addActionListener(e -> excluirUsuarioSelecionado());
        botaoDetalhes.addActionListener(e -> mostrarDetalhesUsuario());
        return painel;
    }

    private JPanel criarPainelCriacao() {
        JPanel painel = new JPanel(new BorderLayout());
        JPanel formulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0; gbc.gridy = 0;
        formulario.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        campoUsername = new JTextField(20);
        formulario.add(campoUsername, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        formulario.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        campoPassword = new JPasswordField(20);
        formulario.add(campoPassword, gbc);
        gbc.gridx = 0; gbc.gridy = 2;
        formulario.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        campoEmail = new JTextField(20);
        formulario.add(campoEmail, gbc);
        gbc.gridx = 0; gbc.gridy = 3;
        formulario.add(new JLabel("Tipo de Usuário:"), gbc);
        gbc.gridx = 1;
        comboTipoUsuario = new JComboBox<>(new String[]{"Administrador", "Profissional de Saúde", "Paciente"});
        formulario.add(comboTipoUsuario, gbc);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        painelCamposEspecificos = new JPanel(new CardLayout());
        formulario.add(painelCamposEspecificos, gbc);
        criarCamposEspecificos();
        painel.add(formulario, BorderLayout.CENTER);
        JPanel painelBotao = new JPanel(new FlowLayout());
        JButton botaoAcao = new JButton("Salvar");
        JButton botaoLimpar = new JButton("Limpar Campos");
        painelBotao.add(botaoAcao);
        painelBotao.add(botaoLimpar);
        painel.add(painelBotao, BorderLayout.SOUTH);
        botaoAcao.addActionListener(e -> {
            if (modoEdicao) {
                salvarEdicaoUsuario();
            } else {
                criarUsuario();
            }
        });
        botaoLimpar.addActionListener(e -> limparCampos());
        return painel;
    }

    private void criarCamposEspecificos() {
        JPanel painelAdmin = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0; gbc.gridy = 0;
        painelAdmin.add(new JLabel("Nível de Permissão:"), gbc);
        gbc.gridx = 1;
        campoNivelPermissao = new JTextField(20);
        painelAdmin.add(campoNivelPermissao, gbc);
        painelCamposEspecificos.add(painelAdmin, "Administrador");

        JPanel painelProf = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0; gbc.gridy = 0;
        painelProf.add(new JLabel("CRM:"), gbc);
        gbc.gridx = 1;
        campoCrm = new JTextField(20);
        painelProf.add(campoCrm, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        painelProf.add(new JLabel("Especialidade:"), gbc);
        gbc.gridx = 1;
        campoEspecialidade = new JTextField(20);
        painelProf.add(campoEspecialidade, gbc);
        gbc.gridx = 0; gbc.gridy = 2;
        painelProf.add(new JLabel("Departamento:"), gbc);
        gbc.gridx = 1;
        campoDepartamento = new JTextField(20);
        painelProf.add(campoDepartamento, gbc);
        painelCamposEspecificos.add(painelProf, "Profissional de Saúde");

        JPanel painelPaciente = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0; gbc.gridy = 0;
        painelPaciente.add(new JLabel("CPF:"), gbc);
        gbc.gridx = 1;
        campoCpf = new JTextField(20);
        painelPaciente.add(campoCpf, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        painelPaciente.add(new JLabel("Data de Nascimento (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        campoDataNascimento = new JTextField(20);
        painelPaciente.add(campoDataNascimento, gbc);
        gbc.gridx = 0; gbc.gridy = 2;
        painelPaciente.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1;
        campoTelefone = new JTextField(20);
        painelPaciente.add(campoTelefone, gbc);
        gbc.gridx = 0; gbc.gridy = 3;
        painelPaciente.add(new JLabel("Endereço:"), gbc);
        gbc.gridx = 1;
        campoEndereco = new JTextField(20);
        painelPaciente.add(campoEndereco, gbc);
        painelCamposEspecificos.add(painelPaciente, "Paciente");
    }

    private void configurarEventos() {
        comboTipoUsuario.addActionListener(e -> {
            String tipoSelecionado = (String) comboTipoUsuario.getSelectedItem();
            CardLayout cardLayout = (CardLayout) painelCamposEspecificos.getLayout();
            cardLayout.show(painelCamposEspecificos, tipoSelecionado);
        });
    }

    private void atualizarTabelaUsuarios() {
        try {
            modeloTabela.setRowCount(0);
            List<User> usuarios = facade.listarUsuarios();
            for (User usuario : usuarios) {
                String detalhes = obterDetalhesUsuario(usuario);
                Object[] linha = {
                    usuario.getUsername(),
                    usuario.getEmail(),
                    usuario.getTipoUsuario(),
                    detalhes
                };
                modeloTabela.addRow(linha);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar lista de usuários: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String obterDetalhesUsuario(User usuario) {
        if (usuario instanceof Administrador) {
            Administrador admin = (Administrador) usuario;
            return "Nível: " + admin.getNivelPermissao();
        } else if (usuario instanceof ProfissionalSaude) {
            ProfissionalSaude prof = (ProfissionalSaude) usuario;
            return "CRM: " + prof.getCrm() + ", " + prof.getEspecialidade();
        } else if (usuario instanceof Paciente) {
            Paciente paciente = (Paciente) usuario;
            return "CPF: " + paciente.getCpf();
        }
        return "";
    }

    private void preencherFormularioEdicao(User usuario) {
        campoUsername.setText(usuario.getUsername());
        campoUsername.setEnabled(false);
        campoPassword.setText("");
        campoEmail.setText(usuario.getEmail());
        comboTipoUsuario.setSelectedItem(usuario.getTipoUsuario());
        CardLayout cardLayout = (CardLayout) painelCamposEspecificos.getLayout();
        cardLayout.show(painelCamposEspecificos, usuario.getTipoUsuario());
        if (usuario instanceof Administrador) {
            campoNivelPermissao.setText(((Administrador) usuario).getNivelPermissao());
        } else if (usuario instanceof ProfissionalSaude) {
            campoCrm.setText(((ProfissionalSaude) usuario).getCrm());
            campoEspecialidade.setText(((ProfissionalSaude) usuario).getEspecialidade());
            campoDepartamento.setText(((ProfissionalSaude) usuario).getDepartamento());
        } else if (usuario instanceof Paciente) {
            campoCpf.setText(((Paciente) usuario).getCpf());
            campoDataNascimento.setText(((Paciente) usuario).getDataNascimento() != null ? ((Paciente) usuario).getDataNascimento().toString() : "");
            campoTelefone.setText(((Paciente) usuario).getTelefone());
            campoEndereco.setText(((Paciente) usuario).getEndereco());
        }
        modoEdicao = true;
        usuarioSelecionadoEdicao = usuario;
        tabbedPane.setTitleAt(1, "Editar Usuário");
        tabbedPane.setSelectedIndex(1);
    }

    private void editarUsuarioSelecionado() {
        int linhaSelecionada = tabelaUsuarios.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String username = (String) modeloTabela.getValueAt(linhaSelecionada, 0);
        User usuario = facade.buscarUsuario(username);
        if (usuario == null) {
            JOptionPane.showMessageDialog(this, "Usuário não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        preencherFormularioEdicao(usuario);
    }

    private void salvarEdicaoUsuario() {
        if (usuarioSelecionadoEdicao == null) {
            JOptionPane.showMessageDialog(this, "Nenhum usuário selecionado para edição.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            String novoEmail = campoEmail.getText().trim();
            String tipoUsuario = (String) comboTipoUsuario.getSelectedItem();
            if (novoEmail.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            usuarioSelecionadoEdicao.setEmail(novoEmail);
            if (usuarioSelecionadoEdicao instanceof Administrador && "Administrador".equals(tipoUsuario)) {
                String nivelPermissao = campoNivelPermissao.getText().trim();
                if (nivelPermissao.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nível de permissão é obrigatório para Administrador.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                ((Administrador) usuarioSelecionadoEdicao).setNivelPermissao(nivelPermissao);
            } else if (usuarioSelecionadoEdicao instanceof ProfissionalSaude && "Profissional de Saúde".equals(tipoUsuario)) {
                String crm = campoCrm.getText().trim();
                String especialidade = campoEspecialidade.getText().trim();
                String departamento = campoDepartamento.getText().trim();
                if (crm.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "CRM é obrigatório para Profissional de Saúde.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                ((ProfissionalSaude) usuarioSelecionadoEdicao).setCrm(crm);
                ((ProfissionalSaude) usuarioSelecionadoEdicao).setEspecialidade(especialidade);
                ((ProfissionalSaude) usuarioSelecionadoEdicao).setDepartamento(departamento);
            } else if (usuarioSelecionadoEdicao instanceof Paciente && "Paciente".equals(tipoUsuario)) {
                String cpf = campoCpf.getText().trim();
                String dataNascimentoStr = campoDataNascimento.getText().trim();
                String telefone = campoTelefone.getText().trim();
                String endereco = campoEndereco.getText().trim();
                if (cpf.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "CPF é obrigatório para Paciente.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                ((Paciente) usuarioSelecionadoEdicao).setCpf(cpf);
                LocalDate dataNascimento = null;
                if (!dataNascimentoStr.isEmpty()) {
                    try {
                        dataNascimento = LocalDate.parse(dataNascimentoStr);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Data de nascimento inválida. Use o formato YYYY-MM-DD.", "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                ((Paciente) usuarioSelecionadoEdicao).setDataNascimento(dataNascimento);
                ((Paciente) usuarioSelecionadoEdicao).setTelefone(telefone);
                ((Paciente) usuarioSelecionadoEdicao).setEndereco(endereco);
            }
            String resultado = facade.editarUsuario(usuarioSelecionadoEdicao);
            JOptionPane.showMessageDialog(this, resultado);
            if (resultado.contains("sucesso")) {
                atualizarTabelaUsuarios();
                limparCampos();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar usuário: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void criarUsuario() {
        try {
            String username = campoUsername.getText().trim();
            String password = new String(campoPassword.getPassword());
            String email = campoEmail.getText().trim();
            String tipoUsuario = (String) comboTipoUsuario.getSelectedItem();
            String resultado = "";
            switch (tipoUsuario) {
                case "Administrador":
                    String nivelPermissao = campoNivelPermissao.getText().trim();
                    resultado = facade.criarAdministrador(username, password, email, nivelPermissao);
                    break;
                case "Profissional de Saúde":
                    String crm = campoCrm.getText().trim();
                    String especialidade = campoEspecialidade.getText().trim();
                    String departamento = campoDepartamento.getText().trim();
                    resultado = facade.criarProfissionalSaude(username, password, email, crm, especialidade, departamento);
                    break;
                case "Paciente":
                    String cpf = campoCpf.getText().trim();
                    String dataNascStr = campoDataNascimento.getText().trim();
                    String telefone = campoTelefone.getText().trim();
                    String endereco = campoEndereco.getText().trim();
                    LocalDate dataNascimento = LocalDate.parse(dataNascStr);
                    resultado = facade.criarPaciente(username, password, email, cpf, dataNascimento, telefone, endereco);
                    break;
            }
            if (resultado.contains("sucesso")) {
                JOptionPane.showMessageDialog(this, resultado, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
                atualizarTabelaUsuarios();
            } else {
                JOptionPane.showMessageDialog(this, resultado, "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao criar usuário: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        campoUsername.setText("");
        campoUsername.setEnabled(true);
        campoPassword.setText("");
        campoEmail.setText("");
        comboTipoUsuario.setSelectedIndex(0);
        campoNivelPermissao.setText("");
        campoCrm.setText("");
        campoEspecialidade.setText("");
        campoDepartamento.setText("");
        campoCpf.setText("");
        campoDataNascimento.setText("");
        campoTelefone.setText("");
        campoEndereco.setText("");
        modoEdicao = false;
        usuarioSelecionadoEdicao = null;
        tabbedPane.setTitleAt(1, "Cadastro/Edição");
    }

    private void excluirUsuarioSelecionado() {
        int linhaSelecionada = tabelaUsuarios.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String username = (String) modeloTabela.getValueAt(linhaSelecionada, 0);
        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir o usuário '" + username + "'?",
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            String resultado = facade.removerUsuario(username);
            if (resultado.contains("sucesso")) {
                JOptionPane.showMessageDialog(this, resultado, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                atualizarTabelaUsuarios();
            } else {
                JOptionPane.showMessageDialog(this, resultado, "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void mostrarDetalhesUsuario() {
        int linhaSelecionada = tabelaUsuarios.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para ver detalhes.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String username = (String) modeloTabela.getValueAt(linhaSelecionada, 0);
        User usuario = facade.buscarUsuario(username);
        if (usuario != null) {
            StringBuilder detalhes = new StringBuilder();
            detalhes.append("Username: ").append(usuario.getUsername()).append("\n");
            detalhes.append("Email: ").append(usuario.getEmail()).append("\n");
            detalhes.append("Tipo: ").append(usuario.getTipoUsuario()).append("\n\n");
            if (usuario instanceof Administrador) {
                Administrador admin = (Administrador) usuario;
                detalhes.append("Nível de Permissão: ").append(admin.getNivelPermissao());
            } else if (usuario instanceof ProfissionalSaude) {
                ProfissionalSaude prof = (ProfissionalSaude) usuario;
                detalhes.append("CRM: ").append(prof.getCrm()).append("\n");
                detalhes.append("Especialidade: ").append(prof.getEspecialidade()).append("\n");
                detalhes.append("Departamento: ").append(prof.getDepartamento());
            } else if (usuario instanceof Paciente) {
                Paciente paciente = (Paciente) usuario;
                detalhes.append("CPF: ").append(paciente.getCpf()).append("\n");
                detalhes.append("Data de Nascimento: ").append(paciente.getDataNascimento()).append("\n");
                detalhes.append("Telefone: ").append(paciente.getTelefone()).append("\n");
                detalhes.append("Endereço: ").append(paciente.getEndereco());
            }
            JOptionPane.showMessageDialog(this, detalhes.toString(), "Detalhes do Usuário", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void iniciar(FacadeSingleton facade) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Erro ao definir Look and Feel: " + e.getMessage());
            }
            new UsuarioInterfaceGrafica(facade).setVisible(true);
        });
    }
}
