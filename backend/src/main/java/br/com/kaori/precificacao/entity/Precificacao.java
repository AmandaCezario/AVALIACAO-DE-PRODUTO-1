package br.com.kaori.precificacao.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "precificacoes")
public class Precificacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @NotNull @DecimalMin("0") private BigDecimal frete;
    @NotNull @DecimalMin("0") private BigDecimal embalagem;
    @NotNull @DecimalMin("0") private BigDecimal taxaCartao;
    @NotNull @DecimalMin("0") private BigDecimal taxaPlataforma;
    @NotNull @DecimalMin("0") private BigDecimal imposto;
    @NotNull @DecimalMin("0.01") private BigDecimal margemLucro;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal precoFinal;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal lucro;
    @Column(nullable = false) private LocalDateTime dataCriacao;

    protected Precificacao() { }

    public Precificacao(Produto produto, BigDecimal frete, BigDecimal embalagem, BigDecimal taxaCartao,
                        BigDecimal taxaPlataforma, BigDecimal imposto, BigDecimal margemLucro,
                        BigDecimal precoFinal, BigDecimal lucro) {
        this.produto = produto; this.frete = frete; this.embalagem = embalagem; this.taxaCartao = taxaCartao;
        this.taxaPlataforma = taxaPlataforma; this.imposto = imposto; this.margemLucro = margemLucro;
        this.precoFinal = precoFinal; this.lucro = lucro; this.dataCriacao = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Produto getProduto() { return produto; }
    public BigDecimal getFrete() { return frete; }
    public BigDecimal getEmbalagem() { return embalagem; }
    public BigDecimal getTaxaCartao() { return taxaCartao; }
    public BigDecimal getTaxaPlataforma() { return taxaPlataforma; }
    public BigDecimal getImposto() { return imposto; }
    public BigDecimal getMargemLucro() { return margemLucro; }
    public BigDecimal getPrecoFinal() { return precoFinal; }
    public BigDecimal getLucro() { return lucro; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
}
