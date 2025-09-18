package com.medapp.controllers;

import com.medapp.models.Relatorio;

/**
 * Template específico para geração de relatórios em formato PDF
 * Implementa o padrão Template Method com formatação específica para PDF
 */
public class RelatorioTemplatePDF extends RelatorioTemplate {
    
    private static final String PDF_HEADER = "%PDF-1.4\n";
    private static final String PDF_SEPARATOR = "\n---PDF-SEPARATOR---\n";
    
    @Override
    protected String gerarCabecalho(Relatorio relatorio) {
        validarRelatorio(relatorio);
        
        StringBuilder cabecalho = new StringBuilder();
        cabecalho.append("===============================================\n");
        cabecalho.append("           RELATÓRIO MÉDICO - PDF            \n");
        cabecalho.append("===============================================\n\n");
        
        // Informações básicas do relatório
        cabecalho.append(gerarInformacoesBasicas(relatorio));
        cabecalho.append(PDF_SEPARATOR);
        
        return cabecalho.toString();
    }
    
    @Override
    protected String gerarCorpo(Relatorio relatorio) {
        StringBuilder corpo = new StringBuilder();
        corpo.append("CONTEÚDO DO RELATÓRIO:\n");
        corpo.append("===============================================\n");
        
        // Processa o conteúdo para formato PDF
        String conteudo = relatorio.getConteudo();
        
        // Divide o conteúdo em seções se houver marcadores
        if (conteudo.contains("##")) {
            String[] secoes = conteudo.split("##");
            for (int i = 1; i < secoes.length; i++) { // Pula o primeiro elemento vazio
                String secao = secoes[i].trim();
                if (!secao.isEmpty()) {
                    String[] linhas = secao.split("\n", 2);
                    String titulo = linhas[0].trim();
                    String texto = linhas.length > 1 ? linhas[1].trim() : "";
                    
                    corpo.append("\n[SEÇÃO] ").append(titulo.toUpperCase()).append("\n");
                    corpo.append("-----------------------------------------------\n");
                    corpo.append(texto).append("\n");
                }
            }
        } else {
            // Conteúdo simples sem seções
            corpo.append(conteudo);
        }
        
        corpo.append("\n").append(PDF_SEPARATOR);
        return corpo.toString();
    }
    
    @Override
    protected String gerarRodape(Relatorio relatorio) {
        StringBuilder rodape = new StringBuilder();
        rodape.append("\n===============================================\n");
        rodape.append("              INFORMAÇÕES ADICIONAIS          \n");
        rodape.append("===============================================\n");
        
        rodape.append("Relatório gerado automaticamente pelo sistema MedApp\n");
        rodape.append("Formato: PDF\n");
        rodape.append("Data de processamento: ").append(formatarData(relatorio.getDataGeracao())).append("\n");
        
        if (relatorio.getDescricao() != null && !relatorio.getDescricao().trim().isEmpty()) {
            rodape.append("Observações: ").append(relatorio.getDescricao()).append("\n");
        }
        
        rodape.append("\n[FIM DO RELATÓRIO PDF]\n");
        rodape.append("===============================================");
        
        return rodape.toString();
    }
    
    @Override
    protected String processarFormatacao(String conteudo) {
        StringBuilder pdfFormatado = new StringBuilder();
        
        // Adiciona cabeçalho PDF básico
        pdfFormatado.append(PDF_HEADER);
        pdfFormatado.append("% Relatório gerado pelo MedApp\n");
        pdfFormatado.append("% ").append(java.time.LocalDateTime.now()).append("\n\n");
        
        // Processa formatação específica do PDF
        String conteudoProcessado = conteudo
                .replace("\n", "\r\n")  // Line breaks específicos do PDF
                .replace("&", "&amp;")     // Escape de caracteres especiais
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace(PDF_SEPARATOR, "\n\n"); // Remove separadores internos
        
        pdfFormatado.append(conteudoProcessado);
        
        // Adiciona metadados do PDF
        pdfFormatado.append("\n\n");
        pdfFormatado.append("% PDF Metadata\n");
        pdfFormatado.append("% Creator: MedApp System\n");
        pdfFormatado.append("% Producer: RelatorioTemplatePDF\n");
        
        return pdfFormatado.toString();
    }
    
    /**
     * Método específico para adicionar formatação de tabelas no PDF
     * @param dados Array de dados para a tabela
     * @param colunas Nomes das colunas
     * @return Tabela formatada para PDF
     */
    public String gerarTabela(String[][] dados, String[] colunas) {
        StringBuilder tabela = new StringBuilder();
        tabela.append("\n[TABELA]\n");
        
        // Cabeçalho da tabela
        tabela.append("| ");
        for (String coluna : colunas) {
            tabela.append(String.format("%-15s | ", coluna));
        }
        tabela.append("\n");
        
        // Linha separadora
        tabela.append("|");
        for (int i = 0; i < colunas.length; i++) {
            tabela.append("-----------------|");
        }
        tabela.append("\n");
        
        // Dados da tabela
        for (String[] linha : dados) {
            tabela.append("| ");
            for (int i = 0; i < linha.length && i < colunas.length; i++) {
                tabela.append(String.format("%-15s | ", linha[i]));
            }
            tabela.append("\n");
        }
        
        tabela.append("[FIM TABELA]\n");
        return tabela.toString();
    }
}
