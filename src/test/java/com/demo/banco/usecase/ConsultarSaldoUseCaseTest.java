package com.demo.banco.usecase;

import com.demo.banco.exception.ContaNaoEncontradaException;
import com.demo.banco.model.Conta;
import com.demo.banco.repository.ContaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultarSaldoUseCaseTest {

    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private ConsultarSaldoUseCase consultarSaldoUseCase;

    @Test
    void deveRetornarSaldoQuandoContaExistir() {
        // Arrange
        UUID contaId = UUID.randomUUID();
        Conta conta = new Conta("Gabriela Rabello", "12345678900");
        conta.setSaldo(new BigDecimal("250.00"));

        when(contaRepository.findById(contaId))
                .thenReturn(Optional.of(conta));

        // Act
        BigDecimal saldo = consultarSaldoUseCase.executar(contaId);

        // Assert
        assertNotNull(saldo);
        assertEquals(new BigDecimal("250.00"), saldo);
    }

    @Test
    void deveLancarExcecaoQuandoContaNaoExistir() {
        // Arrange
        UUID contaId = UUID.randomUUID();

        when(contaRepository.findById(contaId))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                ContaNaoEncontradaException.class,
                () -> consultarSaldoUseCase.executar(contaId)
        );
    }
}

