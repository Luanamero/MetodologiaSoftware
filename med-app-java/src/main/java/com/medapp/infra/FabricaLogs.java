package com.medapp.infra;

/**
 * Fábrica para criação de objetos de log
 * Implementa o padrão Factory para diferentes tipos de logging
 */
public class FabricaLogs {
    
    private static final String DEFAULT_LOG_TYPE = "LOG4J";
    
    /**
     * Cria uma instância de logger baseada no tipo especificado
     * @param tipo Tipo do logger (LOG4J, CONSOLE, FILE)
     * @return Instância do logger
     */
    public static AbstratoLogs criarLogger(String tipo) {
        switch (tipo.toUpperCase()) {
            case "LOG4J":
                return new AdaptadorLog4j();
            case "CONSOLE":
                return new LoggerConsole();
            case "FILE":
                return new LoggerArquivo();
            default:
                throw new IllegalArgumentException("Tipo de logger não suportado: " + tipo);
        }
    }
    
    /**
     * Cria uma instância do logger padrão
     * @return Instância do logger padrão (Log4j)
     */
    public static AbstratoLogs criarLoggerPadrao() {
        return criarLogger(DEFAULT_LOG_TYPE);
    }
    
    /**
     * Cria uma instância de logger baseada em configuração do sistema
     * @return Instância do logger configurado
     */
    public static AbstratoLogs criarLoggerConfigurado() {
        String tipoConfigurado = System.getProperty("medapp.logger.type", DEFAULT_LOG_TYPE);
        return criarLogger(tipoConfigurado);
    }
    
    /**
     * Logger simples para console (implementação interna)
     */
    private static class LoggerConsole extends AbstratoLogs {
        @Override
        public void info(String mensagem) {
            System.out.println("[INFO] " + formatarMensagem(mensagem));
        }
        
        @Override
        public void debug(String mensagem) {
            System.out.println("[DEBUG] " + formatarMensagem(mensagem));
        }
        
        @Override
        public void warn(String mensagem) {
            System.out.println("[WARN] " + formatarMensagem(mensagem));
        }
        
        @Override
        public void error(String mensagem) {
            System.err.println("[ERROR] " + formatarMensagem(mensagem));
        }
        
        @Override
        public void error(String mensagem, Throwable exception) {
            System.err.println("[ERROR] " + formatarMensagem(mensagem));
            if (exception != null) {
                exception.printStackTrace();
            }
        }
    }
    
    /**
     * Logger para arquivo (implementação interna)
     */
    private static class LoggerArquivo extends AbstratoLogs {
        private static final String LOG_FILE = "medapp.log";
        
        @Override
        public void info(String mensagem) {
            escreverNoArquivo("[INFO] " + formatarMensagem(mensagem));
        }
        
        @Override
        public void debug(String mensagem) {
            escreverNoArquivo("[DEBUG] " + formatarMensagem(mensagem));
        }
        
        @Override
        public void warn(String mensagem) {
            escreverNoArquivo("[WARN] " + formatarMensagem(mensagem));
        }
        
        @Override
        public void error(String mensagem) {
            escreverNoArquivo("[ERROR] " + formatarMensagem(mensagem));
        }
        
        @Override
        public void error(String mensagem, Throwable exception) {
            escreverNoArquivo("[ERROR] " + formatarMensagem(mensagem));
            if (exception != null) {
                escreverNoArquivo("Exception: " + exception.getMessage());
            }
        }
        
        private void escreverNoArquivo(String linha) {
            try (java.io.FileWriter writer = new java.io.FileWriter(LOG_FILE, true)) {
                writer.write(linha + "\\n");
                writer.flush();
            } catch (java.io.IOException e) {
                System.err.println("Erro ao escrever no arquivo de log: " + e.getMessage());
            }
        }
    }
}
