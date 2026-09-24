package br.com.kaori.precificacao.controller;

import br.com.kaori.precificacao.dto.CategoriaRequest;
import br.com.kaori.precificacao.dto.CategoriaResponse;
import br.com.kaori.precificacao.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {
    private final CategoriaService service;
    public CategoriaController(CategoriaService service) { this.service = service; }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public CategoriaResponse criar(@Valid @RequestBody CategoriaRequest request) { return service.criar(request); }
    @GetMapping public List<CategoriaResponse> listar() { return service.listar(); }
    @PutMapping("/{id}") public CategoriaResponse atualizar(@PathVariable Long id, @Valid @RequestBody CategoriaRequest request) { return service.atualizar(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void excluir(@PathVariable Long id) { service.excluir(id); }
}
