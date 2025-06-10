package com.example.transacoes_api.dto;

public class TransacaoResponseDTO {
    private Integer id;
    private String nome;
    private String cpf;
    private Double valor;
    private String dataHora;
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getCpf() {
        return cpf;
    }
    
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    
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