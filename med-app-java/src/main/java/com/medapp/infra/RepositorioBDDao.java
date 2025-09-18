package com.medapp.infra;

/**
 * DAO concreto para criação de repositório de Banco de Dados
 * Implementa o padrão Factory Method para criar DBRepository
 */
public class RepositorioBDDao extends FabricaAbstrataRepositorioDAO {
    
    @Override
    public Repository criarRepositorio() {
        return new DBRepository();
    }
}
