# 🍕 AnotaPedido API

> API RESTful para gestão atômica de pedidos e cardápio, construída com Spring Boot 3, Spring Security 6 e autenticação via JWT (JJWT v0.13.0).

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green?style=flat-square&logo=springboot)
![Spring Security](https://img.shields.io/badge/Spring_Security-6.x-blue?style=flat-square&logo=springsecurity)
![JWT](https://img.shields.io/badge/JWT-JJWT_0.13.0-black?style=flat-square&logo=jsonwebtokens)
![License](https://img.shields.io/badge/License-MIT-blue.svg?style=flat-square)

---

## 📌 Sobre o Projeto

O **AnotaPedido** é uma solução backend para automação de pedidos e gerenciamento de cardápios em estabelecimentos alimentícios. 

A API foi projetada focando em **arquitetura em camadas, integridade transacional atômica**, **cobertura abrangente de testes automatizados** e um modelo rigoroso de **Controle de Acesso Baseado em Papéis (RBAC)** via Spring Security 6.

---

## 🛠️ Tecnologias e Ferramentas

- **Linguagem:** Java 21
- **Framework:** Spring Boot 3.x
- **Segurança & Autenticação:** Spring Security 6 + JJWT (`io.jsonwebtoken` v0.13.0)
- **Persistência de Dados:** Spring Data JPA / Hibernate
- **Banco de Dados:** H2 Database (Dev/Testes) / PostgreSQL (Produção)
- **Documentação:** OpenAPI 3 / Swagger UI
- **Testes Automatizados:** JUnit 5, Mockito, AssertJ, MockMvc, Spring Security Test
- **Produtividade & Validação:** Lombok, Bean Validation (`spring-boot-starter-validation`)

---

## 🔐 Matriz de Autorização e Segurança (RBAC)

A API opera de forma **Stateless** com autenticação via **Bearer Token JWT**. As regras de acesso por rota foram configuradas de forma granular no `SecurityConfig`:

| Verbo HTTP | Endpoint | Permissão Exigida | Descrição da Operação |
| :--- | :--- | :--- | :--- |
| `POST` | `/usuarios/login` | `permitAll()` | Autenticação e emissão do Token JWT |
| `POST` | `/usuarios` | `permitAll()` | Cadastro de novo usuário (`USER` ou `ADMIN`) |
| `GET` | `/v3/api-docs/**`, `/swagger-ui/**` | `permitAll()` | Documentação interativa Swagger |
| `GET` | `/produtos/**` | `hasAnyRole("USER", "ADMIN")` | Visualização dos itens do cardápio |
| `POST`, `PUT`, `DELETE` | `/produtos/**` | `hasRole("ADMIN")` | Gestão de produtos (Exclusivo Administrador) |
| `GET` | `/pedidos/**` | `hasAnyRole("USER", "ADMIN")` | Consulta e histórico de pedidos |
| `POST`, `PUT`, `DELETE` | `/pedidos/**` | `hasRole("USER")` | Criação e gestão de pedidos pelo cliente |

---

## 🚀 Funcionalidades & Arquitetura

### 🛒 Criação Atômica de Pedidos
- **Transacionalidade (`@Transactional`):** O pedido e a associação dos seus respectivos itens são persitidos em um único ciclo atômico, garantindo consistência total do banco de dados.
- **Cálculo Dinâmico:** O valor total é calculado no backend cruzando os IDs dos produtos e quantidades enviadas no DTO com as entidades gerenciadas no banco.

### 🛡️ Tratamento Global de Exceções
- Tratamento centralizado via `@RestControllerAdvice` capturando validações de payload (`400 Bad Request`), falhas de autenticação (`401 Unauthorized`), violação de permissões (`403 Forbidden`) e recursos não encontrados (`404 Not Found`).

---

## 🧪 Suíte de Testes Automatizados

A aplicação possui **100% de cobertura nos cenários críticos** das camadas `Service` e `Controller`:

### 🔬 Testes Unitários (`Service`)
- Implementados com **JUnit 5** e **Mockito** (`@Mock`, `@InjectMocks`).
- Isolamento total da regra de negócio sem depender de contexto Spring.
- Cobertura de caminhos felizes e lançamento de exceções de domínio.

### 🌐 Testes de Integração (`Controller` / `MockMvc`)
- Uso do `MockMvc` integrado ao **Spring Security Test** (`@WithMockUser`, `@WithAnonymousUser`).
- **Validação de Permissões:** Testes explícitos garantindo retorno `403 Forbidden` quando um perfil `USER` tenta alterar produtos ou quando um perfil `ADMIN` tenta realizar requisições restritas.
- **Validação de Autenticação:** Testes garantindo retorno `401 Unauthorized` para requisições anônimas em rotas protegidas.
- **Validação de Payload:** Garantia de retorno `400 Bad Request` na presença de DTOs inválidos.

Para rodar toda a suíte de testes:
```bash
./mvnw clean test
