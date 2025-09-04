# MedApp - Sistema de Gerenciamento de Usuários

Sistema de gerenciamento de usuários em Java desenvolvido seguindo o padrão **MVC (Model-View-Controller)** com validação robusta e múltiplas opções de persistência.

## 📋 Descrição

O MedApp é uma aplicação Java que permite cadastrar e gerenciar usuários com validação de dados rigorosa. O sistema oferece flexibilidade na escolha do método de armazenamento (memória ou arquivo) e implementa validações completas para nomes de usuário e senhas.

# Cirurgia Sem Fronteiras v2.0.0

## 📋 Funcionalidades Principais

### 👥 Gerenciamento de Usuários
- **Hierarquia de Usuários**: Sistema baseado em herança com classe abstrata `User`
  - **Administrador**: Usuários com diferentes níveis de permissão (SUPER/NORMAL)
  - **Paciente**: Dados médicos completos (CPF, data nascimento, contato)
  - **Profissional de Saúde**: Informações profissionais (CRM, especialidade, departamento)

### 🏥 Gerenciamento de Salas
- **Cadastro de Salas**: Salas médicas com equipamentos e disponibilidade
- **Agendamento**: Sistema de reserva com controle de horários
- **Monitoramento**: Status em tempo real das salas disponíveis

### 🔧 Validações Robustas
- **Username**: Não permite números, caracteres especiais ou tamanhos inadequados
- **Password**: Verificação de força (maiúscula, minúscula, números, símbolos)
- **Email**: Validação de formato e domínios válidos
- **Dados Médicos**: Validação de CPF, CRM e outros dados específicos

### 💾 Múltiplas Opções de Persistência
- **RAM**: Armazenamento em memória (ideal para testes rápidos)
- **Banco de Dados**: Simulação de BD com timeouts e validações
- **Arquivo**: Persistência em arquivos binários no sistema

## 🏗️ Arquitetura e Padrões

### Design Patterns Implementados
- **Singleton**: `FacadeSingleton` para ponto único de entrada
- **Facade**: Simplificação da interface do sistema
- **Factory**: `RepositoryFactory` para criação de repositórios
- **Repository**: Abstração da camada de persistência
- **Abstract Factory**: Criação de diferentes tipos de usuários

### Princípios SOLID
- **SRP**: Cada classe tem uma responsabilidade única
- **OCP**: Extensível para novos tipos de usuários e repositórios
- **LSP**: Subclasses substituem classes pai sem quebrar funcionalidade
- **ISP**: Interfaces específicas e coesas
- **DIP**: Dependência de abstrações, não implementações concretas

### Estrutura do Projeto
```
src/main/java/com/medapp/
├── controllers/           # Controladores e lógica de negócio
│   ├── FacadeSingleton.java
│   ├── UsuarioGerenciador.java
│   ├── SalaGerenciador.java
│   └── UserController.java
├── models/               # Modelos de domínio
│   ├── User.java (abstract)
│   ├── Administrador.java
│   ├── Paciente.java
│   ├── ProfissionalSaude.java
│   └── Sala.java
├── infra/                # Infraestrutura e repositórios
│   ├── Repository.java
│   ├── RepositoryFactory.java
│   ├── RAMRepository.java
│   ├── DBRepository.java
│   └── FileRepository.java
├── use/                  # Casos de uso e validações
│   ├── UsernameValidator.java
│   └── PasswordValidator.java
├── utils/                # Exceções customizadas
│   ├── password/
│   ├── repository/
│   ├── storage/
│   └── user/
└── views/                # Interface com usuário
    └── UserInterface.java
```

## 🚀 Como Executar

### Pré-requisitos
- Java 17 ou superior
- Sistema operacional: Linux, macOS ou Windows

### Compilação
```bash
cd med-app-java
javac -cp "src/main/java" src/main/java/com/medapp/*.java src/main/java/com/medapp/*/*.java
```

### Execução

#### 1. Modo Demonstração (Padrão)
```bash
# Demonstração completa com todos os tipos de repositório
java -cp "src/main/java" com.medapp.Main

# A demonstração mostra todas as funcionalidades do sistema sequencialmente:
# - Criação de diferentes tipos de usuários
# - Gerenciamento de salas e agendamentos  
# - Validações de segurança
# - Integração e consultas do sistema
```

#### 2. Modo de Testes Automáticos
```bash
# Testes com repositório RAM (padrão)
java -cp "src/main/java" com.medapp.Main --test

# Testes com repositório específico
java -cp "src/main/java" com.medapp.Main --test ram
java -cp "src/main/java" com.medapp.Main --test db
java -cp "src/main/java" com.medapp.Main --test file
```

### Demonstração Completa das Funcionalidades

O modo padrão executa uma demonstração declarativa que mostra:

#### Gerenciamento de Usuários
- **Administrador**: Criação com nível de permissão SUPER
- **Paciente**: Cadastro com dados médicos completos (CPF, data nascimento, contato)
- **Profissional de Saúde**: Registro com CRM, especialidade e departamento

#### Gerenciamento de Salas
- **Listagem**: Visualização de todas as salas disponíveis
- **Agendamento**: Reserva de salas para procedimentos médicos
- **Monitoramento**: Status atualizado após agendamentos

#### Validações de Segurança
- **Username**: Rejeição de nomes com números
- **Password**: Verificação de força de senha
- **Email**: Validação de formato
- **Duplicação**: Prevenção de usuários duplicados

#### Integração do Sistema
- **Consultas**: Listagem e busca de usuários
- **Detalhamento**: Informações específicas por usuário
- **Status**: Visão geral completa do sistema

### Exemplo de Saída da Demonstração
```
=== SISTEMA MÉDICO - CIRURGIA SEM FRONTEIRAS v2.0.0 ===
Demonstração completa das funcionalidades do sistema

=== DEMONSTRAÇÃO COM REPOSITÓRIO RAM ===
✓ Sistema inicializado com repositório RAM

--- GERENCIAMENTO DE USUÁRIOS ---
1. Criando Administrador:
   → Administrador 'admin' criado com sucesso.

2. Criando Paciente:
   → Paciente 'maria' criado com sucesso.

3. Criando Profissional de Saúde:
   → Profissional de Saúde 'dr.silva' criado com sucesso.

--- GERENCIAMENTO DE SALAS ---
1. Listando salas disponíveis:
   → Salas registradas: SALA001, SALA002, SALA003...

2. Agendando sala para cirurgia:
   → Sala SALA001 agendada com sucesso para 2025-09-10T14:30

--- DEMONSTRAÇÃO DE VALIDAÇÕES ---
1. Tentativa de username com números (deve falhar):
   → Erro: Username não pode conter números

2. Tentativa de senha fraca (deve falhar):
   → Erro: Password muito fraca
```

## 🧪 Testes

### Testes Automáticos Incluídos

#### Criação de Usuários
- ✅ Administrador com diferentes níveis de permissão
- ✅ Paciente com dados médicos completos
- ✅ Profissional de Saúde com credenciais

#### Gerenciamento de Salas
- ✅ Listagem de salas disponíveis
- ✅ Agendamento de salas médicas
- ✅ Controle de disponibilidade

#### Validações de Segurança
- ✅ Username sem números (política de segurança)
- ✅ Password com força adequada
- ✅ Email com formato válido
- ✅ Dados médicos consistentes

#### Integração do Sistema
- ✅ Listagem de usuários
- ✅ Exibição de informações detalhadas
- ✅ Status completo do sistema

### Exemplo de Saída dos Testes
```
=== MODO DE TESTES AUTOMÁTICOS ===
Tipo de repositório para testes: RAM
✓ Sistema inicializado com sucesso

=== CRIAÇÃO DE USUÁRIOS ===
✓ Criar Administrador
✓ Criar Paciente
✓ Criar Profissional de Saúde

=== GERENCIAMENTO DE SALAS ===
✓ Listar Salas
✓ Agendar Sala

=== VALIDAÇÕES ===
✓ Validação Username com números
✓ Validação Password fraca
✓ Validação Email inválido

=== INTEGRAÇÃO DO SISTEMA ===
✓ Listar Usuários
✓ Mostrar Informações do Usuário
✓ Status do Sistema

=== RESUMO DOS TESTES ===
Total de testes: 10
Testes aprovados: 10
Testes falharam: 0
Taxa de sucesso: 100.0%

TODOS OS TESTES PASSARAM!
```

## 📊 Tipos de Repositório

### 1. RAM Repository
- **Uso**: Testes rápidos e desenvolvimento
- **Características**: Dados voláteis, alta performance
- **Ideal para**: Prototipagem e testes unitários

### 2. Database Repository (Simulado)
- **Uso**: Simulação de ambiente de produção
- **Características**: Simula timeouts, erros de conexão, validações
- **Ideal para**: Testes de robustez e tratamento de erros

### 3. File Repository
- **Uso**: Persistência local de dados
- **Características**: Arquivos binários, serialização Java
- **Ideal para**: Pequenas instalações e dados locais

## 🔧 Tratamento de Erros

O sistema possui tratamento robusto de exceções:

### Exceções de Usuário
- `EmptyUsernameException`
- `InvalidUsernameException`
- `UsernameContainsNumbersException`
- `UsernameTooLongException`

### Exceções de Password
- `EmptyPasswordException`
- `PasswordTooShortException`
- `PasswordTooLongException`
- `PasswordTooWeakException`
- `PasswordMatchesUsernameException`
- `PasswordMatchesEmailException`

### Exceções de Repositório
- `RepositoryConfigurationException`
- `RepositoryTimeoutException`
- `RepositoryUnavailableException`
- `RepositoryIntegrityException`

### Exceções de Storage
- `UserAlreadyExistsException`
- `UserNotFoundException`
- `FileStorageException`
- `StorageCorruptedException`
- `InsufficientStorageSpaceException`

## 🎯 Novidades da Versão 2.0.0

### Principais Melhorias
1. **Hierarquia de Usuários**: Substituição do sistema de usuário único por hierarquia especializada
2. **Gestão de Salas**: Sistema completo de gerenciamento de salas médicas
3. **Múltiplos Repositórios**: Suporte a RAM, BD e Arquivo
4. **Demonstração Declarativa**: Mostra todas as funcionalidades automaticamente
5. **Testes Integrados**: Bateria de testes automáticos no próprio sistema
6. **Arquitetura SOLID**: Reestruturação completa seguindo princípios de design

### Melhorias Técnicas
- **Factory Pattern**: Criação dinâmica de repositórios
- **Facade Singleton**: Ponto único de entrada thread-safe
- **Validation Layer**: Camada dedicada de validações
- **Exception Handling**: Tratamento robusto e específico de erros
- **Clean Architecture**: Separação clara de responsabilidades
- **Demonstrative Testing**: Testes declarativos que mostram funcionalidades completas

## 📝 Licença

Este projeto foi desenvolvido para fins educacionais como parte do curso de Metodologia de Desenvolvimento de Software.

---

**Desenvolvido com ❤️ para o projeto Cirurgia Sem Fronteiras**