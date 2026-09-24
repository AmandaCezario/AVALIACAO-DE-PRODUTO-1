package br.com.kaori.precificacao.controller;

import br.com.kaori.precificacao.dto.PrecificacaoRequest;
import br.com.kaori.precificacao.dto.PrecificacaoResponse;
import br.com.kaori.precificacao.service.PrecificacaoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/precificacoes")
public class PrecificacaoController {
    private final PrecificacaoService service;
    public PrecificacaoController(PrecificacaoService service) { this.service = service; }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public PrecificacaoResponse criar(@Valid @RequestBody PrecificacaoRequest request) { return service.calcularESalvar(request); }
    @GetMapping public Page<PrecificacaoResponse> listar(@RequestParam(required = false) String produto, Pageable pageable) { return service.listar(produto, pageable); }
    @GetMapping("/{id}") public PrecificacaoResponse buscar(@PathVariable Long id) { return service.resposta(id); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void excluir(@PathVariable Long id) { service.excluir(id); }
}
