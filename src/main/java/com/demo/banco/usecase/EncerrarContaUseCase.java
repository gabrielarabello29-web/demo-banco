package com.demo.banco.usecase;

import com.demo.banco.exception.ContaEncerradaException;
import com.demo.banco.exception.ContaNaoEncontradaException;
import com.demo.banco.model.Conta;
import com.demo.banco.model.StatusConta;
import com.demo.banco.repository.ContaRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EncerrarContaUseCase {

    private final ContaRepository contaRepository;

    public EncerrarContaUseCase(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    public Conta executar(UUID contaId) {

        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(ContaNaoEncontradaException::new);

        if (conta.getStatusConta() == StatusConta.ENCERRADA) {
            throw new ContaEncerradaException("Conta já está encerrada");
        }

        conta.setStatusConta(StatusConta.ENCERRADA);

        contaRepository.save(conta);

        return conta;
    }
}
