package br.com.kaori.precificacao.service;

import br.com.kaori.precificacao.dto.PrecificacaoRequest;
import br.com.kaori.precificacao.dto.PrecificacaoResponse;
import br.com.kaori.precificacao.entity.Precificacao;
import br.com.kaori.precificacao.entity.Produto;
import br.com.kaori.precificacao.exception.RecursoNaoEncontradoException;
import br.com.kaori.precificacao.repository.PrecificacaoRepository;
import br.com.kaori.precificacao.repository.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PrecificacaoService {
    private final PrecificacaoRepository repository;
    private final ProdutoRepository produtoRepository;

    public PrecificacaoService(PrecificacaoRepository repository, ProdutoRepository produtoRepository) { this.repository = repository; this.produtoRepository = produtoRepository; }

    @Transactional
    public PrecificacaoResponse calcularESalvar(PrecificacaoRequest request) {
        Produto produto = produtoRepository.findById(request.produtoId()).orElseThrow(() -> new RecursoNaoEncontradoException("Produto nao encontrado"));
        BigDecimal percentual = request.taxaCartao().add(request.taxaPlataforma()).add(request.imposto()).add(request.margemLucro()).divide(BigDecimal.valueOf(100));
        if (percentual.compareTo(BigDecimal.ONE) >= 0) throw new IllegalArgumentException("A soma das taxas e margem deve ser menor que 100%");
        BigDecimal custo = produto.getCustoCompra().add(request.frete()).add(request.embalagem());
        BigDecimal preco = custo.divide(BigDecimal.ONE.subtract(percentual), 2, RoundingMode.HALF_UP);
        BigDecimal taxas = preco.multiply(request.taxaCartao().add(request.taxaPlataforma()).add(request.imposto()).divide(BigDecimal.valueOf(100)));
        BigDecimal lucro = preco.subtract(custo).subtract(taxas).setScale(2, RoundingMode.HALF_UP);
        Precificacao precificacao = new Precificacao(produto, request.frete(), request.embalagem(), request.taxaCartao(), request.taxaPlataforma(), request.imposto(), request.margemLucro(), preco, lucro);
        return paraResponse(repository.save(precificacao));
    }

    @Transactional(readOnly = true)
    public Page<PrecificacaoResponse> listar(String produto, Pageable pageable) { return (produto == null ? repository.findAll(pageable) : repository.findByProdutoNomeContainingIgnoreCase(produto, pageable)).map(this::paraResponse); }
    @Transactional(readOnly = true)
    public Precificacao buscar(Long id) { return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Precificacao nao encontrada")); }
    @Transactional(readOnly = true)
    public PrecificacaoResponse resposta(Long id) { return paraResponse(buscar(id)); }
    public void excluir(Long id) { repository.delete(buscar(id)); }
    private PrecificacaoResponse paraResponse(Precificacao p) { return new PrecificacaoResponse(p.getId(), p.getProduto().getId(), p.getProduto().getNome(), p.getFrete(), p.getEmbalagem(), p.getTaxaCartao(), p.getTaxaPlataforma(), p.getImposto(), p.getMargemLucro(), p.getPrecoFinal(), p.getLucro(), p.getDataCriacao()); }
}
