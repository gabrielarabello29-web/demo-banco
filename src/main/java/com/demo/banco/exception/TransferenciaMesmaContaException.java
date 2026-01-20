package com.demo.banco.exception;

public class TransferenciaMesmaContaException extends RuntimeException {

    public TransferenciaMesmaContaException() {
        super("Não é possível realizar transferência para a mesma conta");
    }
}
