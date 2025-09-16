package com.medapp.controllers;

import com.medapp.models.Relatorio;

/**
 * Template específico para geração de relatórios em formato HTML
 * Implementa o padrão Template Method com formatação específica para HTML
 */
public class RelatorioTemplateHTML extends RelatorioTemplate {
    
    @Override
    protected String gerarCabecalho(Relatorio relatorio) {
        validarRelatorio(relatorio);
        
        StringBuilder cabecalho = new StringBuilder();
        cabecalho.append("<!DOCTYPE html>\\n");
        cabecalho.append("<html lang=\"pt-BR\">\\n");
        cabecalho.append("<head>\\n");
        cabecalho.append("    <meta charset=\"UTF-8\">\\n");
        cabecalho.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\\n");
        cabecalho.append("    <title>").append(escapeHtml(relatorio.getTitulo())).append("</title>\\n");
        cabecalho.append("    <style>\\n");
        cabecalho.append(gerarCSS());
        cabecalho.append("    </style>\\n");
        cabecalho.append("</head>\\n");
        cabecalho.append("<body>\\n");
        cabecalho.append("    <div class=\"container\">\\n");
        cabecalho.append("        <header class=\"header\">\\n");
        cabecalho.append("            <h1>Relatório Médico</h1>\\n");
        cabecalho.append("            <h2>").append(escapeHtml(relatorio.getTitulo())).append("</h2>\\n");
        cabecalho.append("        </header>\\n");
        
        // Informações básicas em formato HTML
        cabecalho.append("        <section class=\"info-section\">\\n");
        cabecalho.append("            <h3>Informações do Relatório</h3>\\n");
        cabecalho.append("            <div class=\"info-grid\">\\n");
        cabecalho.append("                <div class=\"info-item\">\\n");
        cabecalho.append("                    <strong>ID:</strong> ").append(escapeHtml(relatorio.getId())).append("\\n");
        cabecalho.append("                </div>\\n");
        cabecalho.append("                <div class=\"info-item\">\\n");
        cabecalho.append("                    <strong>Autor:</strong> ").append(escapeHtml(relatorio.getAutorUsername())).append("\\n");
        cabecalho.append("                </div>\\n");
        cabecalho.append("                <div class=\"info-item\">\\n");
        cabecalho.append("                    <strong>Data:</strong> ").append(formatarData(relatorio.getDataGeracao())).append("\\n");
        cabecalho.append("                </div>\\n");
        cabecalho.append("                <div class=\"info-item\">\\n");
        cabecalho.append("                    <strong>Status:</strong> <span class=\"status ").append(relatorio.getStatus().toLowerCase()).append("\">")
                .append(escapeHtml(relatorio.getStatus())).append("</span>\\n");
        cabecalho.append("                </div>\\n");
        cabecalho.append("                <div class=\"info-item\">\\n");
        cabecalho.append("                    <strong>Tipo:</strong> ").append(escapeHtml(relatorio.getTipoRelatorio())).append("\\n");
        cabecalho.append("                </div>\\n");
        
        if (relatorio.getDescricao() != null && !relatorio.getDescricao().trim().isEmpty()) {
            cabecalho.append("                <div class=\"info-item full-width\">\\n");
            cabecalho.append("                    <strong>Descrição:</strong> ").append(escapeHtml(relatorio.getDescricao())).append("\\n");
            cabecalho.append("                </div>\\n");
        }
        
        cabecalho.append("            </div>\\n");
        cabecalho.append("        </section>\\n");
        
        return cabecalho.toString();
    }
    
    @Override
    protected String gerarCorpo(Relatorio relatorio) {
        StringBuilder corpo = new StringBuilder();
        corpo.append("        <main class=\"content-section\">\\n");
        corpo.append("            <h3>Conteúdo do Relatório</h3>\\n");
        corpo.append("            <div class=\"content-body\">\\n");
        
        String conteudo = relatorio.getConteudo();
        
        // Processa o conteúdo para formato HTML
        if (conteudo.contains("##")) {
            // Conteúdo com seções marcadas
            String[] secoes = conteudo.split("##");
            for (int i = 1; i < secoes.length; i++) {
                String secao = secoes[i].trim();
                if (!secao.isEmpty()) {
                    String[] linhas = secao.split("\\n", 2);
                    String titulo = linhas[0].trim();
                    String texto = linhas.length > 1 ? linhas[1].trim() : "";
                    
                    corpo.append("                <div class=\"content-section\">\\n");
                    corpo.append("                    <h4>").append(escapeHtml(titulo)).append("</h4>\\n");
                    corpo.append("                    <div class=\"section-content\">\\n");
                    corpo.append("                        ").append(processarTextoParaHTML(texto)).append("\\n");
                    corpo.append("                    </div>\\n");
                    corpo.append("                </div>\\n");
                }
            }
        } else {
            // Conteúdo simples
            corpo.append("                <div class=\"simple-content\">\\n");
            corpo.append("                    ").append(processarTextoParaHTML(conteudo)).append("\\n");
            corpo.append("                </div>\\n");
        }
        
        corpo.append("            </div>\\n");
        corpo.append("        </main>\\n");
        
        return corpo.toString();
    }
    
    @Override
    protected String gerarRodape(Relatorio relatorio) {
        StringBuilder rodape = new StringBuilder();
        rodape.append("        <footer class=\"footer\">\\n");
        rodape.append("            <div class=\"footer-content\">\\n");
        rodape.append("                <p><strong>Sistema MedApp</strong> - Relatório gerado automaticamente</p>\\n");
        rodape.append("                <p>Data de processamento: ").append(formatarData(relatorio.getDataGeracao())).append("</p>\\n");
        rodape.append("                <p>Formato: HTML | ID: ").append(escapeHtml(relatorio.getId())).append("</p>\\n");
        rodape.append("            </div>\\n");
        rodape.append("        </footer>\\n");
        rodape.append("    </div>\\n");
        rodape.append("</body>\\n");
        rodape.append("</html>");
        
        return rodape.toString();
    }
    
    @Override
    protected String processarFormatacao(String conteudo) {
        // Para HTML, a formatação já foi aplicada nos métodos específicos
        return conteudo;
    }
    
    /**
     * Gera o CSS para estilização do relatório HTML
     * @return String contendo o CSS
     */
    private String gerarCSS() {
        return "body {\\n" +
                "    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\\n" +
                "    line-height: 1.6;\\n" +
                "    margin: 0;\\n" +
                "    padding: 20px;\\n" +
                "    background-color: #f5f5f5;\\n" +
                "}\\n" +
                ".container {\\n" +
                "    max-width: 800px;\\n" +
                "    margin: 0 auto;\\n" +
                "    background: white;\\n" +
                "    border-radius: 8px;\\n" +
                "    box-shadow: 0 2px 10px rgba(0,0,0,0.1);\\n" +
                "    overflow: hidden;\\n" +
                "}\\n" +
                ".header {\\n" +
                "    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\\n" +
                "    color: white;\\n" +
                "    padding: 30px;\\n" +
                "    text-align: center;\\n" +
                "}\\n" +
                ".header h1 {\\n" +
                "    margin: 0 0 10px 0;\\n" +
                "    font-size: 2.5em;\\n" +
                "}\\n" +
                ".header h2 {\\n" +
                "    margin: 0;\\n" +
                "    font-weight: 300;\\n" +
                "    opacity: 0.9;\\n" +
                "}\\n" +
                ".info-section, .content-section {\\n" +
                "    padding: 30px;\\n" +
                "}\\n" +
                ".info-grid {\\n" +
                "    display: grid;\\n" +
                "    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));\\n" +
                "    gap: 15px;\\n" +
                "    margin-top: 20px;\\n" +
                "}\\n" +
                ".info-item {\\n" +
                "    padding: 15px;\\n" +
                "    background: #f8f9fa;\\n" +
                "    border-left: 4px solid #667eea;\\n" +
                "    border-radius: 4px;\\n" +
                "}\\n" +
                ".info-item.full-width {\\n" +
                "    grid-column: 1 / -1;\\n" +
                "}\\n" +
                ".status {\\n" +
                "    padding: 4px 8px;\\n" +
                "    border-radius: 4px;\\n" +
                "    font-size: 0.9em;\\n" +
                "    font-weight: bold;\\n" +
                "}\\n" +
                ".status.pendente { background: #fff3cd; color: #856404; }\\n" +
                ".status.processando { background: #d1ecf1; color: #0c5460; }\\n" +
                ".status.concluido { background: #d4edda; color: #155724; }\\n" +
                ".status.erro { background: #f8d7da; color: #721c24; }\\n" +
                ".content-body {\\n" +
                "    background: #fafafa;\\n" +
                "    padding: 20px;\\n" +
                "    border-radius: 4px;\\n" +
                "    margin-top: 20px;\\n" +
                "}\\n" +
                ".content-section h4 {\\n" +
                "    color: #667eea;\\n" +
                "    border-bottom: 2px solid #667eea;\\n" +
                "    padding-bottom: 5px;\\n" +
                "}\\n" +
                ".footer {\\n" +
                "    background: #2c3e50;\\n" +
                "    color: white;\\n" +
                "    padding: 20px 30px;\\n" +
                "    text-align: center;\\n" +
                "}\\n" +
                ".footer p {\\n" +
                "    margin: 5px 0;\\n" +
                "}\\n" +
                "@media print {\\n" +
                "    body { background: white; }\\n" +
                "    .container { box-shadow: none; }\\n" +
                "}";
    }
    
    /**
     * Processa texto simples para HTML com quebras de linha e formatação básica
     * @param texto Texto a ser processado
     * @return Texto formatado em HTML
     */
    private String processarTextoParaHTML(String texto) {
        if (texto == null) return "";
        
        return escapeHtml(texto)
                .replace("\\n", "<br>\\n                        ")
                .replace("**", "<strong>")
                .replace("__", "<em>");
    }
    
    /**
     * Escapa caracteres especiais do HTML
     * @param texto Texto a ser escapado
     * @return Texto escapado
     */
    private String escapeHtml(String texto) {
        if (texto == null) return "";
        
        return texto
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
    
    /**
     * Método específico para gerar tabelas HTML
     * @param dados Array de dados para a tabela
     * @param colunas Nomes das colunas
     * @return Tabela HTML formatada
     */
    public String gerarTabelaHTML(String[][] dados, String[] colunas) {
        StringBuilder tabela = new StringBuilder();
        tabela.append("<table class=\"data-table\">\\n");
        
        // Cabeçalho da tabela
        tabela.append("    <thead>\\n");
        tabela.append("        <tr>\\n");
        for (String coluna : colunas) {
            tabela.append("            <th>").append(escapeHtml(coluna)).append("</th>\\n");
        }
        tabela.append("        </tr>\\n");
        tabela.append("    </thead>\\n");
        
        // Corpo da tabela
        tabela.append("    <tbody>\\n");
        for (String[] linha : dados) {
            tabela.append("        <tr>\\n");
            for (int i = 0; i < linha.length && i < colunas.length; i++) {
                tabela.append("            <td>").append(escapeHtml(linha[i])).append("</td>\\n");
            }
            tabela.append("        </tr>\\n");
        }
        tabela.append("    </tbody>\\n");
        tabela.append("</table>\\n");
        
        return tabela.toString();
    }
}
