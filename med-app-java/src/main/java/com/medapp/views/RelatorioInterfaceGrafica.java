package com.medapp.views;

import com.medapp.controllers.FacadeSingleton;
import com.medapp.controllers.RelatorioTemplate;
import com.medapp.models.Relatorio;
import com.medapp.infra.AbstratoLogs;
import com.medapp.infra.FabricaLogs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Interface gráfica específica para gerenciamento de relatórios
 * Implementa uma GUI usando Swing para operações CRUD de relatórios
 */
public class RelatorioInterfaceGrafica extends JFrame {
    
    private final FacadeSingleton facade;
    private final AbstratoLogs logger;
    
    // Componentes da interface
    private JTabbedPane tabbedPane;
    private JTable tabelaRelatorios;
    private DefaultTableModel modeloTabela;
    
    // Componentes para criação de relatório
    private JTextField campoTitulo;
    private JTextArea campoConteudo;
    private JComboBox<String> comboTipoRelatorio;
    private JComboBox<String> comboAutor;  // Mudado de JTextField para JComboBox
    private JTextArea campoDescricao;
    
    // Componentes para visualização
    private JTextArea areaVisualizacao;
    private JLabel labelStatusSelecionado;
    
    public RelatorioInterfaceGrafica(FacadeSingleton facade) {
        this.facade = facade;
        this.logger = FabricaLogs.criarLoggerPadrao();
        
        inicializarInterface();
        configurarEventos();
        atualizarTabelaRelatorios();
        
        logger.registrarEvento("INTERFACE_RELATORIO", "Interface gráfica de relatórios inicializada");
    }
    
    private void inicializarInterface() {
        setTitle("MedApp - Gerenciamento de Relatórios");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        // Criar painel principal com abas
        tabbedPane = new JTabbedPane();
        
        // Aba de listagem
        tabbedPane.addTab("Relatórios", criarPainelListagem());
        
        // Aba de criação
        tabbedPane.addTab("Novo Relatório", criarPainelCriacao());
        
        // Aba de visualização
        tabbedPane.addTab("Visualizar", criarPainelVisualizacao());
        
        // Aba de estatísticas
        tabbedPane.addTab("Estatísticas", criarPainelEstatisticas());
        
        add(tabbedPane);
    }
    
    private JPanel criarPainelListagem() {
        JPanel painel = new JPanel(new BorderLayout());
        
        // Criar tabela de relatórios
        String[] colunas = {"ID", "Título", "Autor", "Tipo", "Status", "Data", "ID_COMPLETO"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabela somente leitura
            }
        };
        
        tabelaRelatorios = new JTable(modeloTabela);
        tabelaRelatorios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Ocultar a coluna do ID completo
        tabelaRelatorios.getColumnModel().getColumn(6).setMinWidth(0);
        tabelaRelatorios.getColumnModel().getColumn(6).setMaxWidth(0);
        tabelaRelatorios.getColumnModel().getColumn(6).setWidth(0);
        
        // Configurar largura das colunas
        tabelaRelatorios.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
        tabelaRelatorios.getColumnModel().getColumn(1).setPreferredWidth(200); // Título
        tabelaRelatorios.getColumnModel().getColumn(2).setPreferredWidth(120); // Autor
        tabelaRelatorios.getColumnModel().getColumn(3).setPreferredWidth(80);  // Tipo
        tabelaRelatorios.getColumnModel().getColumn(4).setPreferredWidth(100); // Status
        tabelaRelatorios.getColumnModel().getColumn(5).setPreferredWidth(150); // Data
        
        JScrollPane scrollPane = new JScrollPane(tabelaRelatorios);
        painel.add(scrollPane, BorderLayout.CENTER);
        
        // Painel de filtros
        JPanel painelFiltros = new JPanel(new FlowLayout());
        painelFiltros.setBorder(BorderFactory.createTitledBorder("Filtros"));
        
        JComboBox<String> filtroStatus = new JComboBox<>(new String[]{"Todos", "PENDENTE", "PROCESSANDO", "CONCLUIDO", "ERRO"});
        JComboBox<String> filtroTipo = new JComboBox<>(new String[]{"Todos", "PDF", "HTML"});
        JTextField filtroAutor = new JTextField(15);
        
        painelFiltros.add(new JLabel("Status:"));
        painelFiltros.add(filtroStatus);
        painelFiltros.add(new JLabel("Tipo:"));
        painelFiltros.add(filtroTipo);
        painelFiltros.add(new JLabel("Autor:"));
        painelFiltros.add(filtroAutor);
        
        JButton botaoFiltrar = new JButton("Filtrar");
        JButton botaoLimparFiltro = new JButton("Limpar");
        painelFiltros.add(botaoFiltrar);
        painelFiltros.add(botaoLimparFiltro);
        
        painel.add(painelFiltros, BorderLayout.NORTH);
        
        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout());
        
        JButton botaoAtualizar = new JButton("Atualizar Lista");
        JButton botaoExcluir = new JButton("Excluir Selecionado");
        JButton botaoExportar = new JButton("Exportar Selecionado");
        JButton botaoAtualizarStatus = new JButton("Atualizar Status");
        
        painelBotoes.add(botaoAtualizar);
        painelBotoes.add(botaoExcluir);
        painelBotoes.add(botaoExportar);
        painelBotoes.add(botaoAtualizarStatus);
        
        painel.add(painelBotoes, BorderLayout.SOUTH);
        
        // Eventos dos botões
        botaoAtualizar.addActionListener(e -> atualizarTabelaRelatorios());
        botaoExcluir.addActionListener(e -> excluirRelatorioSelecionado());
        botaoExportar.addActionListener(e -> exportarRelatorioSelecionado());
        botaoAtualizarStatus.addActionListener(e -> atualizarStatusRelatorio());
        
        // Eventos dos filtros
        botaoFiltrar.addActionListener(e -> aplicarFiltros(filtroStatus, filtroTipo, filtroAutor));
        botaoLimparFiltro.addActionListener(e -> {
            filtroStatus.setSelectedIndex(0);
            filtroTipo.setSelectedIndex(0);
            filtroAutor.setText("");
            atualizarTabelaRelatorios();
        });
        
        return painel;
    }
    
    private JPanel criarPainelCriacao() {
        JPanel painel = new JPanel(new BorderLayout());
        
        // Formulário de criação
        JPanel formulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Título
        gbc.gridx = 0; gbc.gridy = 0;
        formulario.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        campoTitulo = new JTextField(30);
        formulario.add(campoTitulo, gbc);
        
        // Autor
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        formulario.add(new JLabel("Autor:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel painelAutor = new JPanel(new BorderLayout());
        comboAutor = new JComboBox<>();
        carregarUsuarios(); // Carregar usuários cadastrados
        painelAutor.add(comboAutor, BorderLayout.CENTER);
        JButton botaoAtualizarUsuarios = new JButton("↻");
        botaoAtualizarUsuarios.setToolTipText("Atualizar lista de usuários");
        botaoAtualizarUsuarios.setPreferredSize(new Dimension(30, botaoAtualizarUsuarios.getPreferredSize().height));
        botaoAtualizarUsuarios.addActionListener(e -> carregarUsuarios());
        painelAutor.add(botaoAtualizarUsuarios, BorderLayout.EAST);
        formulario.add(painelAutor, gbc);
        
        // Tipo de relatório
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        formulario.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        comboTipoRelatorio = new JComboBox<>(new String[]{"PDF", "HTML"});
        formulario.add(comboTipoRelatorio, gbc);
        
        // Conteúdo
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        formulario.add(new JLabel("Conteúdo:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 0.6;
        campoConteudo = new JTextArea(10, 30);
        campoConteudo.setLineWrap(true);
        campoConteudo.setWrapStyleWord(true);
        formulario.add(new JScrollPane(campoConteudo), gbc);
        
        // Descrição
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0; gbc.weighty = 0;
        formulario.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 0.3;
        campoDescricao = new JTextArea(4, 30);
        campoDescricao.setLineWrap(true);
        campoDescricao.setWrapStyleWord(true);
        formulario.add(new JScrollPane(campoDescricao), gbc);
        
        painel.add(formulario, BorderLayout.CENTER);
        
        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout());
        JButton botaoCriar = new JButton("Criar Relatório");
        JButton botaoLimpar = new JButton("Limpar Campos");
        JButton botaoPreview = new JButton("Preview");
        
        painelBotoes.add(botaoCriar);
        painelBotoes.add(botaoLimpar);
        painelBotoes.add(botaoPreview);
        painel.add(painelBotoes, BorderLayout.SOUTH);
        
        // Eventos
        botaoCriar.addActionListener(e -> criarRelatorio());
        botaoLimpar.addActionListener(e -> limparCamposRelatorio());
        botaoPreview.addActionListener(e -> mostrarPreview());
        
        return painel;
    }
    
    private void carregarUsuarios() {
        try {
            comboAutor.removeAllItems();
            comboAutor.addItem("Selecione um usuário...");
            
            List<com.medapp.models.User> usuarios = facade.listarUsuarios();
            for (com.medapp.models.User usuario : usuarios) {
                comboAutor.addItem(usuario.getUsername());
            }
            
            if (comboAutor.getItemCount() > 1) {
                comboAutor.setSelectedIndex(0); // Selecionar o placeholder
            }
        } catch (Exception e) {
            logger.error("Erro ao carregar usuários", e);
            JOptionPane.showMessageDialog(this, "Erro ao carregar lista de usuários: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método público para atualizar a lista de usuários
     * Deve ser chamado quando novos usuários são criados
     */
    public void atualizarListaUsuarios() {
        carregarUsuarios();
    }
    
    private JPanel criarPainelVisualizacao() {
        JPanel painel = new JPanel(new BorderLayout());
        
        // Área de visualização
        areaVisualizacao = new JTextArea();
        areaVisualizacao.setEditable(false);
        areaVisualizacao.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        areaVisualizacao.setLineWrap(true);
        areaVisualizacao.setWrapStyleWord(true);
        areaVisualizacao.setRows(20);
        areaVisualizacao.setColumns(80);
        
        JScrollPane scrollPane = new JScrollPane(areaVisualizacao);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        painel.add(scrollPane, BorderLayout.CENTER);
        
        // Painel de informações
        JPanel painelInfo = new JPanel(new FlowLayout());
        labelStatusSelecionado = new JLabel("Nenhum relatório selecionado");
        painelInfo.add(labelStatusSelecionado);
        
        painel.add(painelInfo, BorderLayout.NORTH);
        
        // Botões (apenas limpar)
        JPanel painelBotoes = new JPanel(new FlowLayout());
        JButton botaoLimpar = new JButton("Limpar Visualização");
        botaoLimpar.setToolTipText("Limpar a área de visualização abaixo");
        
        painelBotoes.add(botaoLimpar);
        painel.add(painelBotoes, BorderLayout.SOUTH);
        
        // Eventos
        botaoLimpar.addActionListener(e -> {
            areaVisualizacao.setText("");
            labelStatusSelecionado.setText("Visualização limpa");
        });
        
        return painel;
    }
    
    private JPanel criarPainelEstatisticas() {
        JPanel painel = new JPanel(new BorderLayout());
        
        JTextArea areaEstatisticas = new JTextArea();
        areaEstatisticas.setEditable(false);
        areaEstatisticas.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(areaEstatisticas);
        painel.add(scrollPane, BorderLayout.CENTER);
        
        JButton botaoAtualizar = new JButton("Atualizar Estatísticas");
        JPanel painelBotao = new JPanel(new FlowLayout());
        painelBotao.add(botaoAtualizar);
        painel.add(painelBotao, BorderLayout.SOUTH);
        
        botaoAtualizar.addActionListener(e -> {
            // Redirecionar saída para capturar as estatísticas
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            java.io.PrintStream originalOut = System.out;
            System.setOut(new java.io.PrintStream(baos));
            
            facade.getRelatorioGerenciador().exibirEstatisticas();
            
            System.setOut(originalOut);
            areaEstatisticas.setText(baos.toString());
        });
        
        return painel;
    }
    
    private void configurarEventos() {
        // Evento de seleção na tabela
        tabelaRelatorios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int linhaSelecionada = tabelaRelatorios.getSelectedRow();
                if (linhaSelecionada != -1) {
                    String id = (String) modeloTabela.getValueAt(linhaSelecionada, 0);
                    String titulo = (String) modeloTabela.getValueAt(linhaSelecionada, 1);
                    String status = (String) modeloTabela.getValueAt(linhaSelecionada, 4);
                    
                    labelStatusSelecionado.setText(String.format("Selecionado: %s (%s) - Status: %s", titulo, id, status));
                    
                    // Carregar automaticamente o relatório na aba de visualização
                    carregarRelatorioSelecionado();
                }
            }
        });
    }
    
    private void atualizarTabelaRelatorios() {
        try {
            modeloTabela.setRowCount(0); // Limpar tabela
            
            List<Relatorio> relatorios = facade.getRelatorioGerenciador().listarRelatorios();
            
            for (Relatorio relatorio : relatorios) {
                Object[] linha = {
                    relatorio.getId().substring(0, Math.min(8, relatorio.getId().length())), // ID truncado para exibição
                    relatorio.getTitulo(),
                    relatorio.getAutorUsername(),
                    relatorio.getTipoRelatorio(),
                    relatorio.getStatus(),
                    relatorio.getDataGeracao() != null ? relatorio.getDataGeracao().toString() : "N/A",
                    relatorio.getId() // ID completo oculto na última coluna
                };
                modeloTabela.addRow(linha);
            }
            
            logger.registrarEvento("TABELA_RELATORIOS_ATUALIZADA", "Tabela atualizada com " + relatorios.size() + " relatórios");
            
        } catch (Exception e) {
            logger.error("Erro ao atualizar tabela de relatórios", e);
            JOptionPane.showMessageDialog(this, "Erro ao atualizar lista de relatórios: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void criarRelatorio() {
        try {
            String titulo = campoTitulo.getText().trim();
            String conteudo = campoConteudo.getText().trim();
            String tipo = (String) comboTipoRelatorio.getSelectedItem();
            String autor = (String) comboAutor.getSelectedItem();
            
            if (titulo.isEmpty() || conteudo.isEmpty() || autor == null || autor.isEmpty() || "Selecione um usuário...".equals(autor)) {
                JOptionPane.showMessageDialog(this, "Todos os campos obrigatórios devem ser preenchidos.\nSelecione um usuário cadastrado como autor.",
                        "Campos Obrigatórios", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            boolean sucesso = facade.getRelatorioGerenciador().criarRelatorio(titulo, conteudo, tipo, autor);
            
            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Relatório criado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparCamposRelatorio();
                atualizarTabelaRelatorios();
                logger.registrarEvento("RELATORIO_CRIADO", "Relatório criado: " + titulo);
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao criar relatório.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            logger.error("Erro ao criar relatório", e);
            JOptionPane.showMessageDialog(this, "Erro ao criar relatório: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limparCamposRelatorio() {
        campoTitulo.setText("");
        campoConteudo.setText("");
        comboAutor.setSelectedIndex(-1); // Limpar seleção
        campoDescricao.setText("");
        comboTipoRelatorio.setSelectedIndex(0);
    }
    
    private void mostrarPreview() {
        try {
            String titulo = campoTitulo.getText().trim();
            String conteudo = campoConteudo.getText().trim();
            String tipo = (String) comboTipoRelatorio.getSelectedItem();
            String autor = (String) comboAutor.getSelectedItem();
            
            if (titulo.isEmpty() || conteudo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Título e conteúdo são obrigatórios para preview.",
                        "Preview", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Criar relatório temporário para preview
            Relatorio relatorioTemp = new Relatorio("PREVIEW", titulo, conteudo, tipo, autor);
            
            RelatorioTemplate template = RelatorioTemplate.criarTemplate(tipo);
            String relatorioGerado = template.gerarRelatorio(relatorioTemp);
            
            // Mostrar preview na aba de visualização
            tabbedPane.setSelectedIndex(2); // Aba de visualização
            areaVisualizacao.setText(relatorioGerado);
            labelStatusSelecionado.setText("Preview: " + titulo + " (" + tipo + ")");
            
        } catch (Exception e) {
            logger.error("Erro ao gerar preview", e);
            JOptionPane.showMessageDialog(this, "Erro ao gerar preview: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void carregarRelatorioSelecionado() {
        int linhaSelecionada = tabelaRelatorios.getSelectedRow();
        if (linhaSelecionada == -1) {
            return; // Não há seleção
        }
        
        // Usar o ID completo da coluna oculta
        String idCompleto = (String) modeloTabela.getValueAt(linhaSelecionada, 6);
        
        // Buscar o relatório pelo ID completo
        Optional<Relatorio> relatorioOpt = facade.getRelatorioGerenciador().buscarRelatorio(idCompleto);
        
        if (relatorioOpt.isPresent()) {
            try {
                Relatorio relatorio = relatorioOpt.get();
                RelatorioTemplate template = RelatorioTemplate.criarTemplate(relatorio.getTipoRelatorio());
                String relatorioGerado = template.gerarRelatorio(relatorio);
                
                areaVisualizacao.setText(relatorioGerado);
                
            } catch (Exception e) {
                logger.error("Erro ao gerar relatório para visualização", e);
                areaVisualizacao.setText("Erro ao carregar relatório: " + e.getMessage());
            }
        }
    }
    
    private void excluirRelatorioSelecionado() {
        int linhaSelecionada = tabelaRelatorios.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um relatório para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String id = (String) modeloTabela.getValueAt(linhaSelecionada, 6); // ID completo da coluna oculta
        String titulo = (String) modeloTabela.getValueAt(linhaSelecionada, 1);
        
        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir o relatório '" + titulo + "'?",
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                boolean sucesso = facade.getRelatorioGerenciador().excluirRelatorio(id);
                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Relatório excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    logger.registrarEvento("RELATORIO_EXCLUIDO", "Relatório excluído: " + titulo);
                    atualizarTabelaRelatorios();
                    
                    // Limpar área de visualização se estava exibindo o relatório excluído
                    areaVisualizacao.setText("");
                    labelStatusSelecionado.setText("Nenhum relatório selecionado");
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir relatório.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                logger.error("Erro ao excluir relatório", e);
                JOptionPane.showMessageDialog(this, "Erro ao excluir relatório: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void exportarRelatorioSelecionado() {
        // Verificar se há um relatório selecionado na tabela
        int linhaSelecionada = tabelaRelatorios.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um relatório na tabela para exportar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Obter informações do relatório selecionado
        String idCompleto = (String) modeloTabela.getValueAt(linhaSelecionada, 6);
        String titulo = (String) modeloTabela.getValueAt(linhaSelecionada, 1);
        String tipo = (String) modeloTabela.getValueAt(linhaSelecionada, 3);
        
        // Definir extensão correta baseada no tipo
        String extensao = tipo.toLowerCase();
        String nomeArquivo = titulo.replaceAll("[^a-zA-Z0-9]", "_") + "." + extensao;
        
        // Buscar e gerar o relatório
        try {
            Optional<Relatorio> relatorioOpt = facade.getRelatorioGerenciador().buscarRelatorio(idCompleto);
            if (!relatorioOpt.isPresent()) {
                JOptionPane.showMessageDialog(this, "Relatório não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Relatorio relatorio = relatorioOpt.get();
            RelatorioTemplate template = RelatorioTemplate.criarTemplate(relatorio.getTipoRelatorio());
            String conteudoRelatorio = template.gerarRelatorio(relatorio);
            
            // Proceder com a exportação usando o conteúdo gerado
            exportarConteudo(conteudoRelatorio, nomeArquivo);
            
        } catch (Exception e) {
            logger.error("Erro ao gerar relatório para exportação", e);
            JOptionPane.showMessageDialog(this, "Erro ao gerar relatório: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void exportarConteudo(String conteudo, String nomeArquivoSugerido) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exportar Relatório");
        fileChooser.setSelectedFile(new File(System.getProperty("user.home") + File.separator + nomeArquivoSugerido));
        
        // Adicionar filtros de formato baseados na extensão do arquivo
        if (nomeArquivoSugerido.toLowerCase().endsWith(".pdf")) {
            fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Arquivos PDF (*.pdf)", "pdf"));
        } else if (nomeArquivoSugerido.toLowerCase().endsWith(".html")) {
            fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Arquivos HTML (*.html)", "html"));
        }
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Arquivos de Texto (*.txt)", "txt"));
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Todos os Arquivos (*.*)", "*"));
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File arquivo = fileChooser.getSelectedFile();
                
                // Garantir extensão correta do arquivo
                String path = arquivo.getAbsolutePath();
                String extensaoOriginal = "";
                if (nomeArquivoSugerido.contains(".")) {
                    extensaoOriginal = nomeArquivoSugerido.substring(nomeArquivoSugerido.lastIndexOf("."));
                }
                
                if (!path.contains(".")) {
                    arquivo = new File(path + extensaoOriginal);
                }
                
                try (FileWriter writer = new FileWriter(arquivo, java.nio.charset.StandardCharsets.UTF_8)) {
                    writer.write(conteudo);
                    writer.flush();
                }
                
                // Verificar se arquivo foi criado
                if (arquivo.exists() && arquivo.length() > 0) {
                    String mensagem = String.format("Relatório exportado com sucesso!\n\nArquivo: %s\nTamanho: %d bytes", 
                                                   arquivo.getAbsolutePath(), arquivo.length());
                    JOptionPane.showMessageDialog(this, mensagem, "Exportação Concluída", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Perguntar se quer abrir o arquivo
                    int opcao = JOptionPane.showConfirmDialog(this, 
                        "Deseja abrir o arquivo exportado?", 
                        "Abrir Arquivo", 
                        JOptionPane.YES_NO_OPTION);
                    
                    if (opcao == JOptionPane.YES_OPTION) {
                        try {
                            java.awt.Desktop.getDesktop().open(arquivo);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this, 
                                "Não foi possível abrir o arquivo automaticamente.\nArquivo salvo em: " + arquivo.getAbsolutePath(), 
                                "Info", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Erro: arquivo não foi criado corretamente.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
                
                logger.registrarEvento("RELATORIO_EXPORTADO", "Relatório exportado para: " + arquivo.getAbsolutePath());
            } catch (IOException e) {
                logger.error("Erro ao exportar relatório", e);
                JOptionPane.showMessageDialog(this, "Erro ao exportar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void atualizarStatusRelatorio() {
        int linhaSelecionada = tabelaRelatorios.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um relatório.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String[] status = {"PENDENTE", "PROCESSANDO", "CONCLUIDO", "ERRO"};
        String novoStatus = (String) JOptionPane.showInputDialog(this, "Selecione o novo status:",
                "Atualizar Status", JOptionPane.QUESTION_MESSAGE, null, status, status[0]);
        
        if (novoStatus != null) {
            try {
                // Obter o ID completo do relatório selecionado (coluna oculta)
                String idRelatorio = (String) modeloTabela.getValueAt(linhaSelecionada, 6);
                
                // Atualizar o status usando o RelatorioGerenciador
                boolean sucesso = facade.getRelatorioGerenciador().atualizarStatusRelatorio(idRelatorio, novoStatus);
                
                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Status atualizado para: " + novoStatus, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    logger.registrarEvento("STATUS_ATUALIZADO", "Status do relatório " + idRelatorio + " atualizado para " + novoStatus);
                    atualizarTabelaRelatorios();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao atualizar status.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                logger.error("Erro ao atualizar status do relatório", e);
                JOptionPane.showMessageDialog(this, "Erro ao atualizar status: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void aplicarFiltros(JComboBox<String> filtroStatus, JComboBox<String> filtroTipo, JTextField filtroAutor) {
        try {
            String statusSelecionado = (String) filtroStatus.getSelectedItem();
            String tipoSelecionado = (String) filtroTipo.getSelectedItem();
            String autorFiltro = filtroAutor.getText().trim();
            
            // Limpar tabela
            modeloTabela.setRowCount(0);
            
            // Obter todos os relatórios
            List<Relatorio> todosRelatorios = facade.getRelatorioGerenciador().listarRelatorios();
            
            // Aplicar filtros
            List<Relatorio> relatoriosFiltrados = todosRelatorios.stream()
                .filter(relatorio -> {
                    // Filtro por status
                    if (!"Todos".equals(statusSelecionado) && !statusSelecionado.equals(relatorio.getStatus())) {
                        return false;
                    }
                    
                    // Filtro por tipo
                    if (!"Todos".equals(tipoSelecionado) && !tipoSelecionado.equals(relatorio.getTipoRelatorio())) {
                        return false;
                    }
                    
                    // Filtro por autor
                    if (!autorFiltro.isEmpty() && !relatorio.getAutorUsername().toLowerCase().contains(autorFiltro.toLowerCase())) {
                        return false;
                    }
                    
                    return true;
                })
                .collect(Collectors.toList());
            
            // Preencher tabela com relatórios filtrados
            for (Relatorio relatorio : relatoriosFiltrados) {
                Object[] linha = {
                    relatorio.getId().substring(0, Math.min(8, relatorio.getId().length())),
                    relatorio.getTitulo(),
                    relatorio.getAutorUsername(),
                    relatorio.getTipoRelatorio(),
                    relatorio.getStatus(),
                    relatorio.getDataGeracao() != null ? relatorio.getDataGeracao().toString() : "N/A",
                    relatorio.getId() // ID completo oculto
                };
                modeloTabela.addRow(linha);
            }
            
            logger.registrarEvento("FILTROS_APLICADOS", 
                String.format("Status: %s, Tipo: %s, Autor: %s - %d relatórios encontrados", 
                    statusSelecionado, tipoSelecionado, autorFiltro, relatoriosFiltrados.size()));
                    
        } catch (Exception e) {
            logger.error("Erro ao aplicar filtros", e);
            JOptionPane.showMessageDialog(this, "Erro ao aplicar filtros: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            // Em caso de erro, atualizar normalmente
            atualizarTabelaRelatorios();
        }
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
            
            new RelatorioInterfaceGrafica(facade).setVisible(true);
        });
    }
}
