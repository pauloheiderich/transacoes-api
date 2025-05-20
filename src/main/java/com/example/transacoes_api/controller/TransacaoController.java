package com.example.transacoes_api.controlador;

import com.example.transacoes_api.dto.EstatisticaDTO;
import com.example.transacoes_api.dto.PeriodoDTO;
import com.example.transacoes_api.dto.TransacaoDTO;
import com.example.transacoes_api.modelo.Transacao;
import com.example.transacoes_api.servico.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeParseException;

@RestController
public class TransacaoController {

    private final TransacaoService service;

    @Autowired
    public TransacaoController(TransacaoService service) {
        this.service = service;
    }

    @PostMapping("/transacao")
    public ResponseEntity<Void> receberTransacao(@Valid @RequestBody TransacaoDTO transacaoDTO) {
        try {
            if (!service.validarDataTransacao(transacaoDTO.getDataHora())) {
                return ResponseEntity.unprocessableEntity().build();
            }
            
            service.salvarTransacao(transacaoDTO.getValor(), transacaoDTO.getDataHora());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (DateTimeParseException e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @DeleteMapping("/transacao")
    public ResponseEntity<Void> limparTransacoes() {
        service.limparTransacoes();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/estatistica")
    public ResponseEntity<EstatisticaDTO> calcularEstatisticas() {
        EstatisticaDTO estatisticas = service.calcularEstatisticasRecentes();
        return ResponseEntity.ok(estatisticas);
    }

    @PostMapping("/transacao/periodo")
    public ResponseEntity<EstatisticaDTO> consultarPorPeriodo(@RequestBody PeriodoDTO periodoDTO) {
        try {
            EstatisticaDTO estatisticas = service.calcularEstatisticasPorPeriodo(
                    periodoDTO.getDataInicial(), periodoDTO.getDataFinal());
            return ResponseEntity.ok(estatisticas);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/transacao/ultima")
    public ResponseEntity<Transacao> consultarUltimaTransacao() {
        Transacao ultima = service.buscarUltimaTransacao();
        if (ultima == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ultima);
    }

    @DeleteMapping("/transacao/periodo")
    public ResponseEntity<Void> excluirPorPeriodo(@RequestBody PeriodoDTO periodoDTO) {
        try {
            boolean removeuAlgo = service.removerTransacoesPorPeriodo(
                    periodoDTO.getDataInicial(), periodoDTO.getDataFinal());
            
            if (removeuAlgo) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
