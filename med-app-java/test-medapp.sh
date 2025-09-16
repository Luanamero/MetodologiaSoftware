#!/bin/bash

# Script para testar o MedApp Java sem Docker
# Uso: ./test-medapp.sh [ram|file|db]

set -e

echo "🧪 Testando MedApp Java"
echo "======================="

# Modo de repositório
REPO_TYPE=${1:-ram}

echo "📍 Modo: $REPO_TYPE"
echo "📁 Diretório: $(pwd)"
echo ""

# Verificar se o Java está disponível
if ! command -v java &> /dev/null; then
    echo "❌ Java não encontrado!"
    exit 1
fi

# Verificar se as classes estão compiladas
if [ ! -f "target/classes/com/medapp/Main.class" ]; then
    echo "❌ Classes não encontradas em target/classes/"
    echo "   Execute primeiro: mvn clean compile"
    exit 1
fi

echo "✅ Java encontrado: $(java -version 2>&1 | head -n 1)"
echo "✅ Classes compiladas encontradas"
echo ""

# Configurar classpath
CLASSPATH="target/classes"

# Adicionar dependências se existirem
if [ -d "target/dependency" ]; then
    CLASSPATH="$CLASSPATH:target/dependency/*"
fi

# Executar aplicação
echo "🚀 Iniciando aplicação..."
echo "   Repositório: $REPO_TYPE"
echo "   Classpath: $CLASSPATH"
echo ""

java -cp "$CLASSPATH" com.medapp.Main "$REPO_TYPE"
