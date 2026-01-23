package com.demo.banco.exception;

public class ContaNaoAtivaException extends RuntimeException {
    public ContaNaoAtivaException(String mensagem) {
        super(mensagem);
    }
}
