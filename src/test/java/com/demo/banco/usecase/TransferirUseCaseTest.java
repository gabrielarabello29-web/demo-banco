package com.demo.banco.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.demo.banco.exception.ContaNaoEncontradaException;
import com.demo.banco.exception.SaldoInsuficienteException;
import com.demo.banco.exception.TransferenciaMesmaContaException;
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

public class TransferirUseCaseTest {

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private TransacaoRepository transacaoRepository;

    @InjectMocks
    private TransferirUseCase transferirUseCase;

    private UUID origemId;
    private UUID destinoId;
    private Conta origem;
    private Conta destino;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        origemId = UUID.randomUUID();
        destinoId = UUID.randomUUID();

        origem = new Conta();
        origem.setId(origemId);
        origem.setSaldo(BigDecimal.valueOf(200));

        destino = new Conta();
        destino.setId(destinoId);
        destino.setSaldo(BigDecimal.valueOf(100));
    }

    @Test
    void deveTransferirComSucesso() {
        // Arrange
        when(contaRepository.findById(origemId)).thenReturn(Optional.of(origem));
        when(contaRepository.findById(destinoId)).thenReturn(Optional.of(destino));

        BigDecimal valorTransferencia = BigDecimal.valueOf(50);

        // Act
        BigDecimal saldoOrigemFinal = transferirUseCase.executar(origemId, destinoId, valorTransferencia);

        // Assert
        assertEquals(BigDecimal.valueOf(150), saldoOrigemFinal);
        assertEquals(BigDecimal.valueOf(150), destino.getSaldo());

        // Verifica se as contas foram salvas
        verify(contaRepository).save(origem);
        verify(contaRepository).save(destino);

        // Verifica se a transação foi criada corretamente
        ArgumentCaptor<Transacao> transacaoCaptor = ArgumentCaptor.forClass(Transacao.class);
        verify(transacaoRepository).save(transacaoCaptor.capture());

        Transacao transacaoSalva = transacaoCaptor.getValue();
        assertEquals(origemId, transacaoSalva.getContaOrigem());
        assertEquals(destinoId, transacaoSalva.getContaDestino());
        assertEquals(valorTransferencia, transacaoSalva.getValor());
        assertEquals(TipoTransacao.TRANSFERENCIA, transacaoSalva.getTipo());
        assertNotNull(transacaoSalva.getDataHora());
    }

    @Test
    void deveLancarExceptionQuandoMesmaConta() {
        BigDecimal valor = BigDecimal.valueOf(50);

        assertThrows(TransferenciaMesmaContaException.class, () ->
                transferirUseCase.executar(origemId, origemId, valor)
        );

        verifyNoInteractions(contaRepository, transacaoRepository);
    }

    @Test
    void deveLancarExceptionQuandoValorInvalido() {
        BigDecimal valor = BigDecimal.ZERO;

        assertThrows(ValorTransferenciaException.class, () ->
                transferirUseCase.executar(origemId, destinoId, valor)
        );

        verifyNoInteractions(contaRepository, transacaoRepository);
    }

    @Test
    void deveLancarExceptionQuandoContaOrigemNaoEncontrada() {
        when(contaRepository.findById(origemId)).thenReturn(Optional.empty());
        BigDecimal valor = BigDecimal.valueOf(50);

        ContaNaoEncontradaException exception = assertThrows(ContaNaoEncontradaException.class, () ->
                transferirUseCase.executar(origemId, destinoId, valor)
        );

        assertTrue(exception.getMessage().contains("origem"));
        verify(contaRepository).findById(origemId);
        verifyNoMoreInteractions(contaRepository, transacaoRepository);
    }

    @Test
    void deveLancarExceptionQuandoContaDestinoNaoEncontrada() {
        when(contaRepository.findById(origemId)).thenReturn(Optional.of(origem));
        when(contaRepository.findById(destinoId)).thenReturn(Optional.empty());
        BigDecimal valor = BigDecimal.valueOf(50);

        ContaNaoEncontradaException exception = assertThrows(ContaNaoEncontradaException.class, () ->
                transferirUseCase.executar(origemId, destinoId, valor)
        );

        assertTrue(exception.getMessage().contains("destino"));
        verify(contaRepository).findById(origemId);
        verify(contaRepository).findById(destinoId);
        verifyNoMoreInteractions(contaRepository, transacaoRepository);
    }

    @Test
    void deveLancarExceptionQuandoSaldoInsuficiente() {
        when(contaRepository.findById(origemId)).thenReturn(Optional.of(origem));
        when(contaRepository.findById(destinoId)).thenReturn(Optional.of(destino));
        BigDecimal valor = BigDecimal.valueOf(500); // maior que saldo

        assertThrows(SaldoInsuficienteException.class, () ->
                transferirUseCase.executar(origemId, destinoId, valor)
        );

        verify(contaRepository).findById(origemId);
        verify(contaRepository).findById(destinoId);
        verifyNoMoreInteractions(contaRepository, transacaoRepository);
    }
}

