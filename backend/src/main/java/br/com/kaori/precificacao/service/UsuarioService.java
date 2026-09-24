package br.com.kaori.precificacao.service;

import br.com.kaori.precificacao.dto.UsuarioRequest;
import br.com.kaori.precificacao.dto.UsuarioResponse;
import br.com.kaori.precificacao.entity.Usuario;
import br.com.kaori.precificacao.exception.RecursoNaoEncontradoException;
import br.com.kaori.precificacao.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) { this.repository = repository; }

    @Transactional
    public UsuarioResponse criar(UsuarioRequest request) { return paraResponse(repository.save(new Usuario(request.nome(), request.email(), request.senha()))); }
    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar() { return repository.findAll().stream().map(this::paraResponse).toList(); }
    @Transactional(readOnly = true)
    public Usuario buscar(Long id) { return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Usuario nao encontrado")); }
    @Transactional
    public UsuarioResponse atualizar(Long id, UsuarioRequest request) { Usuario u = buscar(id); u.atualizar(request.nome(), request.email()); return paraResponse(u); }
    public void excluir(Long id) { repository.delete(buscar(id)); }
    private UsuarioResponse paraResponse(Usuario u) { return new UsuarioResponse(u.getId(), u.getNome(), u.getEmail()); }
}
