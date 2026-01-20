package com.demo.banco.usecase;

import com.demo.banco.exception.ContaNaoEncontradaException;
import com.demo.banco.exception.ValorTransferenciaException;
import com.demo.banco.model.Conta;
import com.demo.banco.model.TipoTransacao;
import com.demo.banco.model.Transacao;
import com.demo.banco.repository.ContaRepository;
import com.demo.banco.repository.TransacaoRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class DepositarUseCase {

    private final ContaRepository contaRepository;
    private final TransacaoRepository transacaoRepository;

    public DepositarUseCase(ContaRepository contaRepository,
                            TransacaoRepository transacaoRepository) {
        this.contaRepository = contaRepository;
        this.transacaoRepository = transacaoRepository;
    }

    public BigDecimal executar(UUID contaId, BigDecimal valor) {

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorTransferenciaException();
        }

        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(ContaNaoEncontradaException::new);

        conta.setSaldo(conta.getSaldo().add(valor));
        contaRepository.save(conta);

        Transacao transacao = new Transacao();
        transacao.setContaOrigem(contaId);
        transacao.setValor(valor);
        transacao.setTipo(TipoTransacao.DEPOSITO);
        transacao.setDataHora(LocalDateTime.now());

        transacaoRepository.save(transacao);

        return conta.getSaldo();
    }
}

