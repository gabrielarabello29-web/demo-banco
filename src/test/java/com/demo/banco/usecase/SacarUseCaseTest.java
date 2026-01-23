package com.demo.banco.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.demo.banco.exception.ContaNaoEncontradaException;
import com.demo.banco.exception.SaldoInsuficienteException;
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

public class SacarUseCaseTest {

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private TransacaoRepository transacaoRepository;

    @InjectMocks
    private SacarUseCase sacarUseCase;

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
    void deveSacarValorComSucesso() {
        // Arrange
        when(contaRepository.findById(contaId)).thenReturn(Optional.of(conta));
        BigDecimal valorSaque = BigDecimal.valueOf(50);

        // Act
        BigDecimal saldoFinal = sacarUseCase.executar(contaId, valorSaque);

        // Assert
        assertEquals(BigDecimal.valueOf(50), saldoFinal);

        // Verifica se o save da conta foi chamado
        verify(contaRepository).save(conta);

        // Verifica se a transação foi criada corretamente
        ArgumentCaptor<Transacao> transacaoCaptor = ArgumentCaptor.forClass(Transacao.class);
        verify(transacaoRepository).save(transacaoCaptor.capture());

        Transacao transacaoSalva = transacaoCaptor.getValue();
        assertEquals(contaId, transacaoSalva.getContaOrigem());
        assertEquals(valorSaque, transacaoSalva.getValor());
        assertEquals(TipoTransacao.SAQUE, transacaoSalva.getTipo());
        assertNotNull(transacaoSalva.getDataHora());
    }

    @Test
    void deveLancarExceptionQuandoValorMenorOuIgualZero() {
        BigDecimal valorNegativo = BigDecimal.valueOf(0);

        assertThrows(ValorTransferenciaException.class, () ->
                sacarUseCase.executar(contaId, valorNegativo)
        );

        // Nenhuma interação com os repositórios deve ocorrer
        verifyNoInteractions(contaRepository, transacaoRepository);
    }

    @Test
    void deveLancarExceptionQuandoContaNaoEncontrada() {
        when(contaRepository.findById(contaId)).thenReturn(Optional.empty());
        BigDecimal valorSaque = BigDecimal.valueOf(50);

        assertThrows(ContaNaoEncontradaException.class, () ->
                sacarUseCase.executar(contaId, valorSaque)
        );

        verify(contaRepository).findById(contaId);
        verifyNoMoreInteractions(contaRepository, transacaoRepository);
    }

    @Test
    void deveLancarExceptionQuandoSaldoInsuficiente() {
        when(contaRepository.findById(contaId)).thenReturn(Optional.of(conta));
        BigDecimal valorSaque = BigDecimal.valueOf(200); // maior que o saldo

        assertThrows(SaldoInsuficienteException.class, () ->
                sacarUseCase.executar(contaId, valorSaque)
        );

        verify(contaRepository).findById(contaId);
        verifyNoMoreInteractions(contaRepository, transacaoRepository);
    }
}

