package com.demo.banco.usecase;

import com.demo.banco.exception.*;
import com.demo.banco.model.Conta;
import com.demo.banco.model.StatusConta;
import com.demo.banco.model.TipoTransacao;
import com.demo.banco.model.Transacao;
import com.demo.banco.repository.ContaRepository;
import com.demo.banco.repository.TransacaoRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class TransferirUseCase {

    private final ContaRepository contaRepository;
    private final TransacaoRepository transacaoRepository;

    public TransferirUseCase(ContaRepository contaRepository,
                             TransacaoRepository transacaoRepository) {
        this.contaRepository = contaRepository;
        this.transacaoRepository = transacaoRepository;
    }

    public BigDecimal executar(UUID origemId, UUID destinoId, BigDecimal valor) {

        if (origemId.equals(destinoId)) {
            throw new TransferenciaMesmaContaException();
        }

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorTransferenciaException();
        }

        Conta origem = contaRepository.findById(origemId)
                .orElseThrow(() -> new ContaNaoEncontradaException("Conta origem não encontrada"));

        Conta destino = contaRepository.findById(destinoId)
                .orElseThrow(() -> new ContaNaoEncontradaException("Conta destino não encontrada"));

        validarContasAtivas(origem, destino);

        if (origem.getSaldo().compareTo(valor) < 0) {
            throw new SaldoInsuficienteException();
        }

        origem.setSaldo(origem.getSaldo().subtract(valor));
        destino.setSaldo(destino.getSaldo().add(valor));

        contaRepository.save(origem);
        contaRepository.save(destino);

        Transacao transacao = new Transacao();
        transacao.setContaOrigem(origemId);
        transacao.setContaDestino(destinoId);
        transacao.setValor(valor);
        transacao.setTipo(TipoTransacao.TRANSFERENCIA);
        transacao.setDataHora(LocalDateTime.now());

        transacaoRepository.save(transacao);

        return origem.getSaldo();
    }

    private void validarContasAtivas(Conta origem, Conta destino) {
        if (origem.getStatusConta() != StatusConta.ATIVA) {
            throw new ContaNaoAtivaException("Conta de origem não está ativa");
        }

        if (destino.getStatusConta() != StatusConta.ATIVA) {
            throw new ContaNaoAtivaException("Conta de destino não está ativa");
        }
    }
}
