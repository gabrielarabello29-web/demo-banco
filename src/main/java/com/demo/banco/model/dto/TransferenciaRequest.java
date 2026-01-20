package com.demo.banco.model.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class TransferenciaRequest {
    private UUID contaDestino;
    private UUID contaOrigem;
    private BigDecimal valor;

    public UUID getContaDestino() {
        return contaDestino;
    }

    public BigDecimal getValor() {
        return valor;
    }
}
