package com.example.transacoes_api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class TransacaoDTO {
    @NotNull
    @PositiveOrZero
    private Double valor;
    
    @NotNull
    private String dataHora;

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }
}
