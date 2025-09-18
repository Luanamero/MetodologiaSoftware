package com.medapp.infra;

/**
 * DAO concreto para criação de repositório de Arquivo
 * Implementa o padrão Factory Method para criar FileRepository
 */
public class RepositorioArquivoDao extends FabricaAbstrataRepositorioDAO {
    
    @Override
    public Repository criarRepositorio() {
        return new FileRepository();
    }
}
