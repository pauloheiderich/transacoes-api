package com.example.transacoes_api.service;

import com.example.transacoes_api.dto.EstatisticaResponseDTO;
import com.example.transacoes_api.dto.PeriodoRequestDTO;
import com.example.transacoes_api.dto.RequisicaoComBancoDTO;
import com.example.transacoes_api.dto.TransacaoDTO;
import com.example.transacoes_api.dto.TransacaoResponseDTO;
import com.example.transacoes_api.dto.TransacaoUpdateDTO;
import com.example.transacoes_api.model.Transacao;
import com.example.transacoes_api.repository.TransacaoRepository;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Banco1ServiceImpl implements TransacaoService {
    private final TransacaoRepository repository;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
    
    public Banco1ServiceImpl(TransacaoRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public void registrarTransacao(TransacaoDTO dto) {
        Transacao transacao = new Transacao();
        transacao.setNome(dto.getNome());
        transacao.setCpf(dto.getCpf());
        transacao.setValor(dto.getValor());
        transacao.setDataHora(LocalDateTime.parse(dto.getDataHora(), formatter));
        transacao.setBanco("banco1");
        
        repository.save(transacao);
    }
    
    @Override
    public void limparTransacoes(RequisicaoComBancoDTO dto) {
        LocalDateTime limiteData = LocalDateTime.now().minusYears(3);
        repository.deleteByBancoAndDataHoreBefore("banco1", limiteData);
    }
    
    @Override
    public void excluirPorPeriodo(PeriodoRequestDTO dto) {
        LocalDateTime dataInicial = LocalDateTime.parse(dto.getDataInicial(), formatter);
        LocalDateTime dataFinal = LocalDateTime.parse(dto.getDataFinal(), formatter);
        repository.deleteByBancoAndDataHoraBetween("banco1", dataInicial, dataFinal);
    }
    
    @Override
    public EstatisticaResponseDTO estatisticasRecentes(RequisicaoComBancoDTO dto) {
        List<Transacao> transacoes = repository.findByBancoAndValorGreaterThan("banco1", 5.0);
        return calcularEstatisticas(transacoes);
    }
    
    @Override
    public EstatisticaResponseDTO estatisticasPorPeriodo(PeriodoRequestDTO dto) {
        LocalDateTime dataInicial = LocalDateTime.parse(dto.getDataInicial(), formatter);
        LocalDateTime dataFinal = LocalDateTime.parse(dto.getDataFinal(), formatter);
        List<Transacao> transacoes = repository.findByBancoAndDataHoraBetweenAndValorGreaterThan(
                "banco1", dataInicial, dataFinal, 5.0);
        
        return calcularEstatisticas(transacoes);
    }
    
    @Override
    public TransacaoResponseDTO consultarUltima(RequisicaoComBancoDTO dto) {
        Transacao ultima = repository.findTopByBancoOrderByDataHoraDesc("banco1");
        return transacaoToDTO(ultima);
    }
    
    @Override
    public List<TransacaoResponseDTO> buscarPorCpf(String cpf) {
        return repository.findByCpf(cpf).stream()
                .filter(t -> "banco1".equals(t.getBanco()))
                .map(this::transacaoToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TransacaoResponseDTO> buscarPorNome(String nome) {
        return repository.findByNomeContaining(nome).stream()
                .filter(t -> "banco1".equals(t.getBanco()))
                .map(this::transacaoToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public TransacaoResponseDTO atualizarTransacao(Integer id, TransacaoUpdateDTO dto) {
        Transacao transacao = repository.findById(id);
        if (transacao == null || !"banco1".equals(transacao.getBanco())) {
            return null;
        }
        
        transacao.setNome(dto.getNome());
        transacao.setCpf(dto.getCpf());
        
        return transacaoToDTO(transacao);
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