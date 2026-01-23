package com.demo.banco.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import com.demo.banco.exception.ContaNaoEncontradaException;
import com.demo.banco.exception.ValorTransferenciaException;
import com.demo.banco.model.Conta;
import com.demo.banco.model.TipoTransacao;
import com.demo.banco.model.Transacao;
import com.demo.banco.repository.ContaRepository;
import com.demo.banco.repository.TransacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DepositarUseCaseTest {

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private TransacaoRepository transacaoRepository;

    @InjectMocks
    private DepositarUseCase depositarUseCase;

    private UUID contaId;
    private Conta conta;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        contaId = UUID.randomUUID();
        conta = new Conta();
        conta.setId(contaId);
        conta.setSaldo(BigDecimal.valueOf(100));
    }

    @Test
    void deveDepositarValorPositivoComSucesso() {
        // Arrange
        when(contaRepository.findById(contaId)).thenReturn(Optional.of(conta));

        BigDecimal valorDeposito = BigDecimal.valueOf(50);

        // Act
        BigDecimal saldoFinal = depositarUseCase.executar(contaId, valorDeposito);

        // Assert
        assertEquals(BigDecimal.valueOf(150), saldoFinal);

        // Verifica se o save da conta foi chamado
        verify(contaRepository).save(conta);

        // Verifica se a transação foi criada corretamente
        ArgumentCaptor<Transacao> transacaoCaptor = ArgumentCaptor.forClass(Transacao.class);
        verify(transacaoRepository).save(transacaoCaptor.capture());

        Transacao transacaoSalva = transacaoCaptor.getValue();
        assertEquals(contaId, transacaoSalva.getContaOrigem());
        assertEquals(valorDeposito, transacaoSalva.getValor());
        assertEquals(TipoTransacao.DEPOSITO, transacaoSalva.getTipo());
        assertNotNull(transacaoSalva.getDataHora());
    }

    @Test
    void deveLancarExceptionQuandoValorMenorOuIgualZero() {
        BigDecimal valorNegativo = BigDecimal.valueOf(0);

        assertThrows(ValorTransferenciaException.class, () ->
                depositarUseCase.executar(contaId, valorNegativo)
        );

        // Nenhuma interação com os repositórios deve ocorrer
        verifyNoInteractions(contaRepository, transacaoRepository);
    }

    @Test
    void deveLancarExceptionQuandoContaNaoEncontrada() {
        when(contaRepository.findById(contaId)).thenReturn(Optional.empty());

        BigDecimal valorDeposito = BigDecimal.valueOf(50);

        assertThrows(ContaNaoEncontradaException.class, () ->
                depositarUseCase.executar(contaId, valorDeposito)
        );

        // Verifica que a contaRepository.findById foi chamada
        verify(contaRepository).findById(contaId);
        verifyNoMoreInteractions(contaRepository, transacaoRepository);
    }
}

