package br.com.kaori.precificacao.service;

import br.com.kaori.precificacao.dto.ProdutoRequest;
import br.com.kaori.precificacao.dto.ProdutoResponse;
import br.com.kaori.precificacao.entity.Categoria;
import br.com.kaori.precificacao.entity.Produto;
import br.com.kaori.precificacao.entity.Usuario;
import br.com.kaori.precificacao.exception.RecursoNaoEncontradoException;
import br.com.kaori.precificacao.repository.CategoriaRepository;
import br.com.kaori.precificacao.repository.ProdutoRepository;
import br.com.kaori.precificacao.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoService {
    private final ProdutoRepository repository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;

    public ProdutoService(ProdutoRepository repository, CategoriaRepository categoriaRepository, UsuarioRepository usuarioRepository) {
        this.repository = repository; this.categoriaRepository = categoriaRepository; this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public ProdutoResponse criar(ProdutoRequest request) {
        return paraResponse(repository.save(new Produto(request.nome(), request.descricao(), request.custoCompra(), request.estoque(), categoria(request.categoriaId()), usuario(request.usuarioId()))));
    }

    @Transactional(readOnly = true)
    public Page<ProdutoResponse> listar(String nome, Long categoriaId, Pageable pageable) {
        Page<Produto> pagina;
        if (nome != null && categoriaId != null) pagina = repository.findByNomeContainingIgnoreCaseAndCategoriaId(nome, categoriaId, pageable);
        else if (nome != null) pagina = repository.findByNomeContainingIgnoreCase(nome, pageable);
        else if (categoriaId != null) pagina = repository.findByCategoriaId(categoriaId, pageable);
        else pagina = repository.findAll(pageable);
        return pagina.map(this::paraResponse);
    }

    @Transactional(readOnly = true)
    public Produto buscar(Long id) { return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Produto nao encontrado")); }

    @Transactional
    public ProdutoResponse atualizar(Long id, ProdutoRequest request) {
        Produto produto = buscar(id);
        produto.atualizar(request.nome(), request.descricao(), request.custoCompra(), request.estoque(), categoria(request.categoriaId()));
        return paraResponse(produto);
    }

    public void excluir(Long id) { repository.delete(buscar(id)); }
    private Categoria categoria(Long id) { return categoriaRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Categoria nao encontrada")); }
    private Usuario usuario(Long id) { return usuarioRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Usuario nao encontrado")); }
    private ProdutoResponse paraResponse(Produto p) { return new ProdutoResponse(p.getId(), p.getNome(), p.getDescricao(), p.getCustoCompra(), p.getEstoque(), p.getCategoria().getId(), p.getCategoria().getNome(), p.getUsuario().getId()); }
}
