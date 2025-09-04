package com.medapp.infra;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class RepositoryFactory {
    
    private static final String DEFAULT_REPOSITORY = "ram";
    private static final String CONFIG_FILE = "config.properties";
    private static final String PROPERTY_KEY = "tipoRepositorio";
    
    public static Repository createRepository(String[] args) {
        String repositoryType = determineRepositoryType(args);
        return createRepositoryByType(repositoryType);
    }
    
    private static String determineRepositoryType(String[] args) {
        // Primeiro, verifica argumentos da linha de comando
        if (args.length > 0) {
            return args[0].toLowerCase();
        }
        
        // Depois, verifica arquivo de configuração
        return getConfigurationProperty();
    }
    
    private static String getConfigurationProperty() {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream(CONFIG_FILE));
            return props.getProperty(PROPERTY_KEY, DEFAULT_REPOSITORY).toLowerCase();
        } catch (IOException e) {
            System.out.println("Arquivo de configuração não encontrado. Usando repositório padrão.");
            return DEFAULT_REPOSITORY;
        }
    }
    
    private static Repository createRepositoryByType(String type) {
        switch (type) {
            case "file":
                System.out.println("Usando FileRepository");
                return new FileRepository();
            case "db":
                System.out.println("Usando DBRepository");
                return new DBRepository();
            case "ram":
            default:
                System.out.println("Usando RAMRepository");
                return new RAMRepository();
        }
    }
}
