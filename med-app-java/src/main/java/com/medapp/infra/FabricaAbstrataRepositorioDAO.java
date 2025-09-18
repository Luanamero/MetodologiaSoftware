package com.medapp.infra;

/**
 * Fábrica abstrata para criação de DAOs (Data Access Objects)
 * Implementa o padrão Abstract Factory para criar diferentes tipos de repositórios
 */
public abstract class FabricaAbstrataRepositorioDAO {
    
    /**
     * Método factory para criar um repositório específico
     * @return Instância do repositório concreto
     */
    public abstract Repository criarRepositorio();
    
    /**
     * Factory method estático para obter a fábrica correta baseada no tipo
     * @param tipo Tipo do repositório (RAM, BD, ARQUIVO)
     * @return Fábrica específica para o tipo solicitado
     */
    public static FabricaAbstrataRepositorioDAO obterFabrica(String tipo) {
        switch (tipo.toUpperCase()) {
            case "RAM":
                return new RepositorioRAMDao();
            case "BD":
            case "DATABASE":
                return new RepositorioBDDao();
            case "ARQUIVO":
            case "FILE":
                return new RepositorioArquivoDao();
            default:
                throw new IllegalArgumentException("Tipo de repositório não suportado: " + tipo);
        }
    }
    
    /**
     * Método de conveniência para criar repositório diretamente
     * @param tipo Tipo do repositório
     * @return Instância do repositório
     */
    public static Repository criarRepositorioPorTipo(String tipo) {
        FabricaAbstrataRepositorioDAO fabrica = obterFabrica(tipo);
        return fabrica.criarRepositorio();
    }
}
