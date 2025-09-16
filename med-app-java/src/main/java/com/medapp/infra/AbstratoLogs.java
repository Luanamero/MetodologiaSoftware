package com.medapp.infra;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe abstrata para sistema de logging
 * Define a interface comum para diferentes implementações de log
 */
public abstract class AbstratoLogs {
    
    protected static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    
    /**
     * Registra uma mensagem de informação
     * @param mensagem Mensagem a ser registrada
     */
    public abstract void info(String mensagem);
    
    /**
     * Registra uma mensagem de debug
     * @param mensagem Mensagem a ser registrada
     */
    public abstract void debug(String mensagem);
    
    /**
     * Registra uma mensagem de aviso
     * @param mensagem Mensagem a ser registrada
     */
    public abstract void warn(String mensagem);
    
    /**
     * Registra uma mensagem de erro
     * @param mensagem Mensagem a ser registrada
     */
    public abstract void error(String mensagem);
    
    /**
     * Registra uma mensagem de erro com exceção
     * @param mensagem Mensagem a ser registrada
     * @param exception Exceção relacionada ao erro
     */
    public abstract void error(String mensagem, Throwable exception);
    
    /**
     * Método helper para formatar mensagens com timestamp
     * @param mensagem Mensagem a ser formatada
     * @return Mensagem formatada com timestamp
     */
    protected String formatarMensagem(String mensagem) {
        return String.format("[%s] %s", 
            LocalDateTime.now().format(TIMESTAMP_FORMATTER), 
            mensagem);
    }
    
    /**
     * Método helper para formatar exceções
     * @param exception Exceção a ser formatada
     * @return String representando a exceção
     */
    protected String formatarExcecao(Throwable exception) {
        if (exception == null) {
            return "Exception: null";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Exception: ").append(exception.getClass().getSimpleName());
        sb.append(" - Message: ").append(exception.getMessage());
        
        // Adiciona informações da stack trace (limitada)
        StackTraceElement[] stack = exception.getStackTrace();
        if (stack.length > 0) {
            sb.append(" - At: ").append(stack[0].toString());
        }
        
        return sb.toString();
    }
    
    /**
     * Método para registrar eventos de sistema
     * @param evento Nome do evento
     * @param detalhes Detalhes do evento
     */
    public void registrarEvento(String evento, String detalhes) {
        info(String.format("EVENTO: %s - %s", evento, detalhes));
    }
    
    /**
     * Método para registrar performance
     * @param operacao Nome da operação
     * @param tempoMs Tempo em milissegundos
     */
    public void registrarPerformance(String operacao, long tempoMs) {
        if (tempoMs > 1000) {
            warn(String.format("PERFORMANCE: %s demorou %d ms", operacao, tempoMs));
        } else {
            debug(String.format("PERFORMANCE: %s executada em %d ms", operacao, tempoMs));
        }
    }
    
    /**
     * Método para registrar acesso de usuário
     * @param username Nome do usuário
     * @param acao Ação realizada
     */
    public void registrarAcessoUsuario(String username, String acao) {
        info(String.format("ACESSO: Usuario '%s' realizou '%s'", username, acao));
    }
    
    /**
     * Método para registrar erros de validação
     * @param campo Campo que falhou na validação
     * @param valorFornecido Valor que foi fornecido
     * @param motivoFalha Motivo da falha
     */
    public void registrarErroValidacao(String campo, String valorFornecido, String motivoFalha) {
        warn(String.format("VALIDACAO: Campo '%s' com valor '%s' falhou: %s", 
            campo, valorFornecido, motivoFalha));
    }
    
    /**
     * Método para registrar operações de repositório
     * @param tipoRepositorio Tipo do repositório (RAM, BD, ARQUIVO)
     * @param operacao Operação realizada (SAVE, LOAD, DELETE)
     * @param entidade Tipo de entidade (USER, SALA, RELATORIO)
     * @param identificador Identificador da entidade
     */
    public void registrarOperacaoRepositorio(String tipoRepositorio, String operacao, String entidade, String identificador) {
        debug(String.format("REPOSITORY: %s.%s(%s) - ID: %s", 
            tipoRepositorio, operacao, entidade, identificador));
    }
}
