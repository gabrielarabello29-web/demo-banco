package com.demo.banco.model.dto;

public class CriarContaRequest {
    private String nomeTitular;
    private String cpf;

    public String getNomeTitular() {
        return nomeTitular;
    }

    public String getCpf() {
        return cpf;
    }

    public void setNomeTitular(String nomeTitular) {
        this.nomeTitular = nomeTitular;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
