package com.demo.banco.usecase;

import com.demo.banco.model.dto.CriarContaRequest;
import com.demo.banco.model.Conta;
import com.demo.banco.model.StatusConta;
import com.demo.banco.repository.ContaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CriarContaUseCaseTest {

    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private CriarContaUseCase criarContaUseCase;

    @Test
    void deveCriarContaComValoresIniciaisCorretos() {

        // Arrange
        CriarContaRequest request = new CriarContaRequest();
        request.setNomeTitular("Gabriela Rabello");
        request.setCpf("12345678900");

        // Act
        Conta conta = criarContaUseCase.executar(request);

        // Assert
        assertNotNull(conta);
        assertNotNull(conta.getId());

        assertEquals("Gabriela Rabello", conta.getNomeTitular());
        assertEquals("12345678900", conta.getCpf());

        assertEquals(new BigDecimal("0.00"), conta.getSaldo());
        assertEquals(StatusConta.ATIVA, conta.getStatusConta());
        assertNotNull(conta.getDataAbertura());

        verify(contaRepository).save(conta);
    }
}

