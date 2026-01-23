package com.demo.banco.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Arrays;

import com.demo.banco.exception.ContaNaoEncontradaException;
import com.demo.banco.model.Conta;
import com.demo.banco.model.TipoTransacao;
import com.demo.banco.model.Transacao;
import com.demo.banco.repository.ContaRepository;
import com.demo.banco.repository.TransacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ExtratoUseCaseTest {

    @Mock
    private TransacaoRepository transacaoRepository;

    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private ExtratoUseCase extratoUseCase;

    private UUID contaId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        contaId = UUID.randomUUID();
    }

    @Test
    void deveRetornarListaDeTransacoesQuandoContaExiste() {
        // Arrange
        when(contaRepository.findById(contaId)).thenReturn(Optional.of(new Conta()));

        Transacao t1 = new Transacao();
        t1.setValor(BigDecimal.valueOf(100));
        t1.setTipo(TipoTransacao.DEPOSITO);

        Transacao t2 = new Transacao();
        t2.setValor(BigDecimal.valueOf(50));
        t2.setTipo(TipoTransacao.SAQUE);

        List<Transacao> transacoes = Arrays.asList(t1, t2);
        when(transacaoRepository.findByContaOrigemOrContaDestino(contaId, contaId))
                .thenReturn(transacoes);

        // Act
        List<Transacao> resultado = extratoUseCase.executar(contaId);

        // Assert
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(t1));
        assertTrue(resultado.contains(t2));

        // Verifica interações com os repositórios
        verify(contaRepository).findById(contaId);
        verify(transacaoRepository).findByContaOrigemOrContaDestino(contaId, contaId);
    }

    @Test
    void deveLancarExceptionQuandoContaNaoExiste() {
        // Arrange
        when(contaRepository.findById(contaId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ContaNaoEncontradaException.class, () ->
                extratoUseCase.executar(contaId)
        );

        // Verifica que a transação não foi consultada
        verify(contaRepository).findById(contaId);
        verifyNoMoreInteractions(transacaoRepository);
    }
}

