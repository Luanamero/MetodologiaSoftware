#!/bin/bash

# Script para limpeza dos recursos Docker do MedApp
# Uso: ./scripts/docker-clean.sh [--all]

set -e

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}🐳 MedApp - Docker Cleanup Script${NC}"
echo "=================================="

# Verificar se deve fazer limpeza completa
if [ "$1" = "--all" ]; then
    echo -e "${YELLOW}🧹 Realizando limpeza completa...${NC}"
    
    # Parar e remover containers
    echo -e "${BLUE}🛑 Parando containers...${NC}"
    docker-compose down --remove-orphans
    
    # Remover imagens
    echo -e "${BLUE}🗑️  Removendo imagens...${NC}"
    docker rmi medapp:latest 2>/dev/null || echo "Imagem medapp:latest não encontrada"
    
    # Remover volumes
    echo -e "${BLUE}📦 Removendo volumes...${NC}"
    docker volume rm med-app-java_maven-cache 2>/dev/null || echo "Volume maven-cache não encontrado"
    
    # Limpeza geral do Docker
    echo -e "${BLUE}🧽 Limpeza geral do Docker...${NC}"
    docker system prune -f
    
    echo -e "${GREEN}✅ Limpeza completa realizada!${NC}"
else
    echo -e "${YELLOW}🧹 Realizando limpeza básica...${NC}"
    
    # Parar containers
    echo -e "${BLUE}🛑 Parando containers...${NC}"
    docker-compose down
    
    echo -e "${GREEN}✅ Containers parados!${NC}"
    echo -e "${YELLOW}💡 Para limpeza completa (imagens, volumes), use: $0 --all${NC}"
fi

echo ""
echo -e "${BLUE}📊 Status atual do Docker:${NC}"
echo "Containers:"
docker ps -a --filter "name=medapp"
echo ""
echo "Imagens:"
docker images --filter "reference=medapp"