package com.demo.banco.repository;

import com.demo.banco.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface TransacaoRepository
        extends JpaRepository<Transacao, UUID> {

    List<Transacao> findByContaOrigemOrContaDestino(
            UUID contaOrigem,
            UUID contaDestino
    );
}
