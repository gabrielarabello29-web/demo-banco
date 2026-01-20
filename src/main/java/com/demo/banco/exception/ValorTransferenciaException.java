package com.demo.banco.exception;

public class ValorTransferenciaException extends RuntimeException {

    public ValorTransferenciaException() {
        super("Valor da transferência deve ser positivo");
    }
}


