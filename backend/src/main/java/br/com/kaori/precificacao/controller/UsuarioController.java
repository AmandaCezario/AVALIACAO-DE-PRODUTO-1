package br.com.kaori.precificacao.controller;

import br.com.kaori.precificacao.dto.UsuarioRequest;
import br.com.kaori.precificacao.dto.UsuarioResponse;
import br.com.kaori.precificacao.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioService service;
    public UsuarioController(UsuarioService service) { this.service = service; }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public UsuarioResponse criar(@Valid @RequestBody UsuarioRequest request) { return service.criar(request); }
    @GetMapping public List<UsuarioResponse> listar() { return service.listar(); }
    @PutMapping("/{id}") public UsuarioResponse atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequest request) { return service.atualizar(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void excluir(@PathVariable Long id) { service.excluir(id); }
}
