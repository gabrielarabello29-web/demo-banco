package com.demo.banco.model.dto;

import java.time.LocalDateTime;

public class OperacaoResponse {
    private String mensagem;
    private String saldoAtual;
    private LocalDateTime dataHora;

    public OperacaoResponse(String mensagem, String saldoAtual) {
        this.mensagem = mensagem;
        this.saldoAtual = saldoAtual;
        this.dataHora = LocalDateTime.now();
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getSaldoAtual() {
        return saldoAtual;
    }

    public void setSaldoAtual(String saldoAtual) {
        this.saldoAtual = saldoAtual;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
