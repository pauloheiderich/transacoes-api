package com.example.transacoes_api.service;

@Component
public class BancoServiceFactory {
    private final Banco1ServiceImpl banco1Service;
    private final Banco2ServiceImpl banco2Service;
    private final Banco3ServiceImpl banco3Service;
    
    public BancoServiceFactory(Banco1ServiceImpl banco1Service, 
                              Banco2ServiceImpl banco2Service,
                              Banco3ServiceImpl banco3Service) {
        this.banco1Service = banco1Service;
        this.banco2Service = banco2Service;
        this.banco3Service = banco3Service;
    }
    
    public TransacaoService getService(String banco) {
        switch (banco) {
            case "banco1":
                return banco1Service;
            case "banco2":
                return banco2Service;
            case "banco3":
                return banco3Service;
            default:
                throw new IllegalArgumentException("Banco inválido: " + banco);
        }
    }
}