package com.example.transacoes_api.controller;

import com.example.transacoes_api.dto.EstatisticaResponseDTO;
import com.example.transacoes_api.dto.PeriodoRequestDTO;
import com.example.transacoes_api.dto.RequisicaoComBancoDTO;
import com.example.transacoes_api.dto.TransacaoDTO;
import com.example.transacoes_api.dto.TransacaoResponseDTO;
import com.example.transacoes_api.service.BancoServiceFactory;
import com.example.transacoes_api.service.TransacaoService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransacaoController {

    private final BancoServiceFactory serviceFactory;

    @Autowired
    public TransacaoController(BancoServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    @PostMapping("/transacao")
    public ResponseEntity<Void> receberTransacao(@Valid @RequestBody TransacaoDTO transacaoDTO) {
        try {
            TransacaoService service = serviceFactory.getService(transacaoDTO.getBanco());
            service.registrarTransacao(transacaoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @DeleteMapping("/transacao")
    public ResponseEntity<Void> limparTransacoes(@RequestBody RequisicaoComBancoDTO dto) {
        try {
            TransacaoService service = serviceFactory.getService(dto.getBanco());
            service.limparTransacoes(dto);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/estatistica")
    public ResponseEntity<EstatisticaResponseDTO> calcularEstatisticas(@RequestBody RequisicaoComBancoDTO dto) {
        try {
            TransacaoService service = serviceFactory.getService(dto.getBanco());
            EstatisticaResponseDTO estatisticas = service.estatisticasRecentes(dto);
            return ResponseEntity.ok(estatisticas);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/transacao/periodo")
    public ResponseEntity<EstatisticaResponseDTO> consultarPorPeriodo(@RequestBody PeriodoRequestDTO periodoDTO) {
        try {
            TransacaoService service = serviceFactory.getService(periodoDTO.getBanco());
            EstatisticaResponseDTO estatisticas = service.estatisticasPorPeriodo(periodoDTO);
            return ResponseEntity.ok(estatisticas);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @GetMapping("/transacao/ultima")
    public ResponseEntity<TransacaoResponseDTO> consultarUltimaTransacao(@RequestBody RequisicaoComBancoDTO dto) {
        try {
            TransacaoService service = serviceFactory.getService(dto.getBanco());
            TransacaoResponseDTO ultima = service.consultarUltima(dto);
            if (ultima == null) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(ultima);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/transacao/periodo")
    public ResponseEntity<Void> excluirPorPeriodo(@RequestBody PeriodoRequestDTO periodoDTO) {
        try {
            TransacaoService service = serviceFactory.getService(periodoDTO.getBanco());
            service.excluirPorPeriodo(periodoDTO);
            return ResponseEntity.ok().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }
}