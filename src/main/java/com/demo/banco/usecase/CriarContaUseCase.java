package com.demo.banco.usecase;

import com.demo.banco.model.Conta;
import com.demo.banco.model.dto.CriarContaRequest;
import com.demo.banco.repository.ContaRepository;
import org.springframework.stereotype.Component;

@Component
public class CriarContaUseCase {

    private final ContaRepository contaRepository;

    public CriarContaUseCase(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    public Conta executar(CriarContaRequest request) {
        Conta conta = new Conta(request.getNomeTitular(), request.getCpf());
        contaRepository.save(conta);
        return conta;
    }
}
