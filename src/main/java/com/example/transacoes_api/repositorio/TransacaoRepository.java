package com.example.transacoesapi.repositorio;

import com.example.transacoesapi.modelo.Transacao;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Repository
public class TransacaoRepository {
    private final List<Transacao> transacoes = new CopyOnWriteArrayList<>();

    public void salvar(Transacao transacao) {
        transacoes.add(transacao);
    }

    public void limparTudo() {
        transacoes.clear();
    }

    public List<Transacao> listarTransacoesRecentes(LocalDateTime dataLimite) {
        return transacoes.stream()
                .filter(t -> t.getDataHora().isAfter(dataLimite))
                .collect(Collectors.toList());
    }

    public List<Transacao> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return transacoes.stream()
                .filter(t -> !t.getDataHora().isBefore(inicio) && !t.getDataHora().isAfter(fim))
                .collect(Collectors.toList());
    }

    public void removerPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        transacoes.removeIf(t -> !t.getDataHora().isBefore(inicio) && !t.getDataHora().isAfter(fim));
    }

    public Transacao buscarUltima() {
        if (transacoes.isEmpty()) {
            return null;
        }
        return transacoes.get(transacoes.size() - 1);
    }
}
