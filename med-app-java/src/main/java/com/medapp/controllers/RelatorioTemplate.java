package com.medapp.controllers;

import com.medapp.models.Relatorio;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Template abstrato para geração de relatórios
 * Implementa o padrão Template Method
 */
public abstract class RelatorioTemplate {
    
    protected static final DateTimeFormatter DATA_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    /**
     * Método template que define o algoritmo de geração de relatório
     * @param relatorio Dados do relatório a ser gerado
     * @return Conteúdo do relatório gerado
     * @throws IOException Se houver erro na geração
     */
    public final String gerarRelatorio(Relatorio relatorio) throws IOException {
        StringBuilder resultado = new StringBuilder();
        
        // Passos do template method
        resultado.append(gerarCabecalho(relatorio));
        resultado.append(gerarCorpo(relatorio));
        resultado.append(gerarRodape(relatorio));
        
        // Pós-processamento específico do formato
        return processarFormatacao(resultado.toString());
    }
    
    /**
     * Gera o cabeçalho do relatório
     * @param relatorio Dados do relatório
     * @return Cabeçalho formatado
     */
    protected abstract String gerarCabecalho(Relatorio relatorio);
    
    /**
     * Gera o corpo principal do relatório
     * @param relatorio Dados do relatório
     * @return Corpo formatado
     */
    protected abstract String gerarCorpo(Relatorio relatorio);
    
    /**
     * Gera o rodapé do relatório
     * @param relatorio Dados do relatório
     * @return Rodapé formatado
     */
    protected abstract String gerarRodape(Relatorio relatorio);
    
    /**
     * Aplica formatação específica do tipo de relatório
     * @param conteudo Conteúdo bruto do relatório
     * @return Conteúdo formatado
     */
    protected abstract String processarFormatacao(String conteudo);
    
    /**
     * Método helper para formatar data
     * @param data Data a ser formatada
     * @return Data formatada como string
     */
    protected String formatarData(LocalDateTime data) {
        return data != null ? data.format(DATA_FORMATTER) : "Data não disponível";
    }
    
    /**
     * Método helper para gerar informações básicas do relatório
     * @param relatorio Dados do relatório
     * @return Informações básicas formatadas
     */
    protected String gerarInformacoesBasicas(Relatorio relatorio) {
        StringBuilder info = new StringBuilder();
        info.append("ID: ").append(relatorio.getId()).append("\\n");
        info.append("Título: ").append(relatorio.getTitulo()).append("\\n");
        info.append("Autor: ").append(relatorio.getAutorUsername()).append("\\n");
        info.append("Data de Geração: ").append(formatarData(relatorio.getDataGeracao())).append("\\n");
        info.append("Status: ").append(relatorio.getStatus()).append("\\n");
        info.append("Tipo: ").append(relatorio.getTipoRelatorio()).append("\\n");
        
        if (relatorio.getDescricao() != null && !relatorio.getDescricao().trim().isEmpty()) {
            info.append("Descrição: ").append(relatorio.getDescricao()).append("\\n");
        }
        
        return info.toString();
    }
    
    /**
     * Valida se o relatório tem os dados mínimos necessários
     * @param relatorio Relatório a ser validado
     * @throws IllegalArgumentException Se o relatório for inválido
     */
    protected void validarRelatorio(Relatorio relatorio) {
        if (relatorio == null) {
            throw new IllegalArgumentException("Relatório não pode ser nulo");
        }
        if (relatorio.getTitulo() == null || relatorio.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("Título do relatório é obrigatório");
        }
        if (relatorio.getConteudo() == null || relatorio.getConteudo().trim().isEmpty()) {
            throw new IllegalArgumentException("Conteúdo do relatório é obrigatório");
        }
        if (relatorio.getAutorUsername() == null || relatorio.getAutorUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Autor do relatório é obrigatório");
        }
    }
    
    /**
     * Factory method para criar template baseado no tipo
     * @param tipo Tipo do template (PDF, HTML)
     * @return Template específico
     */
    public static RelatorioTemplate criarTemplate(String tipo) {
        switch (tipo.toUpperCase()) {
            case "PDF":
                return new RelatorioTemplatePDF();
            case "HTML":
                return new RelatorioTemplateHTML();
            default:
                throw new IllegalArgumentException("Tipo de template não suportado: " + tipo);
        }
    }
}
