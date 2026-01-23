package com.demo.banco.controller;

import com.demo.banco.model.*;
import com.demo.banco.model.dto.*;
import com.demo.banco.usecase.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/contas")
public class ContaController {
    private final CriarContaUseCase criarContaUseCase;
    private final DepositarUseCase depositarUseCase;
    private final SacarUseCase sacarUseCase;
    private final TransferirUseCase transferirUseCase;
    private final ExtratoUseCase extratoUseCase;
    private final ConsultarSaldoUseCase consultarSaldoUseCase;
    private final EncerrarContaUseCase encerrarContaUseCase;

    public ContaController(
            CriarContaUseCase criarContaUseCase,
            DepositarUseCase depositarUseCase,
            SacarUseCase sacarUseCase,
            TransferirUseCase transferirUseCase,
            ExtratoUseCase extratoUseCase,
            ConsultarSaldoUseCase consultarSaldoUseCase,
            EncerrarContaUseCase encerrarContaUseCase) {

        this.criarContaUseCase = criarContaUseCase;
        this.depositarUseCase = depositarUseCase;
        this.sacarUseCase = sacarUseCase;
        this.transferirUseCase = transferirUseCase;
        this.extratoUseCase = extratoUseCase;
        this.consultarSaldoUseCase = consultarSaldoUseCase;
        this.encerrarContaUseCase = encerrarContaUseCase;
    }

    @PostMapping
    public ResponseEntity<Conta> criarConta(
            @RequestBody CriarContaRequest request) {

        Conta contaCriada = criarContaUseCase.executar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(contaCriada);
    }

    @PostMapping("/{id}/deposito")
    public ResponseEntity<OperacaoResponse> depositar(
            @PathVariable UUID id,
            @RequestBody ValorRequest request) {

        BigDecimal saldoAtual = depositarUseCase.executar(id, request.getValor());

        return ResponseEntity.ok(
                new OperacaoResponse(
                        "Depósito realizado com sucesso",
                        saldoAtual.toString())
        );
    }

    @PostMapping("/{id}/saque")
    public ResponseEntity<OperacaoResponse> sacar(
            @PathVariable UUID id,
            @RequestBody ValorRequest request) {

        BigDecimal saldoAtual = sacarUseCase.executar(id, request.getValor());

        return ResponseEntity.ok(
                new OperacaoResponse(
                        "Saque realizado com sucesso",
                        saldoAtual.toString()
                )
        );
    }

    @PostMapping("/{id}/transferencias")
    public ResponseEntity<OperacaoResponse> transferir(
            @PathVariable UUID id,
            @RequestBody TransferenciaRequest request) {

        BigDecimal saldoAtual = transferirUseCase.executar(
                id,
                request.getContaDestino(),
                request.getValor()
        );

        return ResponseEntity.ok(
                new OperacaoResponse(
                        "Transferência realizada com sucesso",
                        saldoAtual.toString()
                )
        );
    }

    @GetMapping("/{id}/extrato")
    public ResponseEntity<List<Transacao>> extrato(
            @PathVariable UUID id) {

        List<Transacao> transacoes = extratoUseCase.executar(id);
        return ResponseEntity.ok(transacoes);
    }

    @GetMapping("/{id}/saldo")
    public ResponseEntity<SaldoResponse> consultarSaldo(@PathVariable UUID id) {

        SaldoResponse response =
                new SaldoResponse(consultarSaldoUseCase.executar(id));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{id}/encerrar")
    public ResponseEntity<EncerrarContaResponse> encerrarConta(@PathVariable UUID id) {

        Conta conta = encerrarContaUseCase.executar(id);

        EncerrarContaResponse response =
                new EncerrarContaResponse(
                        conta.getId(),
                        "Conta encerrada com sucesso"
                );

        return ResponseEntity.ok(response);
    }
}