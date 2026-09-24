package br.com.kaori.precificacao.repository;

import br.com.kaori.precificacao.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> { }
