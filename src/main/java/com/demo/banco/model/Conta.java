package com.demo.banco.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Conta {
    @Id
    private UUID id;
    private String nomeTitular;
    private String cpf;
    private LocalDate dataAbertura;
    private StatusConta statusConta;
    private BigDecimal saldo;

    public Conta(String nomeTitular, String cpf) {
        this.id = UUID.randomUUID();
        this.nomeTitular = nomeTitular;
        this.cpf = cpf;
        this.saldo = BigDecimal.ZERO.setScale(2);
        this.statusConta = StatusConta.ATIVA;
        this.dataAbertura = LocalDate.now();
    }

    public Conta() {
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNomeTitular() {
        return nomeTitular;
    }

    public void setNomeTitular(String nomeTitular) {
        this.nomeTitular = nomeTitular;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDate dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public StatusConta getStatusConta() {
        return statusConta;
    }

    public void setStatusConta(StatusConta statusConta) {
        this.statusConta = statusConta;
    }
}
