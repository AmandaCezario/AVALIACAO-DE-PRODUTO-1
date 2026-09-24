package br.com.kaori.precificacao.repository;

import br.com.kaori.precificacao.entity.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Page<Produto> findByNomeContainingIgnoreCaseAndCategoriaId(String nome, Long categoriaId, Pageable pageable);
    Page<Produto> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
    Page<Produto> findByCategoriaId(Long categoriaId, Pageable pageable);
}
