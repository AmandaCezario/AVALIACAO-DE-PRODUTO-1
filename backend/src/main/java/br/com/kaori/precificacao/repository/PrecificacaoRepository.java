package br.com.kaori.precificacao.repository;

import br.com.kaori.precificacao.entity.Precificacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrecificacaoRepository extends JpaRepository<Precificacao, Long> {
    Page<Precificacao> findByProdutoNomeContainingIgnoreCase(String nome, Pageable pageable);
}
