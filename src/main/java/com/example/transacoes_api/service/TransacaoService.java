package com.example.transacoes_api.service;

import com.example.transacoes_api.dto.EstatisticaResponseDTO;
import com.example.transacoes_api.dto.PeriodoRequestDTO;
import com.example.transacoes_api.dto.RequisicaoComBancoDTO;
import com.example.transacoes_api.dto.TransacaoDTO;
import com.example.transacoes_api.dto.TransacaoResponseDTO;
import com.example.transacoes_api.dto.TransacaoUpdateDTO;

import java.util.List;

public interface TransacaoService {
    void registrarTransacao(TransacaoDTO dto);
    void limparTransacoes(RequisicaoComBancoDTO dto);
    void excluirPorPeriodo(PeriodoRequestDTO dto);
    EstatisticaResponseDTO estatisticasRecentes(RequisicaoComBancoDTO dto);
    EstatisticaResponseDTO estatisticasPorPeriodo(PeriodoRequestDTO dto);
    TransacaoResponseDTO consultarUltima(RequisicaoComBancoDTO dto);
    List<TransacaoResponseDTO> buscarPorCpf(String cpf);
    List<TransacaoResponseDTO> buscarPorNome(String nome);
    TransacaoResponseDTO atualizarTransacao(Integer id, TransacaoUpdateDTO dto);
}