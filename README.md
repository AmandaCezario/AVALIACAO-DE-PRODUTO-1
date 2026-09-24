# SISTEMA KAORI DE CONTROLE FINANCEIRO E PRECIFICACAO

Projeto desenvolvido para a disciplina **Projeto de Website - Back End**, no curso de Tecnologia em Analise e Desenvolvimento de Sistemas, 4o periodo.

## Integrantes

- Amanda Cezario

## Sobre o projeto

A calculadora de precificação auxilia pequenos negocios a cadastrar produtos, organizar categorias e calcular um preco de venda considerando custos extras, taxas e margem de lucro.

A interface web existente representa a calculadora e o historico. O backend foi organizado como uma API REST em Java 21 e Spring Boot, com persistencia em MySQL.

## Tecnologias

- Java 21
- Spring Boot
- Maven
- MySQL
- Spring Data JPA
- Bean Validation
- Spring Security (HTTP Basic)
- OpenAPI/Swagger
- JUnit e Mockito

## Entidades e relacionamentos

- `Usuario` possui varios `Produto`; as precificacoes pertencem aos produtos desse usuario.
- `Categoria` possui varios `Produto`.
- `Produto` possui varias `Precificacao`.

As quatro entidades principais sao `Usuario`, `Categoria`, `Produto` e `Precificacao`.

## Como executar o backend

1. Instale Java 21 e Maven.
2. Crie o banco MySQL ou mantenha `createDatabaseIfNotExist=true` na URL.
3. Entre na pasta `backend`.
4. Ajuste `DB_USERNAME` e `DB_PASSWORD`, se necessario.
5. Execute:

```bash
mvn spring-boot:run
```

Para executar os testes:

```bash
mvn test
```

## Documentacao da API

Com a aplicacao em execucao, acesse:

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

A API usa autenticacao HTTP Basic. Para demonstracao, use:

```text
usuario: admin
senha: kaori123
```

Esses valores podem ser alterados pelas variaveis `APP_ADMIN_USERNAME` e `APP_ADMIN_PASSWORD`.

## Exemplos de consultas

```text
GET /api/produtos?nome=coleira&page=0&size=10&sort=nome,asc
GET /api/precificacoes?produto=coleira&page=0&size=10&sort=dataCriacao,desc
```

## Primeira etapa da disciplina

- CRUD de usuarios, categorias, produtos e precificacoes;
- DTOs de requisicao e resposta;
- Validacao dos dados recebidos;
- Regra de calculo de preco no service;
- Tratamento centralizado de excecoes;
- Filtros, paginacao e ordenacao;
- Interface documentada pelo Swagger;
- Teste unitario da regra principal;
- Controle de versao com Git.

