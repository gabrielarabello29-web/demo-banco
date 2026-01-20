package com.demo.banco.usecase;

import com.demo.banco.exception.ContaNaoEncontradaException;
import com.demo.banco.model.Conta;
import com.demo.banco.repository.ContaRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class ConsultarSaldoUseCase {
    private final ContaRepository contaRepository;

    public ConsultarSaldoUseCase(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    public BigDecimal executar(UUID contaId) {
        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(ContaNaoEncontradaException::new);

        return conta.getSaldo();
    }
}
