package br.com.kaori.precificacao.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProdutoRequest(
        @NotBlank String nome,
        String descricao,
        @NotNull @DecimalMin("0.01") BigDecimal custoCompra,
        @NotNull @DecimalMin("0") Integer estoque,
        @NotNull Long categoriaId,
        @NotNull Long usuarioId) { }
