package br.com.kaori.precificacao.repository;

import br.com.kaori.precificacao.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> { }
