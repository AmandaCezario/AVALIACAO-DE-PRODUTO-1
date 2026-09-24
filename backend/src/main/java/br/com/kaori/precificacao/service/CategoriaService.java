package br.com.kaori.precificacao.service;

import br.com.kaori.precificacao.dto.CategoriaRequest;
import br.com.kaori.precificacao.dto.CategoriaResponse;
import br.com.kaori.precificacao.entity.Categoria;
import br.com.kaori.precificacao.exception.RecursoNaoEncontradoException;
import br.com.kaori.precificacao.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {
    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) { this.repository = repository; }

    @Transactional
    public CategoriaResponse criar(CategoriaRequest request) {
        return paraResponse(repository.save(new Categoria(request.nome(), request.descricao())));
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listar() { return repository.findAll().stream().map(this::paraResponse).toList(); }

    @Transactional(readOnly = true)
    public Categoria buscar(Long id) { return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Categoria nao encontrada")); }

    @Transactional
    public CategoriaResponse atualizar(Long id, CategoriaRequest request) {
        Categoria categoria = buscar(id);
        categoria.atualizar(request.nome(), request.descricao());
        return paraResponse(categoria);
    }

    public void excluir(Long id) { repository.delete(buscar(id)); }

    private CategoriaResponse paraResponse(Categoria categoria) { return new CategoriaResponse(categoria.getId(), categoria.getNome(), categoria.getDescricao()); }
}
