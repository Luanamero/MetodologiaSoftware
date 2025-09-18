package com.medapp.infra;

/**
 * DAO concreto para criação de repositório RAM
 * Implementa o padrão Factory Method para criar RAMRepository
 */
public class RepositorioRAMDao extends FabricaAbstrataRepositorioDAO {
    
    @Override
    public Repository criarRepositorio() {
        return new RAMRepository();
    }
}
