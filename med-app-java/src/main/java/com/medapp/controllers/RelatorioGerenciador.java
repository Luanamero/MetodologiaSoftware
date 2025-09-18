package com.medapp.controllers;

import com.medapp.models.Relatorio;
import com.medapp.infra.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class RelatorioGerenciador {
    private final Repository repository;
    private final List<Relatorio> relatorios;

    public RelatorioGerenciador(Repository repository) {
        this.repository = repository;
        this.relatorios = new ArrayList<>();
        inicializarRelatorios();
    }

    private void inicializarRelatorios() {
        try {
            List<Relatorio> relatoriosRepositorio = repository.getAllRelatorios();
            if (!relatoriosRepositorio.isEmpty()) {
                relatorios.clear();
                relatorios.addAll(relatoriosRepositorio);
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar relatórios do repositório: " + e.getMessage());
        }
    }

    public boolean criarRelatorio(String titulo, String conteudo, String tipoRelatorio, String autorUsername) {
        try {
            String id = UUID.randomUUID().toString();
            Relatorio relatorio = new Relatorio(id, titulo, conteudo, tipoRelatorio, autorUsername);
            relatorio.setDataGeracao(LocalDateTime.now());
            relatorio.setStatus("PENDENTE");
            
            relatorios.add(relatorio);
            repository.saveRelatorio(relatorio);
            
            System.out.println("Relatório criado com sucesso: " + titulo);
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao criar relatório: " + e.getMessage());
            return false;
        }
    }

    public Optional<Relatorio> buscarRelatorio(String id) {
        try {
            // Buscar primeiro no repository para garantir dados atualizados
            Relatorio relatorio = repository.loadRelatorio(id);
            return Optional.ofNullable(relatorio);
        } catch (Exception e) {
            // Se não encontrar no repository, buscar na lista local
            return relatorios.stream()
                    .filter(r -> r.getId().equals(id))
                    .findFirst();
        }
    }

    public List<Relatorio> listarRelatorios() {
        try {
            // Sincronizar com o repository para garantir dados atualizados
            List<Relatorio> relatoriosRepositorio = repository.getAllRelatorios();
            relatorios.clear();
            relatorios.addAll(relatoriosRepositorio);
            return new ArrayList<>(relatorios);
        } catch (Exception e) {
            System.err.println("Erro ao listar relatórios: " + e.getMessage());
            return new ArrayList<>(relatorios); // Retorna lista local em caso de erro
        }
    }

    public List<Relatorio> listarRelatoriosPorAutor(String autorUsername) {
        return relatorios.stream()
                .filter(r -> r.getAutorUsername().equals(autorUsername))
                .collect(Collectors.toList());
    }

    public List<Relatorio> listarRelatoriosPorTipo(String tipoRelatorio) {
        return relatorios.stream()
                .filter(r -> r.getTipoRelatorio().equals(tipoRelatorio))
                .collect(Collectors.toList());
    }

    public List<Relatorio> listarRelatoriosPorStatus(String status) {
        return relatorios.stream()
                .filter(r -> r.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    public boolean atualizarRelatorio(String id, String novoTitulo, String novoConteudo) {
        Optional<Relatorio> relatorioOpt = buscarRelatorio(id);
        if (relatorioOpt.isPresent()) {
            try {
                Relatorio relatorio = relatorioOpt.get();
                relatorio.setTitulo(novoTitulo);
                relatorio.setConteudo(novoConteudo);
                relatorio.setStatus("PENDENTE"); // Reset status para reprocessamento
                
                repository.saveRelatorio(relatorio);
                System.out.println("Relatório atualizado com sucesso: " + id);
                return true;
            } catch (Exception e) {
                System.err.println("Erro ao atualizar relatório: " + e.getMessage());
                return false;
            }
        }
        System.err.println("Relatório não encontrado: " + id);
        return false;
    }

    public boolean atualizarStatusRelatorio(String id, String novoStatus) {
        Optional<Relatorio> relatorioOpt = buscarRelatorio(id);
        if (relatorioOpt.isPresent()) {
            try {
                Relatorio relatorio = relatorioOpt.get();
                relatorio.setStatus(novoStatus);
                
                repository.saveRelatorio(relatorio);
                System.out.println("Status do relatório atualizado: " + id + " -> " + novoStatus);
                return true;
            } catch (Exception e) {
                System.err.println("Erro ao atualizar status do relatório: " + e.getMessage());
                return false;
            }
        }
        System.err.println("Relatório não encontrado: " + id);
        return false;
    }

    public boolean excluirRelatorio(String id) {
        try {
            // Tentar excluir do repository primeiro
            repository.deleteRelatorio(id);
            
            // Remover da lista local também
            relatorios.removeIf(r -> r.getId().equals(id));
            
            System.out.println("Relatório excluído com sucesso: " + id);
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao excluir relatório: " + e.getMessage());
            return false;
        }
    }

    public void exibirEstatisticas() {
        System.out.println("\n=== ESTATÍSTICAS DE RELATÓRIOS ===");
        System.out.println("Total de relatórios: " + relatorios.size());
        
        long pendentes = relatorios.stream().filter(r -> "PENDENTE".equals(r.getStatus())).count();
        long processando = relatorios.stream().filter(r -> "PROCESSANDO".equals(r.getStatus())).count();
        long concluidos = relatorios.stream().filter(r -> "CONCLUIDO".equals(r.getStatus())).count();
        long erros = relatorios.stream().filter(r -> "ERRO".equals(r.getStatus())).count();
        
        System.out.println("Pendentes: " + pendentes);
        System.out.println("Processando: " + processando);
        System.out.println("Concluídos: " + concluidos);
        System.out.println("Com erro: " + erros);
        
        long pdfs = relatorios.stream().filter(r -> "PDF".equals(r.getTipoRelatorio())).count();
        long htmls = relatorios.stream().filter(r -> "HTML".equals(r.getTipoRelatorio())).count();
        
        System.out.println("Relatórios PDF: " + pdfs);
        System.out.println("Relatórios HTML: " + htmls);
    }

    public void salvarTodos() {
        try {
            for (Relatorio relatorio : relatorios) {
                repository.saveRelatorio(relatorio);
            }
            System.out.println("Todos os relatórios foram salvos no repositório.");
        } catch (Exception e) {
            System.err.println("Erro ao salvar relatórios: " + e.getMessage());
        }
    }
}
