# MedApp - Sistema de Gerenciamento de Usuários

Sistema de gerenciamento de usuários em Java desenvolvido seguindo o padrão **MVC (Model-View-Controller)** com validação robusta e múltiplas opções de persistência.

## 📋 Descrição

O MedApp é uma aplicação Java que permite cadastrar e gerenciar usuários com validação de dados rigorosa. O sistema oferece flexibilidade na escolha do método de armazenamento (memória ou arquivo) e implementa validações completas para nomes de usuário e senhas.

## 🏗️ Arquitetura

O sistema implementa o padrão **MVC**:

- **Model** → `User` - Representa os dados do usuário
- **View** → `UserInterface` - Interface de interação com o usuário  
- **Controller** → `UserController` - Gerencia a lógica de negócio

### Estrutura de Diretórios

```
med-app-java/
├── src/main/java/com/medapp/
│   ├── Main.java                    # Ponto de entrada da aplicação
│   ├── Index.java                   # Servidor de desenvolvimento
│   ├── models/
│   │   └── User.java               # Modelo de dados do usuário
│   ├── controllers/
│   │   └── UserController.java     # Controlador principal
│   ├── views/
│   │   └── UserInterface.java      # Interface do usuário
│   ├── infra/                      # Camada de infraestrutura
│   │   ├── Repository.java         # Interface de repositório
│   │   ├── RAMRepository.java      # Implementação em memória
│   │   ├── FileRepository.java     # Implementação em arquivo
│   │   └── DBRepository.java       # Implementação para banco de dados
│   ├── use/                        # Casos de uso e validadores
│   │   ├── UsernameValidator.java  # Validação de username
│   │   └── PasswordValidator.java  # Validação de senha
│   └── utils/                      # Exceções personalizadas
│       ├── password/               # Exceções de senha
│       └── user/                   # Exceções de usuário
├── config.properties               # Configurações do sistema
├── users/                          # Diretório de armazenamento de usuários
└── pom.xml                         # Configuração Maven
```

## 🚀 Funcionalidades

### Cadastro de Usuários
- Cadastro com username, senha e email
- Validação automática de dados
- Armazenamento configurável (RAM ou arquivo)

### Validações Implementadas

#### Username
- ❌ Não pode estar vazio
- ❌ Não pode conter números
- ❌ Máximo de 12 caracteres

#### Senha
- ✅ Mínimo 8 caracteres, máximo 128
- ✅ Deve conter pelo menos 3 dos seguintes tipos:
  - Letras maiúsculas (A-Z)
  - Letras minúsculas (a-z)
  - Números (0-9)
  - Caracteres especiais (!@#$%^&*()_+-=[]{}|')
- ❌ Não pode ser igual ao username
- ❌ Não pode ser igual ao email

### Opções de Armazenamento

1. **RAMRepository**: Armazenamento em memória (volátil)
2. **FileRepository**: Armazenamento em arquivos JSON
3. **DBRepository**: Preparado para implementação com banco de dados

## ⚙️ Configuração

### Arquivo `config.properties`
```properties
# Tipo de repositório: "ram" ou "file"
tipoRepositorio=file
```

### Argumentos de Linha de Comando
```bash
java -jar med-app-java.jar file    # Usar FileRepository
java -jar med-app-java.jar ram     # Usar RAMRepository
```

## 🔧 Tecnologias Utilizadas

- **Java 17**
- **Maven** - Gerenciamento de dependências
- **Jackson** - Serialização/deserialização JSON
- **JUnit 4** - Testes unitários

### Dependências
```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.17.1</version>
</dependency>
```

## 🏃‍♂️ Como Executar

### Pré-requisitos
- Java 17 ou superior
- Maven 3.6+

### Compilação e Execução

1. **Compilar o projeto:**
```bash
cd med-app-java
mvn clean compile
```

2. **Executar a aplicação:**
```bash
# Usando Maven
mvn exec:java

# Com argumentos específicos
mvn exec:java -Dexec.args="file"
mvn exec:java -Dexec.args="ram"
```

3. **Executar testes:**
```bash
mvn test
```

4. **Gerar JAR executável:**
```bash
mvn clean package
java -jar target/med-app-java-1.0-SNAPSHOT.jar
```

## 📝 Exemplos de Uso

### Usuários Válidos
```java
// Cadastro bem-sucedido
ui.sendUserInfo("alice", "StrongPass123!", "alice@example.com");
ui.sendUserInfo("bob", "MySecure456@", "bob@example.com");
```

### Casos de Erro
```java
// Username com números
ui.sendUserInfo("alice123", "StrongPass789!", "alice123@example.com");
// Erro: "Username cannot contain numbers."

// Senha fraca
ui.sendUserInfo("carol", "weak", "carol@example.com");
// Erro: "Password must contain at least 3 of the following: uppercase letters, lowercase letters, numbers, and special characters..."

// Senha igual ao username
ui.sendUserInfo("dave", "dave", "dave@example.com");
// Erro: "Password cannot be the same as username."
```

## 📊 Estrutura de Dados

### Modelo User
```java
public class User {
    private String username;
    private String password;
    private String email;
    
    // Construtores, getters e setters
}
```

### Formato JSON (FileRepository)
```json
{
    "username": "alice",
    "password": "StrongPass123!",
    "email": "alice@example.com"
}
```

## 🐳 Executando com Docker

### Pré-requisitos Docker
- Docker 20.10+
- Docker Compose 2.0+

### Execução Rápida

**Opção 1: Script interativo**
```bash
# Execute o script na raiz do projeto
cd med-app-java
./docker-start.sh
```

**Opção 2: Docker Compose direto**
```bash
cd med-app-java

# FileRepository (dados persistidos)
docker-compose up medapp

# RAMRepository (dados em memória)
docker-compose --profile ram up medapp-ram

# Modo desenvolvimento ✅ Recompilação automática ao mudar código
docker-compose --profile dev up medapp-dev
```

**Opção 3: Docker run direto**
```bash
cd med-app-java

# Build da imagem
docker build -t medapp:latest .

# Executar com FileRepository
docker run --rm -v $(pwd)/users:/app/users medapp:latest

# Executar com RAMRepository
docker run --rm medapp:latest mvn exec:java -Dexec.args=ram
```

### Scripts Auxiliares

O projeto inclui scripts para facilitar o uso do Docker:

```bash
# Build da imagem
./scripts/docker-build.sh [tag]

# Executar aplicação
./scripts/docker-run.sh [ram|file|dev]

# Limpeza de recursos
./scripts/docker-clean.sh [--all]
```

### Configuração Docker

**Dockerfile**: Imagem baseada em OpenJDK 17 com Maven
**docker-compose.yml**: Serviços pré-configurados para diferentes cenários
**Volumes**: Persistência de dados de usuários e cache Maven

### Volumes Persistentes

- `./users:/app/users` - Arquivos de usuários (FileRepository)
- `maven-cache` - Cache de dependências Maven
- `./config.properties:/app/config.properties` - Configurações personalizadas

### Variáveis de Ambiente

```bash
JAVA_OPTS="-Xmx512m -Xms256m"    # Configurações da JVM
APP_PROFILE="docker"              # Perfil da aplicação
```

### Troubleshooting Docker

**Problema**: Permissões em sistemas Unix
```bash
# Dar permissão aos scripts
chmod +x scripts/*.sh
chmod +x docker-start.sh
```

**Problema**: Build lento
```bash
# Usar cache do Docker
docker build -t medapp:latest . --cache-from medapp:latest
```

**Problema**: Espaço em disco
```bash
# Limpeza completa
./scripts/docker-clean.sh --all
docker system prune -a
```

## 🧪 Demonstração

A aplicação inclui casos de teste automáticos que demonstram:

1. ✅ Cadastro de usuários válidos
2. ❌ Validação de username (números, tamanho, vazio)
3. ❌ Validação de senha (força, duplicação)
4. 📋 Listagem de usuários cadastrados

Todos os testes são executados automaticamente ao iniciar a aplicação.

## 🔮 Futuras Implementações

- **DBRepository**: Integração com banco de dados
- **Interface Web**: Interface gráfica para o sistema
- **API REST**: Endpoints para integração externa
- **Autenticação**: Sistema de login e sessões
- **Logs**: Sistema de auditoria e logs

## 📄 Licença

Este projeto foi desenvolvido para fins educacionais na disciplina de Metodologia de Software.

---

**Nota**: O projeto demonstra boas práticas de desenvolvimento Java, incluindo separação de responsabilidades, validação de dados, tratamento de exceções e flexibilidade arquitetural.