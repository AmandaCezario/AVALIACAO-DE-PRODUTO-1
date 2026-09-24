package br.com.kaori.precificacao.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PrecificacaoRequest(
        @NotNull Long produtoId,
        @NotNull @DecimalMin("0") BigDecimal frete,
        @NotNull @DecimalMin("0") BigDecimal embalagem,
        @NotNull @DecimalMin("0") BigDecimal taxaCartao,
        @NotNull @DecimalMin("0") BigDecimal taxaPlataforma,
        @NotNull @DecimalMin("0") BigDecimal imposto,
        @NotNull @DecimalMin("0.01") BigDecimal margemLucro) { }
