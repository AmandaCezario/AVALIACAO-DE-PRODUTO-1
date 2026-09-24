package br.com.kaori.precificacao.dto;

import java.math.BigDecimal;

public record ProdutoResponse(Long id, String nome, String descricao, BigDecimal custoCompra,
                              Integer estoque, Long categoriaId, String categoriaNome, Long usuarioId) { }
