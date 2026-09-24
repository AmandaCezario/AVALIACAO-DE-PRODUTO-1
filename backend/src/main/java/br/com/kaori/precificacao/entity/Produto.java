package br.com.kaori.precificacao.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "produtos")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    private String descricao;

    @NotNull
    @DecimalMin("0.01")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal custoCompra;

    @NotNull
    @DecimalMin("0")
    @Column(nullable = false)
    private Integer estoque;

    @ManyToOne(optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    protected Produto() { }

    public Produto(String nome, String descricao, BigDecimal custoCompra, Integer estoque, Categoria categoria, Usuario usuario) {
        this.nome = nome;
        this.descricao = descricao;
        this.custoCompra = custoCompra;
        this.estoque = estoque;
        this.categoria = categoria;
        this.usuario = usuario;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public BigDecimal getCustoCompra() { return custoCompra; }
    public Integer getEstoque() { return estoque; }
    public Categoria getCategoria() { return categoria; }
    public Usuario getUsuario() { return usuario; }
    public void atualizar(String nome, String descricao, BigDecimal custoCompra, Integer estoque, Categoria categoria) {
        this.nome = nome; this.descricao = descricao; this.custoCompra = custoCompra; this.estoque = estoque; this.categoria = categoria;
    }
}
