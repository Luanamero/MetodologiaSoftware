#!/bin/bash

# Script para build da imagem Docker do MedApp
# Uso: ./scripts/docker-build.sh [tag]

set -e

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}🐳 MedApp - Docker Build Script${NC}"
echo "=================================="

# Definir tag da imagem
TAG=${1:-latest}
IMAGE_NAME="medapp:${TAG}"

echo -e "${YELLOW}📦 Building Docker image: ${IMAGE_NAME}${NC}"

# Verificar se estamos no diretório correto
if [ ! -f "Dockerfile" ]; then
    echo -e "${RED}❌ Erro: Dockerfile não encontrado!${NC}"
    echo -e "${YELLOW}💡 Execute este script do diretório med-app-java${NC}"
    exit 1
fi

# Build da imagem
echo -e "${BLUE}🔨 Iniciando build...${NC}"
docker build -t ${IMAGE_NAME} . --no-cache

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Build concluído com sucesso!${NC}"
    echo -e "${GREEN}📋 Imagem criada: ${IMAGE_NAME}${NC}"
    
    # Mostrar informações da imagem
    echo -e "${BLUE}📊 Informações da imagem:${NC}"
    docker images ${IMAGE_NAME}
    
    echo ""
    echo -e "${YELLOW}🚀 Para executar a aplicação:${NC}"
    echo "docker run --rm ${IMAGE_NAME}"
    echo ""
    echo -e "${YELLOW}🐳 Ou usar docker-compose:${NC}"
    echo "docker-compose up"
    
else
    echo -e "${RED}❌ Falha no build da imagem!${NC}"
    exit 1
fi