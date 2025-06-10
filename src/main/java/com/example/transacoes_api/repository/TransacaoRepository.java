package com.example.transacoes_api.repository;

import com.example.transacoes_api.model.Transacao;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class TransacaoRepository {
    private final List<Transacao> transacoes = new CopyOnWriteArrayList<>();
    private final AtomicInteger idCounter = new AtomicInteger();
    
    public Transacao save(Transacao transacao) {
        if (transacao.getId() == null) {
            transacao.setId(idCounter.incrementAndGet());
        }
        transacoes.add(transacao);
        return transacao;
    }
    
    public Transacao findById(Integer id) {
        return transacoes.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    public List<Transacao> findByCpf(String cpf) {
        return transacoes.stream()
                .filter(t -> t.getCpf().equals(cpf))
                .collect(Collectors.toList());
    }
    
    public List<Transacao> findByNomeContaining(String nome) {
        return transacoes.stream()
                .filter(t -> t.getNome().toLowerCase().contains(nome.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    public void deleteByBanco(String banco) {
        transacoes.removeIf(t -> t.getBanco().equals(banco));
    }
    
    public void deleteByBancoAndDataHoreBefore(String banco, LocalDateTime data) {
        transacoes.removeIf(t -> t.getBanco().equals(banco) && t.getDataHora().isBefore(data));
    }
    
    public void deleteByBancoAndDataHoraBetween(String banco, LocalDateTime inicio, LocalDateTime fim) {
        transacoes.removeIf(t -> 
            t.getBanco().equals(banco) && 
            !t.getDataHora().isBefore(inicio) && 
            !t.getDataHora().isAfter(fim));
    }
    
    public List<Transacao> findByBanco(String banco) {
        return transacoes.stream()
                .filter(t -> t.getBanco().equals(banco))
                .collect(Collectors.toList());
    }
    
    public List<Transacao> findByBancoAndValorGreaterThan(String banco, Double valorMinimo) {
        return transacoes.stream()
                .filter(t -> t.getBanco().equals(banco) && t.getValor() > valorMinimo)
                .collect(Collectors.toList());
    }
    
    public List<Transacao> findByBancoAndValorLessThan(String banco, Double valorMaximo) {
        return transacoes.stream()
                .filter(t -> t.getBanco().equals(banco) && t.getValor() < valorMaximo)
                .collect(Collectors.toList());
    }
    
    public List<Transacao> findByBancoAndDataHoraBetween(String banco, LocalDateTime inicio, LocalDateTime fim) {
        return transacoes.stream()
                .filter(t -> 
                    t.getBanco().equals(banco) && 
                    !t.getDataHora().isBefore(inicio) && 
                    !t.getDataHora().isAfter(fim))
                .collect(Collectors.toList());
    }
    
    public List<Transacao> findByBancoAndDataHoraBetweenAndValorGreaterThan(
            String banco, LocalDateTime inicio, LocalDateTime fim, Double valorMinimo) {
        return transacoes.stream()
                .filter(t -> 
                    t.getBanco().equals(banco) && 
                    !t.getDataHora().isBefore(inicio) && 
                    !t.getDataHora().isAfter(fim) &&
                    t.getValor() > valorMinimo)
                .collect(Collectors.toList());
    }
    
    public List<Transacao> findByBancoAndDataHoraBetweenAndValorLessThan(
            String banco, LocalDateTime inicio, LocalDateTime fim, Double valorMaximo) {
        return transacoes.stream()
                .filter(t -> 
                    t.getBanco().equals(banco) && 
                    !t.getDataHora().isBefore(inicio) && 
                    !t.getDataHora().isAfter(fim) &&
                    t.getValor() < valorMaximo)
                .collect(Collectors.toList());
    }
    
    public Transacao findTopByBancoOrderByDataHoraDesc(String banco) {
        return transacoes.stream()
                .filter(t -> t.getBanco().equals(banco))
                .sorted((t1, t2) -> t2.getDataHora().compareTo(t1.getDataHora()))
                .findFirst()
                .orElse(null);
    }
}