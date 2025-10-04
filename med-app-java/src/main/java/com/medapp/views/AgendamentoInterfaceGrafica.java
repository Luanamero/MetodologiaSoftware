package com.medapp.views;

import com.medapp.controllers.FacadeSingleton;
import com.medapp.models.Agendamento;
import com.medapp.models.User;
import com.medapp.models.Sala;
import com.medapp.models.ProfissionalSaude;
import com.medapp.models.Paciente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

public class AgendamentoInterfaceGrafica extends JFrame {
    private final FacadeSingleton facade;
    private JTabbedPane tabbedPane;
    
    // Componentes da listagem
    private JTable tabelaAgendamentos;
    private DefaultTableModel modeloTabela;
    
    // Componentes do formulário
    private JComboBox<String> comboPaciente;
    private JComboBox<String> comboProfissional;
    private JComboBox<String> comboSala;
    private JTextField campoDataHora;
    private JTextField campoTipoConsulta;
    private JTextArea campoObservacoes;
    
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public AgendamentoInterfaceGrafica(FacadeSingleton facade) {
        this.facade = facade;
        inicializarInterface();
        configurarEventos();
        atualizarTabelaAgendamentos();
        carregarComboBoxes();
    }

    private void inicializarInterface() {
        setTitle("MedApp - Gerenciamento de Agendamentos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Agendamentos", criarPainelListagem());
        tabbedPane.addTab("Novo Agendamento", criarPainelCriacao());
        
        add(tabbedPane);
    }

    private JPanel criarPainelListagem() {
        JPanel painel = new JPanel(new BorderLayout());
        
        // Tabela de agendamentos
        String[] colunas = {"ID", "Paciente", "Profissional", "Sala", "Data/Hora", "Tipo", "Status"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabelaAgendamentos = new JTable(modeloTabela);
        tabelaAgendamentos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tabelaAgendamentos);
        painel.add(scrollPane, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout());
        
        JButton botaoAtualizar = new JButton("Atualizar");
        JButton botaoConfirmar = new JButton("Confirmar Selecionado");
        JButton botaoCancelar = new JButton("Cancelar Selecionado");
        JButton botaoFinalizar = new JButton("Finalizar Selecionado");
        JButton botaoDetalhes = new JButton("Ver Detalhes");
        
        painelBotoes.add(botaoAtualizar);
        painelBotoes.add(botaoConfirmar);
        painelBotoes.add(botaoCancelar);
        painelBotoes.add(botaoFinalizar);
        painelBotoes.add(botaoDetalhes);
        
        painel.add(painelBotoes, BorderLayout.SOUTH);
        
        // Eventos dos botões
        botaoAtualizar.addActionListener(e -> atualizarTabelaAgendamentos());
        botaoConfirmar.addActionListener(e -> confirmarAgendamento());
        botaoCancelar.addActionListener(e -> cancelarAgendamento());
        botaoFinalizar.addActionListener(e -> finalizarAgendamento());
        botaoDetalhes.addActionListener(e -> mostrarDetalhesAgendamento());
        
        return painel;
    }

    private JPanel criarPainelCriacao() {
        JPanel painel = new JPanel(new BorderLayout());
        
        // Formulário
        JPanel formulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Paciente
        gbc.gridx = 0; gbc.gridy = 0;
        formulario.add(new JLabel("Paciente:"), gbc);
        gbc.gridx = 1;
        comboPaciente = new JComboBox<>();
        formulario.add(comboPaciente, gbc);
        
        // Profissional
        gbc.gridx = 0; gbc.gridy = 1;
        formulario.add(new JLabel("Profissional:"), gbc);
        gbc.gridx = 1;
        comboProfissional = new JComboBox<>();
        formulario.add(comboProfissional, gbc);
        
        // Sala
        gbc.gridx = 0; gbc.gridy = 2;
        formulario.add(new JLabel("Sala:"), gbc);
        gbc.gridx = 1;
        comboSala = new JComboBox<>();
        formulario.add(comboSala, gbc);
        
        // Data/Hora
        gbc.gridx = 0; gbc.gridy = 3;
        formulario.add(new JLabel("Data/Hora (dd/MM/yyyy HH:mm):"), gbc);
        gbc.gridx = 1;
        campoDataHora = new JTextField(20);
        campoDataHora.setToolTipText("Formato: 25/12/2023 14:30");
        formulario.add(campoDataHora, gbc);
        
        // Tipo de Consulta
        gbc.gridx = 0; gbc.gridy = 4;
        formulario.add(new JLabel("Tipo de Consulta:"), gbc);
        gbc.gridx = 1;
        campoTipoConsulta = new JTextField(20);
        formulario.add(campoTipoConsulta, gbc);
        
        // Observações
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formulario.add(new JLabel("Observações:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        campoObservacoes = new JTextArea(4, 20);
        campoObservacoes.setLineWrap(true);
        campoObservacoes.setWrapStyleWord(true);
        JScrollPane scrollObs = new JScrollPane(campoObservacoes);
        formulario.add(scrollObs, gbc);
        
        painel.add(formulario, BorderLayout.CENTER);
        
        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout());
        JButton botaoAgendar = new JButton("Agendar");
        JButton botaoLimpar = new JButton("Limpar Campos");
        JButton botaoAtualizar = new JButton("Atualizar Listas");
        
        painelBotoes.add(botaoAgendar);
        painelBotoes.add(botaoLimpar);
        painelBotoes.add(botaoAtualizar);
        
        painel.add(painelBotoes, BorderLayout.SOUTH);
        
        // Eventos
        botaoAgendar.addActionListener(e -> criarAgendamento());
        botaoLimpar.addActionListener(e -> limparCampos());
        botaoAtualizar.addActionListener(e -> carregarComboBoxes());
        
        return painel;
    }

    private void configurarEventos() {
        // Configurações adicionais se necessário
    }

    private void atualizarTabelaAgendamentos() {
        try {
            modeloTabela.setRowCount(0);
            List<Agendamento> agendamentos = facade.listarAgendamentosAtivos();
            
            for (Agendamento agendamento : agendamentos) {
                Object[] linha = {
                    agendamento.getId(),
                    agendamento.getPacienteUsername(),
                    agendamento.getProfissionalUsername(),
                    agendamento.getSalaId(),
                    agendamento.getDataHora().format(formatter),
                    agendamento.getTipoConsulta(),
                    agendamento.getStatus().toString()
                };
                modeloTabela.addRow(linha);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao atualizar lista de agendamentos: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarComboBoxes() {
        try {
            // Carregar pacientes
            comboPaciente.removeAllItems();
            comboPaciente.addItem("Selecione um paciente...");
            
            List<User> usuarios = facade.listarUsuarios();
            for (User usuario : usuarios) {
                if (usuario instanceof Paciente) {
                    comboPaciente.addItem(usuario.getUsername());
                }
            }
            
            // Carregar profissionais
            comboProfissional.removeAllItems();
            comboProfissional.addItem("Selecione um profissional...");
            
            for (User usuario : usuarios) {
                if (usuario instanceof ProfissionalSaude) {
                    comboProfissional.addItem(usuario.getUsername());
                }
            }
            
            // Carregar salas
            comboSala.removeAllItems();
            comboSala.addItem("Selecione uma sala...");
            
            List<Sala> salas = facade.listarSalasDisponiveis();
            for (Sala sala : salas) {
                comboSala.addItem(sala.getId() + " - " + sala.getNome());
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao carregar listas: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void criarAgendamento() {
        try {
            // Validar campos
            if (comboPaciente.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(this, "Selecione um paciente.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (comboProfissional.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(this, "Selecione um profissional.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (comboSala.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(this, "Selecione uma sala.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String dataHoraStr = campoDataHora.getText().trim();
            if (dataHoraStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe a data e hora.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String tipoConsulta = campoTipoConsulta.getText().trim();
            if (tipoConsulta.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o tipo da consulta.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Parse da data
            LocalDateTime dataHora;
            try {
                dataHora = LocalDateTime.parse(dataHoraStr, formatter);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, 
                    "Formato de data/hora inválido. Use: dd/MM/yyyy HH:mm", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Extrair dados
            String pacienteUsername = (String) comboPaciente.getSelectedItem();
            String profissionalUsername = (String) comboProfissional.getSelectedItem();
            String salaInfo = (String) comboSala.getSelectedItem();
            String salaId = salaInfo.split(" - ")[0]; // Extrair apenas o ID
            String observacoes = campoObservacoes.getText().trim();
            
            // Criar agendamento
            String resultado = facade.criarAgendamento(pacienteUsername, profissionalUsername, 
                                                     salaId, dataHora, tipoConsulta, observacoes);
            
            if (resultado.contains("sucesso")) {
                JOptionPane.showMessageDialog(this, resultado, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
                atualizarTabelaAgendamentos();
            } else {
                JOptionPane.showMessageDialog(this, resultado, "Erro", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao criar agendamento: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void confirmarAgendamento() {
        executarAcaoAgendamento("confirmar", facade::confirmarAgendamento);
    }

    private void cancelarAgendamento() {
        executarAcaoAgendamento("cancelar", facade::cancelarAgendamento);
    }

    private void finalizarAgendamento() {
        executarAcaoAgendamento("finalizar", facade::finalizarAgendamento);
    }

    private void executarAcaoAgendamento(String acao, java.util.function.Function<String, String> funcao) {
        int linhaSelecionada = tabelaAgendamentos.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um agendamento para " + acao + ".", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String id = (String) modeloTabela.getValueAt(linhaSelecionada, 0);
        String resultado = funcao.apply(id);
        
        if (resultado.contains("sucesso")) {
            JOptionPane.showMessageDialog(this, resultado, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            atualizarTabelaAgendamentos();
        } else {
            JOptionPane.showMessageDialog(this, resultado, "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDetalhesAgendamento() {
        int linhaSelecionada = tabelaAgendamentos.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um agendamento para ver detalhes.", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String id = (String) modeloTabela.getValueAt(linhaSelecionada, 0);
        Optional<Agendamento> agendamentoOpt = facade.buscarAgendamento(id);
        
        if (agendamentoOpt.isPresent()) {
            Agendamento agendamento = agendamentoOpt.get();
            
            StringBuilder detalhes = new StringBuilder();
            detalhes.append("=== DETALHES DO AGENDAMENTO ===\n\n");
            detalhes.append("ID: ").append(agendamento.getId()).append("\n");
            detalhes.append("Paciente: ").append(agendamento.getPacienteUsername()).append("\n");
            detalhes.append("Profissional: ").append(agendamento.getProfissionalUsername()).append("\n");
            detalhes.append("Sala: ").append(agendamento.getSalaId()).append("\n");
            detalhes.append("Data/Hora: ").append(agendamento.getDataHora().format(formatter)).append("\n");
            detalhes.append("Tipo: ").append(agendamento.getTipoConsulta()).append("\n");
            detalhes.append("Status: ").append(agendamento.getStatus()).append("\n");
            detalhes.append("Criado em: ").append(agendamento.getDataCriacao().format(formatter)).append("\n");
            detalhes.append("\nObservações:\n").append(agendamento.getObservacoes());
            
            JTextArea textArea = new JTextArea(detalhes.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));
            
            JOptionPane.showMessageDialog(this, scrollPane, 
                "Detalhes do Agendamento", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Agendamento não encontrado.", 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        comboPaciente.setSelectedIndex(0);
        comboProfissional.setSelectedIndex(0);
        comboSala.setSelectedIndex(0);
        campoDataHora.setText("");
        campoTipoConsulta.setText("");
        campoObservacoes.setText("");
    }

    /**
     * Método estático para inicializar a interface
     */
    public static void iniciar(FacadeSingleton facade) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Erro ao definir Look and Feel: " + e.getMessage());
            }
            
            new AgendamentoInterfaceGrafica(facade).setVisible(true);
        });
    }
}