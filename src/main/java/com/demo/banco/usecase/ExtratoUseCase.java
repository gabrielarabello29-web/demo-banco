package com.demo.banco.usecase;

import com.demo.banco.exception.ContaNaoAtivaException;
import com.demo.banco.exception.ContaNaoEncontradaException;
import com.demo.banco.model.Conta;
import com.demo.banco.model.StatusConta;
import com.demo.banco.model.Transacao;
import com.demo.banco.repository.ContaRepository;
import com.demo.banco.repository.TransacaoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ExtratoUseCase {

    private final TransacaoRepository transacaoRepository;
    private final ContaRepository contaRepository;

    public ExtratoUseCase(TransacaoRepository transacaoRepository,
                          ContaRepository contaRepository) {
        this.transacaoRepository = transacaoRepository;
        this.contaRepository = contaRepository;
    }

    public List<Transacao> executar(UUID contaId) {

        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(ContaNaoEncontradaException::new);

        if (conta.getStatusConta() != StatusConta.ATIVA) {
            throw new ContaNaoAtivaException("Conta não está ativa");
        }

        return transacaoRepository
                .findByContaOrigemOrContaDestino(contaId, contaId);
    }
}

