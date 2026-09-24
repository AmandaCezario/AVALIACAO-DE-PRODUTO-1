package br.com.kaori.precificacao.controller;

import br.com.kaori.precificacao.dto.ProdutoRequest;
import br.com.kaori.precificacao.dto.ProdutoResponse;
import br.com.kaori.precificacao.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {
    private final ProdutoService service;
    public ProdutoController(ProdutoService service) { this.service = service; }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public ProdutoResponse criar(@Valid @RequestBody ProdutoRequest request) { return service.criar(request); }
    @GetMapping public Page<ProdutoResponse> listar(@RequestParam(required = false) String nome, @RequestParam(required = false) Long categoriaId, Pageable pageable) { return service.listar(nome, categoriaId, pageable); }
    @PutMapping("/{id}") public ProdutoResponse atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoRequest request) { return service.atualizar(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void excluir(@PathVariable Long id) { service.excluir(id); }
}
