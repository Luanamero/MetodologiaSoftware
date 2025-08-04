#!/bin/bash

# Script simples para iniciar o MedApp com Docker
# Este script está na raiz para facilitar o acesso

cd med-app-java

echo "🐳 Iniciando MedApp com Docker..."
echo ""
echo "Escolha o modo de execução:"
echo "1) FileRepository (dados persistidos em arquivos)"
echo "2) RAMRepository (dados em memória)"
echo "3) Modo desenvolvimento"
echo ""
read -p "Digite sua opção (1-3) [padrão: 1]: " option

case $option in
    2)
        echo "Executando com RAMRepository..."
        ./scripts/docker-run.sh ram
        ;;
    3)
        echo "Executando em modo desenvolvimento..."
        ./scripts/docker-run.sh dev
        ;;
    *)
        echo "Executando com FileRepository..."
        ./scripts/docker-run.sh file
        ;;
esac