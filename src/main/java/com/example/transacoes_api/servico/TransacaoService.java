package com.example.transacoes_api.servico;

import com.example.transacoes_api.dto.EstatisticaDTO;
import com.example.transacoes_api.modelo.Transacao;
import com.example.transacoes_api.repositorio.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class TransacaoService {

    private final TransacaoRepository repository;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    @Autowired
    public TransacaoService(TransacaoRepository repository) {
        this.repository = repository;
    }

    public void salvarTransacao(Double valor, String dataHoraStr) {
        LocalDateTime dataHora = LocalDateTime.parse(dataHoraStr, formatter);
        Transacao transacao = new Transacao(valor, dataHora);
        repository.salvar(transacao);
    }

    public void limparTransacoes() {
        repository.limparTudo();
    }

    public EstatisticaDTO calcularEstatisticasRecentes() {
        LocalDateTime limiteInferior = LocalDateTime.now().minusSeconds(60);
        List<Transacao> transacoesRecentes = repository.listarTransacoesRecentes(limiteInferior);
        
        return calcularEstatisticas(transacoesRecentes);
    }

    public EstatisticaDTO calcularEstatisticasPorPeriodo(String dataInicialStr, String dataFinalStr) {
        LocalDateTime dataInicial = LocalDateTime.parse(dataInicialStr, formatter);
        LocalDateTime dataFinal = LocalDateTime.parse(dataFinalStr, formatter);
        
        List<Transacao> transacoesDoPeriodo = repository.buscarPorPeriodo(dataInicial, dataFinal);
        
        return calcularEstatisticas(transacoesDoPeriodo);
    }

    public boolean removerTransacoesPorPeriodo(String dataInicialStr, String dataFinalStr) {
        LocalDateTime dataInicial = LocalDateTime.parse(dataInicialStr, formatter);
        LocalDateTime dataFinal = LocalDateTime.parse(dataFinalStr, formatter);
        
        List<Transacao> transacoesDoPeriodo = repository.buscarPorPeriodo(dataInicial, dataFinal);
        if (transacoesDoPeriodo.isEmpty()) {
            return false;
        }
        
        repository.removerPorPeriodo(dataInicial, dataFinal);
        return true;
    }

    public Transacao buscarUltimaTransacao() {
        return repository.buscarUltima();
    }

    private EstatisticaDTO calcularEstatisticas(List<Transacao> transacoes) {
        if (transacoes.isEmpty()) {
            return new EstatisticaDTO();
        }

        long count = transacoes.size();
        double sum = transacoes.stream().mapToDouble(Transacao::getValor).sum();
        double avg = sum / count;
        double min = transacoes.stream().mapToDouble(Transacao::getValor).min().orElse(0);
        double max = transacoes.stream().mapToDouble(Transacao::getValor).max().orElse(0);

        return new EstatisticaDTO(count, sum, avg, min, max);
    }

    public boolean validarDataTransacao(String dataHoraStr) throws DateTimeParseException {
        LocalDateTime dataHora = LocalDateTime.parse(dataHoraStr, formatter);
        return !dataHora.isAfter(LocalDateTime.now());
    }
}
