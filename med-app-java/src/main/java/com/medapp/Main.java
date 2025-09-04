package com.medapp;

import com.medapp.controllers.FacadeSingleton;
import com.medapp.infra.Repository;
import com.medapp.infra.RepositoryFactory;
import com.medapp.views.UserInterface;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {
    
    private static UserInterface ui;
    private static int testCount = 0;
    private static int passedTests = 0;
    
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--test")) {
            runTestMode(args);
        } else {
            runDemonstrationMode(args);
        }
    }
    
    
    private static void runDemonstrationMode(String[] args) {
        System.out.println("=== SISTEMA MÉDICO - CIRURGIA SEM FRONTEIRAS v2.0.0 ===");
        System.out.println("Demonstração completa das funcionalidades do sistema\n");
        
        // Demonstrar com todos os tipos de repositório
        demonstrateWithRepository("RAM", "ram");
        System.out.println("\n" + "=".repeat(60) + "\n");
        demonstrateWithRepository("BANCO DE DADOS", "db");
        System.out.println("\n" + "=".repeat(60) + "\n");
        demonstrateWithRepository("ARQUIVO", "file");
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMONSTRAÇÃO COMPLETA FINALIZADA!");
        System.out.println("Para executar testes automáticos: java com.medapp.Main --test [ram|db|file]");
    }
    
    private static void demonstrateWithRepository(String repoName, String repoType) {
        System.out.println("=== DEMONSTRAÇÃO COM REPOSITÓRIO " + repoName + " ===");
        
        try {
            Repository repository = RepositoryFactory.createRepository(new String[]{repoType});
            FacadeSingleton facade = FacadeSingleton.getInstance(repository);
            ui = new UserInterface(facade);
            
            System.out.println("✓ Sistema inicializado com repositório " + repoName);
            
            demonstrateUserManagement();
            demonstrateSalaManagement();
            demonstrateValidations();
            demonstrateSystemIntegration();
            
        } catch (Exception e) {
            System.err.println("✗ Erro na demonstração com " + repoName + ": " + e.getMessage());
        }
    }
    
    private static void demonstrateUserManagement() {
        System.out.println("\n--- GERENCIAMENTO DE USUÁRIOS ---");
        
        // Criar diferentes tipos de usuários
        System.out.println("\n1. Criando Administrador:");
        String result = ui.criarAdministrador("admin", "AdminPass123!", "admin@hospital.com", "SUPER");
        System.out.println("   → " + result);
        
        System.out.println("\n2. Criando Paciente:");
        result = ui.criarPaciente("maria", "MariaPass123!", "maria@email.com", 
                                "123.456.789-00", LocalDate.of(1990, 5, 15), 
                                "(11) 99999-9999", "Rua das Flores, 123");
        System.out.println("   → " + result);
        
        System.out.println("\n3. Criando Profissional de Saúde:");
        result = ui.criarProfissionalSaude("dr.silva", "DrSilva123!", "silva@hospital.com", 
                                         "CRM123456", "Cardiologia", "Emergência");
        System.out.println("   → " + result);
        
        System.out.println("\n4. Criando segundo Paciente:");
        result = ui.criarPaciente("joao", "JoaoPass123!", "joao@email.com", 
                                "987.654.321-00", LocalDate.of(1985, 12, 10), 
                                "(11) 88888-8888", "Av. Principal, 456");
        System.out.println("   → " + result);
    }
    
    private static void demonstrateSalaManagement() {
        System.out.println("\n--- GERENCIAMENTO DE SALAS ---");
        
        System.out.println("\n1. Listando salas disponíveis:");
        String result = ui.mostrarSalas();
        System.out.println("   → " + result);
        
        System.out.println("\n2. Agendando sala para cirurgia:");
        LocalDateTime agendamento1 = LocalDateTime.of(2025, 9, 10, 14, 30);
        result = ui.agendarSala("SALA001", agendamento1);
        System.out.println("   → " + result);
        
        System.out.println("\n3. Agendando outra sala:");
        LocalDateTime agendamento2 = LocalDateTime.of(2025, 9, 11, 10, 0);
        result = ui.agendarSala("SALA002", agendamento2);
        System.out.println("   → " + result);
        
        System.out.println("\n4. Verificando salas após agendamentos:");
        result = ui.mostrarSalas();
        System.out.println("   → " + result);
    }
    
    private static void demonstrateValidations() {
        System.out.println("\n--- DEMONSTRAÇÃO DE VALIDAÇÕES ---");
        
        System.out.println("\n1. Tentativa de username com números (deve falhar):");
        String result = ui.criarPaciente("user123", "Pass123!", "user123@email.com", 
                                       "111.111.111-11", LocalDate.of(1985, 1, 1), 
                                       "(11) 11111-1111", "Endereço teste");
        System.out.println("   → " + result);
        
        System.out.println("\n2. Tentativa de senha fraca (deve falhar):");
        result = ui.criarAdministrador("teste", "123", "teste@email.com", "NORMAL");
        System.out.println("   → " + result);
        
        System.out.println("\n3. Tentativa de email inválido (deve falhar):");
        result = ui.criarProfissionalSaude("medico", "MedicoPass123!", "email-invalido", 
                                         "CRM789", "Neurologia", "UTI");
        System.out.println("   → " + result);
        
        System.out.println("\n4. Tentativa de usuário duplicado (deve falhar):");
        result = ui.criarPaciente("maria", "NovaPass123!", "maria2@email.com", 
                                "999.999.999-99", LocalDate.of(1995, 3, 20), 
                                "(11) 77777-7777", "Nova rua, 789");
        System.out.println("   → " + result);
    }
    
    private static void demonstrateSystemIntegration() {
        System.out.println("\n--- INTEGRAÇÃO E CONSULTAS DO SISTEMA ---");
        
        System.out.println("\n1. Listando todos os usuários cadastrados:");
        String result = ui.mostrarListaUsuarios();
        System.out.println("   → " + result);
        
        System.out.println("\n2. Informações detalhadas do usuário 'maria':");
        result = ui.mostrarInformacoesUsuario("maria");
        System.out.println("   → " + result);
        
        System.out.println("\n3. Informações detalhadas do usuário 'dr.silva':");
        result = ui.mostrarInformacoesUsuario("dr.silva");
        System.out.println("   → " + result);
        
        System.out.println("\n4. Status completo do sistema:");
        result = ui.mostrarStatusCompleto();
        System.out.println("   → " + result);
        
        System.out.println("\n5. Tentativa de buscar usuário inexistente:");
        result = ui.mostrarInformacoesUsuario("usuario_inexistente");
        System.out.println("   → " + result);
    }
    
    private static void runTestMode(String[] args) {
        System.out.println("=== MODO DE TESTES AUTOMÁTICOS ===");
        
        String repositoryType = args.length > 1 ? args[1] : "ram";
        System.out.println("Tipo de repositório para testes: " + repositoryType.toUpperCase());
        
        initializeSystemForTests(repositoryType);
        runAllTests();
        printTestSummary();
    }
    
    private static void initializeSystemForTests(String repositoryType) {
        try {
            Repository repository = RepositoryFactory.createRepository(new String[]{repositoryType});
            FacadeSingleton facade = FacadeSingleton.getInstance(repository);
            ui = new UserInterface(facade);
            System.out.println("✓ Sistema inicializado com sucesso\n");
        } catch (Exception e) {
            System.err.println("✗ Erro ao inicializar sistema: " + e.getMessage());
            System.exit(1);
        }
    }
    
    private static void runAllTests() {
        testUsuarioCreation();
        testSalaManagement();
        testValidations();
        testSystemIntegration();
    }
    
    private static void testUsuarioCreation() {
        printTestCategory("CRIAÇÃO DE USUÁRIOS");
        
        testCreateAdministrador();
        testCreatePaciente();
        testCreateProfissionalSaude();
    }
    
    private static void testCreateAdministrador() {
        String result = ui.criarAdministrador("admin", "AdminPass123!", "admin@hospital.com", "SUPER");
        assertTest("Criar Administrador", result.contains("sucesso"));
    }
    
    private static void testCreatePaciente() {
        String result = ui.criarPaciente("alice", "AlicePass123!", "alice@email.com", 
                                       "123.456.789-00", LocalDate.of(1990, 5, 15), 
                                       "(11) 99999-9999", "Rua das Flores, 123");
        assertTest("Criar Paciente", result.contains("sucesso"));
    }
    
    private static void testCreateProfissionalSaude() {
        String result = ui.criarProfissionalSaude("dr.silva", "DrSilva123!", "silva@hospital.com", 
                                                 "CRM123456", "Cardiologia", "Emergência");
        assertTest("Criar Profissional de Saúde", result.contains("sucesso"));
    }
    
    private static void testSalaManagement() {
        printTestCategory("GERENCIAMENTO DE SALAS");
        
        testShowSalas();
        testAgendarSala();
    }
    
    private static void testShowSalas() {
        String result = ui.mostrarSalas();
        assertTest("Listar Salas", result.contains("Salas registradas"));
    }
    
    private static void testAgendarSala() {
        LocalDateTime agendamento = LocalDateTime.of(2025, 9, 10, 14, 30);
        String result = ui.agendarSala("SALA001", agendamento);
        assertTest("Agendar Sala", result.contains("agendada"));
    }
    
    private static void testValidations() {
        printTestCategory("VALIDAÇÕES");
        
        testInvalidUsername();
        testWeakPassword();
        testInvalidEmail();
    }
    
    private static void testInvalidUsername() {
        String result = ui.criarPaciente("alice123", "Pass123!", "alice123@email.com", 
                                       "111.111.111-11", LocalDate.of(1985, 1, 1), 
                                       "(11) 11111-1111", "Endereço teste");
        assertTest("Validação Username com números", result.contains("Erro"));
    }
    
    private static void testWeakPassword() {
        String result = ui.criarAdministrador("teste", "123", "teste@email.com", "NORMAL");
        assertTest("Validação Password fraca", result.contains("Erro"));
    }
    
    private static void testInvalidEmail() {
        String result = ui.criarProfissionalSaude("medico", "MedicoPass123!", "email-invalido", 
                                                 "CRM789", "Neurologia", "UTI");
        assertTest("Validação Email inválido", result.contains("Erro"));
    }
    
    private static void testSystemIntegration() {
        printTestCategory("INTEGRAÇÃO DO SISTEMA");
        
        testListUsuarios();
        testShowUserInfo();
        testSystemStatus();
    }
    
    private static void testListUsuarios() {
        String result = ui.mostrarListaUsuarios();
        assertTest("Listar Usuários", result.contains("Usuários registrados"));
    }
    
    private static void testShowUserInfo() {
        String result = ui.mostrarInformacoesUsuario("alice");
        assertTest("Mostrar Informações do Usuário", result.contains("Informações do usuário"));
    }
    
    private static void testSystemStatus() {
        String result = ui.mostrarStatusCompleto();
        assertTest("Status do Sistema", result.contains("STATUS DO SISTEMA"));
    }
    
    // Métodos utilitários para testes
    private static void printTestCategory(String category) {
        System.out.println(String.format("=== %s ===", category));
    }
    
    private static void assertTest(String testName, boolean condition) {
        testCount++;
        if (condition) {
            passedTests++;
            System.out.println(String.format("✓ %s", testName));
        } else {
            System.out.println(String.format("✗ %s", testName));
        }
    }
    
    private static void printTestSummary() {
        System.out.println("\n=== RESUMO DOS TESTES ===");
        System.out.println(String.format("Total de testes: %d", testCount));
        System.out.println(String.format("Testes aprovados: %d", passedTests));
        System.out.println(String.format("Testes falharam: %d", testCount - passedTests));
        System.out.println(String.format("Taxa de sucesso: %.1f%%", 
                          (double) passedTests / testCount * 100));
        
        if (passedTests == testCount) {
            System.out.println("\nTODOS OS TESTES PASSARAM!");
        } else {
            System.out.println("\nALGUNS TESTES FALHARAM!");
        }
    }
}

