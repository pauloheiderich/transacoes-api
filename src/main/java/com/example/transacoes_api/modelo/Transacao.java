package com.example.transacoesapi.modelo;

import java.time.LocalDateTime;

public class Transacao {
    private Double valor;
    private LocalDateTime dataHora;

    public Transacao() {
    }

    public Transacao(Double valor, LocalDateTime dataHora) {
        this.valor = valor;
        this.dataHora = dataHora;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
