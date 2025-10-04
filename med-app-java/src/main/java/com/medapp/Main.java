package com.medapp;

import com.medapp.controllers.FacadeSingleton;
import com.medapp.infra.Repository;
import com.medapp.infra.RepositoryFactory;
import com.medapp.views.UsuarioInterfaceGrafica;
import com.medapp.views.RelatorioInterfaceGrafica;
import com.medapp.views.AgendamentoInterfaceGrafica;

/**
 * Classe principal do Sistema Médico - Cirurgia Sem Fronteiras v2.0.0
 * 
 * Esta é a única classe principal do sistema que oferece duas opções de execução:
 * 1. Interface gráfica completa (padrão)
 * 2. Servidor HTTP simples (com argumento "server")
 * 
 * Seguindo a arquitetura DAO com FacadeSingleton
 */
public class Main {
    
    public static void main(String[] args) {
        // Verificar se deve executar como servidor
        if (args.length > 0 && "server".equalsIgnoreCase(args[0])) {
            startSimpleServer(args);
            return;
        }
        
        // Executar interface gráfica (comportamento padrão)
        startGUIApplication(args);
    }
    
    /**
     * Inicia o servidor HTTP simples
     */
    private static void startSimpleServer(String[] args) {
        int port = 3000;
        String portEnv = System.getenv("PORT");
        if (portEnv != null) {
            port = Integer.parseInt(portEnv);
        }
        
        // Se foi passado um segundo argumento, usar como porta
        if (args.length > 1) {
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Porta inválida: " + args[1] + ". Usando porta padrão: " + port);
            }
        }
        
        System.out.println("=== SISTEMA MÉDICO - SERVIDOR HTTP ===");
        System.out.println("🚀 Server is running at http://localhost:" + port);
        System.out.println("Para iniciar interface gráfica, execute sem o argumento 'server'");
    }
    
    /**
     * Inicia a aplicação com interface gráfica
     */
    private static void startGUIApplication(String[] args) {
        System.out.println("=== SISTEMA MÉDICO - CIRURGIA SEM FRONTEIRAS v2.0.0 ===");
        System.out.println("Inicializando interfaces gráficas do sistema\n");
        
        try {
            // Determinar tipo de repositório (padrão: RAM)
            String repoType = "ram";
            if (args.length > 0) {
                repoType = args[0];
            }
            
            Repository repository = RepositoryFactory.createRepository(new String[]{repoType});
            FacadeSingleton facade = FacadeSingleton.getInstance(repository);
            
            System.out.println("✓ Sistema inicializado com repositório " + repoType.toUpperCase());
            System.out.println("✓ Abrindo interface de usuários...");
            
            // Iniciar interface gráfica de usuários
            UsuarioInterfaceGrafica.iniciar(facade);
            
            // Aguardar um pouco e abrir interface de relatórios
            Thread.sleep(1000);
            System.out.println("✓ Abrindo interface de relatórios...");
            RelatorioInterfaceGrafica.iniciar(facade);
            
            // Aguardar um pouco e abrir interface de agendamentos
            Thread.sleep(1000);
            System.out.println("✓ Abrindo interface de agendamentos...");
            AgendamentoInterfaceGrafica.iniciar(facade);
            
            System.out.println("\n=== INTERFACES GRÁFICAS ATIVAS ===");
            System.out.println("- Interface de Usuários: Gerenciamento de usuários do sistema");
            System.out.println("- Interface de Relatórios: Criação e gerenciamento de relatórios");
            System.out.println("- Interface de Agendamentos: Gerenciamento de consultas e procedimentos");
            System.out.println("\nUse as interfaces para interagir com o sistema.");
            System.out.println("Argumentos disponíveis:");
            System.out.println("  - Repositório: ram, db, file");
            System.out.println("  - Servidor: server [porta]");
            
        } catch (Exception e) {
            System.err.println("Erro ao inicializar sistema: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
