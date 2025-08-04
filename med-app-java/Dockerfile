# Use OpenJDK 17 como imagem base
FROM openjdk:17-jdk-slim

# Informações do mantenedor
LABEL maintainer="MedApp Team"
LABEL description="Sistema de Gerenciamento de Usuários - MedApp"

# Instalar Maven
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Definir diretório de trabalho
WORKDIR /app

# Copiar arquivos de configuração Maven primeiro (para cache de dependências)
COPY pom.xml .
COPY config.properties .

# Baixar dependências (essa camada será cacheada se o pom.xml não mudar)
RUN mvn dependency:go-offline -B

# Copiar código fonte
COPY src ./src

# Compilar a aplicação
RUN mvn clean compile -B

# Criar diretório para arquivos de usuários
RUN mkdir -p users

# Expor porta (se necessário para futuras implementações web)
EXPOSE 3000

# Definir variáveis de ambiente
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV APP_PROFILE="production"

# Comando padrão - executar com FileRepository
CMD ["mvn", "exec:java", "-Dexec.args=file"]