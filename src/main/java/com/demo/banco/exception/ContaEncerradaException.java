package com.demo.banco.exception;

public class ContaEncerradaException extends RuntimeException {
    public ContaEncerradaException(String mensagem) {
        super(mensagem);
    }
}
