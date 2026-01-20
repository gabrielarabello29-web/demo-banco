package com.demo.banco.model;

import com.demo.banco.exception.TransferenciaMesmaContaException;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID contaOrigem;

    private UUID contaDestino;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTransacao tipo;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    public Transacao() {
    }

    public static Transacao deposito(UUID contaId, BigDecimal valor) {
        validarValor(valor);

        Transacao t = new Transacao();
        t.contaOrigem = contaId;
        t.valor = valor;
        t.tipo = TipoTransacao.DEPOSITO;
        t.dataHora = LocalDateTime.now();
        return t;
    }

    public static Transacao saque(UUID contaId, BigDecimal valor) {
        validarValor(valor);

        Transacao t = new Transacao();
        t.contaOrigem = contaId;
        t.valor = valor;
        t.tipo = TipoTransacao.SAQUE;
        t.dataHora = LocalDateTime.now();
        return t;
    }

    public static Transacao transferencia(UUID origemId, UUID destinoId, BigDecimal valor) {
        validarValor(valor);

        if (origemId.equals(destinoId)) {
            throw new TransferenciaMesmaContaException();
        }

        Transacao t = new Transacao();
        t.contaOrigem = origemId;
        t.contaDestino = destinoId;
        t.valor = valor;
        t.tipo = TipoTransacao.TRANSFERENCIA;
        t.dataHora = LocalDateTime.now();
        return t;
    }

    private static void validarValor(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor da transação deve ser positivo");
        }
    }

    public void setContaOrigem(UUID contaOrigem) {
        this.contaOrigem = contaOrigem;
    }

    public void setContaDestino(UUID contaDestino) {
        this.contaDestino = contaDestino;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public void setTipo(TipoTransacao tipo) {
        this.tipo = tipo;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getContaOrigem() {
        return contaOrigem;
    }

    public UUID getContaDestino() {
        return contaDestino;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public TipoTransacao getTipo() {
        return tipo;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }
}

