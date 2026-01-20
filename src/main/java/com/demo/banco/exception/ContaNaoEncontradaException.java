package com.demo.banco.exception;

public class ContaNaoEncontradaException  extends RuntimeException {

    public ContaNaoEncontradaException() {
        super("Conta não encontrada");
    }

    public ContaNaoEncontradaException(String contaNãoEncontrada) {
        super(contaNãoEncontrada);
    }
}

