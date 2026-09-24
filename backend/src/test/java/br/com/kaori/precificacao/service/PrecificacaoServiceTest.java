package br.com.kaori.precificacao.service;

import br.com.kaori.precificacao.dto.PrecificacaoRequest;
import br.com.kaori.precificacao.entity.Produto;
import br.com.kaori.precificacao.repository.PrecificacaoRepository;
import br.com.kaori.precificacao.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PrecificacaoServiceTest {
    @Mock private PrecificacaoRepository repository;
    @Mock private ProdutoRepository produtoRepository;
    @InjectMocks private PrecificacaoService service;

    @Test
    void deveCalcularPrecoFinalComCustosETaxas() {
        Produto produto = new Produto("Coleira", null, new BigDecimal("20.00"), 5, null, null);
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var resultado = service.calcularESalvar(new PrecificacaoRequest(1L, new BigDecimal("5"), new BigDecimal("2"), new BigDecimal("5"), new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("50")));

        assertEquals(new BigDecimal("60.00"), resultado.precoFinal());
        assertEquals(new BigDecimal("30.00"), resultado.lucro());
    }
}
