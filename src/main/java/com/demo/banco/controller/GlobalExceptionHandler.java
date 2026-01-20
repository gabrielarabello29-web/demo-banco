package com.demo.banco.controller;

import com.demo.banco.exception.ContaNaoEncontradaException;
import com.demo.banco.exception.SaldoInsuficienteException;
import com.demo.banco.exception.TransferenciaMesmaContaException;
import com.demo.banco.exception.ValorTransferenciaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<String> handleSaldoInsuficiente(
            SaldoInsuficienteException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(ContaNaoEncontradaException.class)
    public ResponseEntity<String> handleContaNaoEncontrada(
            ContaNaoEncontradaException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(TransferenciaMesmaContaException.class)
    public ResponseEntity<String> handleTransferenciaMesmaConta(
            TransferenciaMesmaContaException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(ValorTransferenciaException.class)
    public ResponseEntity<String> handleValorTransferencia(
            ValorTransferenciaException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
}