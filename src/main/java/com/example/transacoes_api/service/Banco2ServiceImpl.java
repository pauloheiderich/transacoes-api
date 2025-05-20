package com.example.transacoes_api.service;

import com.example.transacoes_api.dto.EstatisticaResponseDTO;
import com.example.transacoes_api.dto.PeriodoRequestDTO;
import com.example.transacoes_api.dto.RequisicaoComBancoDTO;
import com.example.transacoes_api.dto.TransacaoDTO;
import com.example.transacoes_api.dto.TransacaoResponseDTO;
import com.example.transacoes_api.modelo.Transacao;
import com.example.transacoes_api.repositorio.TransacaoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class Banco2ServiceImpl implements TransacaoService {
    private final TransacaoRepository repository;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
    
    public Banco2ServiceImpl(TransacaoRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public void registrarTransacao(TransacaoDTO dto) {
        // Banco 2 aceita apenas transações do ano de 2025
        LocalDateTime dataHora = LocalDateTime.parse(dto.getDataHora(), formatter);
        if (dataHora.getYear() != 2025) {
            throw new IllegalArgumentException("Banco 2 aceita apenas transações de 2025");
        }
        
        Transacao transacao = new Transacao();
        transacao.setNome(dto.getNome());
        transacao.setCpf(dto.getCpf());
        transacao.setValor(dto.getValor());
        transacao.setDataHora(dataHora);
        transacao.setBanco("banco2");
        
        repository.save(transacao);
    }
    
    @Override
    public void limparTransacoes(RequisicaoComBancoDTO dto) {
        repository.deleteByBanco("banco2");
    }
    
    @Override
    public void excluirPorPeriodo(PeriodoRequestDTO dto) {
        // Exige senha BD2@456
        if (!"BD2@456".equals(dto.getSenha())) {
            throw new SecurityException("Senha inválida para o Banco 2");
        }
        
        LocalDateTime dataInicial = LocalDateTime.parse(dto.getDataInicial(), formatter);
        LocalDateTime dataFinal = LocalDateTime.parse(dto.getDataFinal(), formatter);
        repository.deleteByBancoAndDataHoraBetween("banco2", dataInicial, dataFinal);
    }
    
    @Override
    public EstatisticaResponseDTO estatisticasRecentes(RequisicaoComBancoDTO dto) {
        // Ignora transações acima de R$1.000,00
        List<Transacao> transacoes = repository.findByBancoAndValorLessThan("banco2", 1000.0);
        return calcularEstatisticas(transacoes);
    }
    
    @Override
    public EstatisticaResponseDTO estatisticasPorPeriodo(PeriodoRequestDTO dto) {
        // Ignora transações acima de R$1.000,00
        LocalDateTime dataInicial = LocalDateTime.parse(dto.getDataInicial(), formatter);
        LocalDateTime dataFinal = LocalDateTime.parse(dto.getDataFinal(), formatter);
        List<Transacao> transacoes = repository.findByBancoAndDataHoraBetweenAndValorLessThan(
                "banco2", dataInicial, dataFinal, 1000.0);
        
        return calcularEstatisticas(transacoes);
    }
    
    @Override
    public TransacaoResponseDTO consultarUltima(RequisicaoComBancoDTO dto) {
        Transacao ultima = repository.findTopByBancoOrderByDataHoraDesc("banco2");
        return transacaoToDTO(ultima);
    }
    
    private EstatisticaResponseDTO calcularEstatisticas(List<Transacao> transacoes) {
        if (transacoes.isEmpty()) {
            return new EstatisticaResponseDTO();
        }

        long count = transacoes.size();
        double sum = transacoes.stream().mapToDouble(Transacao::getValor).sum();
        double avg = sum / count;
        double min = transacoes.stream().mapToDouble(Transacao::getValor).min().orElse(0);
        double max = transacoes.stream().mapToDouble(Transacao::getValor).max().orElse(0);

        return new EstatisticaResponseDTO(count, sum, avg, min, max);
    }
    
    private TransacaoResponseDTO transacaoToDTO(Transacao transacao) {
        if (transacao == null) {
            return null;
        }
        
        TransacaoResponseDTO dto = new TransacaoResponseDTO();
        dto.setId(transacao.getId());
        dto.setNome(transacao.getNome());
        dto.setCpf(transacao.getCpf());
        dto.setValor(transacao.getValor());
        dto.setDataHora(transacao.getDataHora().format(formatter));
        
        return dto;
    }
}
