package com.demo.banco.model.dto;

import java.util.UUID;

public class EncerrarContaResponse {

    private UUID id;
    private String mensagem;

    public EncerrarContaResponse(UUID id, String mensagem) {
        this.id = id;
        this.mensagem = mensagem;
    }

    public UUID getId() {
        return id;
    }

    public String getMensagem() {
        return mensagem;
    }
}

