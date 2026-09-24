package br.com.kaori.precificacao.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PrecificacaoResponse(Long id, Long produtoId, String produtoNome, BigDecimal frete,
                                   BigDecimal embalagem, BigDecimal taxaCartao, BigDecimal taxaPlataforma,
                                   BigDecimal imposto, BigDecimal margemLucro, BigDecimal precoFinal,
                                   BigDecimal lucro, LocalDateTime dataCriacao) { }
