package com.medapp.infra;

/**
 * Adaptador para Log4j que implementa o padrão Adapter
 * Conecta a interface AbstratoLogs com a biblioteca Log4j
 */
public class AdaptadorLog4j extends AbstratoLogs {
    
    // Simulação da interface Log4j (já que pode não estar disponível)
    private Log4jLoggerInterface logger;
    
    public AdaptadorLog4j() {
        // Inicializa o logger Log4j
        try {
            this.logger = new Log4jLoggerImpl("MedAppLogger");
        } catch (Exception e) {
            // Fallback para console se Log4j não estiver disponível
            System.err.println("Log4j não disponível, usando fallback para console: " + e.getMessage());
            this.logger = new Log4jLoggerFallback();
        }
    }
    
    @Override
    public void info(String mensagem) {
        logger.info(formatarMensagem(mensagem));
    }
    
    @Override
    public void debug(String mensagem) {
        logger.debug(formatarMensagem(mensagem));
    }
    
    @Override
    public void warn(String mensagem) {
        logger.warn(formatarMensagem(mensagem));
    }
    
    @Override
    public void error(String mensagem) {
        logger.error(formatarMensagem(mensagem));
    }
    
    @Override
    public void error(String mensagem, Throwable exception) {
        logger.error(formatarMensagem(mensagem), exception);
    }
    
    /**
     * Interface interna para simular Log4j Logger
     */
    private interface Log4jLoggerInterface {
        void info(String message);
        void debug(String message);
        void warn(String message);
        void error(String message);
        void error(String message, Throwable throwable);
    }
    
    /**
     * Implementação real do Log4j (seria substituída pela biblioteca real)
     */
    private static class Log4jLoggerImpl implements Log4jLoggerInterface {
        private final String loggerName;
        
        public Log4jLoggerImpl(String name) {
            this.loggerName = name;
            // Aqui seria inicializado o logger real do Log4j
            // Logger.getLogger(name) ou LoggerFactory.getLogger(name)
        }
        
        @Override
        public void info(String message) {
            // log4j.info(message);
            System.out.println("[LOG4J-INFO] " + loggerName + ": " + message);
        }
        
        @Override
        public void debug(String message) {
            // log4j.debug(message);
            System.out.println("[LOG4J-DEBUG] " + loggerName + ": " + message);
        }
        
        @Override
        public void warn(String message) {
            // log4j.warn(message);
            System.out.println("[LOG4J-WARN] " + loggerName + ": " + message);
        }
        
        @Override
        public void error(String message) {
            // log4j.error(message);
            System.err.println("[LOG4J-ERROR] " + loggerName + ": " + message);
        }
        
        @Override
        public void error(String message, Throwable throwable) {
            // log4j.error(message, throwable);
            System.err.println("[LOG4J-ERROR] " + loggerName + ": " + message);
            if (throwable != null) {
                System.err.println("Exception details: " + formatarExcecaoDetalhada(throwable));
            }
        }
        
        private String formatarExcecaoDetalhada(Throwable throwable) {
            StringBuilder sb = new StringBuilder();
            sb.append(throwable.getClass().getSimpleName());
            sb.append(": ").append(throwable.getMessage());
            
            StackTraceElement[] stackTrace = throwable.getStackTrace();
            for (int i = 0; i < Math.min(3, stackTrace.length); i++) {
                sb.append("\\n\\tat ").append(stackTrace[i].toString());
            }
            
            if (stackTrace.length > 3) {
                sb.append("\\n\\t... ").append(stackTrace.length - 3).append(" more");
            }
            
            return sb.toString();
        }
    }
    
    /**
     * Implementação fallback quando Log4j não está disponível
     */
    private static class Log4jLoggerFallback implements Log4jLoggerInterface {
        
        @Override
        public void info(String message) {
            System.out.println("[FALLBACK-INFO] " + message);
        }
        
        @Override
        public void debug(String message) {
            System.out.println("[FALLBACK-DEBUG] " + message);
        }
        
        @Override
        public void warn(String message) {
            System.out.println("[FALLBACK-WARN] " + message);
        }
        
        @Override
        public void error(String message) {
            System.err.println("[FALLBACK-ERROR] " + message);
        }
        
        @Override
        public void error(String message, Throwable throwable) {
            System.err.println("[FALLBACK-ERROR] " + message);
            if (throwable != null) {
                throwable.printStackTrace();
            }
        }
    }
    
    /**
     * Método para configurar o nível de log do Log4j
     * @param nivel Nível do log (DEBUG, INFO, WARN, ERROR)
     */
    public void configurarNivelLog(String nivel) {
        // Aqui seria configurado o nível do Log4j real
        // Logger.getRootLogger().setLevel(Level.toLevel(nivel));
        System.out.println("[LOG4J-CONFIG] Nível de log configurado para: " + nivel);
    }
    
    /**
     * Método para configurar arquivo de saída do Log4j
     * @param caminhoArquivo Caminho para o arquivo de log
     */
    public void configurarArquivoSaida(String caminhoArquivo) {
        // Aqui seria configurado o appender de arquivo do Log4j
        // FileAppender appender = new FileAppender(layout, caminhoArquivo);
        // Logger.getRootLogger().addAppender(appender);
        System.out.println("[LOG4J-CONFIG] Arquivo de log configurado para: " + caminhoArquivo);
    }
}
