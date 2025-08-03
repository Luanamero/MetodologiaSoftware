#!/bin/bash

# Script para executar o MedApp via Docker
# Uso: ./scripts/docker-run.sh [ram|file|dev]

set -e

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}🐳 MedApp - Docker Run Script${NC}"
echo "================================"

# Modo de execução
MODE=${1:-file}

case $MODE in
    "ram")
        echo -e "${YELLOW}🧠 Executando com RAMRepository (dados em memória)${NC}"
        docker-compose --profile ram up medapp-ram
        ;;
    "file")
        echo -e "${YELLOW}📁 Executando com FileRepository (dados persistidos)${NC}"
        docker-compose up medapp
        ;;
    "dev")
        echo -e "${YELLOW}🛠️  Executando em modo desenvolvimento${NC}"
        docker-compose --profile dev up medapp-dev
        ;;
    *)
        echo -e "${RED}❌ Modo inválido: $MODE${NC}"
        echo -e "${YELLOW}💡 Modos disponíveis:${NC}"
        echo "  ram  - Executar com dados em memória"
        echo "  file - Executar com dados em arquivo (padrão)"
        echo "  dev  - Executar em modo desenvolvimento"
        echo ""
        echo -e "${YELLOW}Exemplo:${NC} ./scripts/docker-run.sh ram"
        exit 1
        ;;
esac