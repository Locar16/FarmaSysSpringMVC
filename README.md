# FarmaSys — Sistema de Gestão de Farmácia (Spring MVC)

Trabalho final da disciplina de Programação Orientada a Objetos Web 1 (POOW1) — UFSM.
Aplicação web para gestão de uma farmácia: cadastro de medicamentos, clientes e
farmacêuticos, registro de vendas com baixa de estoque e histórico de movimentações.

Esta é a versão construída sobre **Spring Boot / Spring MVC**, com views em JSP e
acesso a dados em JDBC puro sobre PostgreSQL.

**Autora:** Lorena Finamor Carvalho
**Professor:** Alencar Machado

---

## Stack

| Camada            | Tecnologia                                                        |
|-------------------|-------------------------------------------------------------------|
| Linguagem         | Java 17                                                           |
| Framework         | Spring Boot 4.1.0 (Spring MVC)                                    |
| Servidor          | Tomcat embarcado (também empacotável como WAR)                   |
| View              | JSP + JSTL + Expression Language                                  |
| Banco de dados    | PostgreSQL                                                        |
| Acesso a dados    | JDBC puro (sem ORM), via `DriverManager`                          |
| Build             | Apache Maven (packaging `war`)                                    |

---

## Arquitetura

A aplicação segue o padrão **MVC em camadas**, com o `DispatcherServlet` do Spring
atuando como Front Controller (ponto único de entrada das requisições):

```
Requisição → DispatcherServlet → Controller → Service → DAO → PostgreSQL
                                      ↓
                                  View (JSP)
```

Pacote base: `com.Lorena.Farmasys`

- **`controller`** — classes anotadas com `@Controller`, mapeando rotas via
  `@GetMapping` / `@PostMapping`. Recebem os parâmetros, acionam o serviço e
  devolvem o nome da view (padrão Post-Redirect-Get). Não contêm regra de negócio.
- **`service`** — uma classe por entidade. Concentra validações, verificação de
  estoque, cálculo do total da venda e tradução de erros de banco em mensagens
  amigáveis.
- **`dao`** — encapsula o acesso ao PostgreSQL via JDBC, com `map(ResultSet)` por
  entidade e a conexão centralizada em `ConexaoDB`.
- **`model`** — POJOs do domínio: `Farmaceutico`, `Medicamento`, `Cliente`,
  `Venda` e `ItemVenda`.

A resolução de views é configurada em `application.properties`:

```properties
spring.mvc.view.prefix=/WEB-INF/pages/
spring.mvc.view.suffix=.jsp
```

O retorno `"dashboard"` de um controller, por exemplo, resolve para
`/WEB-INF/pages/dashboard.jsp`.

---

## Funcionalidades

- **Autenticação e sessão** — login por usuário/senha; a sessão guarda o
  farmacêutico logado, e cada controller redireciona para a tela de login quando
  não há sessão ativa.
- **Auto-cadastro de farmacêutico** — qualquer visitante pode criar a própria
  conta pela tela pública de cadastro, com confirmação de senha validada no
  servidor.
- **Medicamentos (CRUD)** — cadastro completo com princípio ativo, dosagem,
  laboratório, lote, validade, classificação, tipo, grupo farmacológico, preço e
  estoque. A exclusão é bloqueada quando o medicamento já possui vendas.
- **Clientes (CRUD)** — cadastro, edição e exclusão (bloqueada quando há venda
  vinculada).
- **Farmacêuticos (CRUD)** — listagem de todos; cada usuário edita apenas o
  próprio perfil, com troca de senha e desativação da própria conta.
- **Vendas (processamento)** — carrinho mantido na sessão, verificação e **baixa
  automática de estoque**, formas de pagamento (Dinheiro, Cartão, PIX) e cliente
  opcional. Cada venda persiste seus itens em transação.
- **Dashboard** — atalhos para os módulos e histórico completo de vendas, com
  farmacêutico, cliente, forma de pagamento e total.

---

## Modelo de dados

Cinco tabelas com integridade referencial entre elas:

- `farmaceutico` — usuários do sistema (login, CRF, dados pessoais).
- `cliente` — clientes da farmácia.
- `medicamento` — itens em estoque.
- `venda` — referencia `farmaceutico_id` (obrigatório) e `cliente_id` (opcional).
- `item_venda` — referencia `venda_id` e `medicamento_id` (itens de cada venda).

O banco utiliza tipos enumerados próprios do PostgreSQL (`forma_pagamento`,
`classificacao_medicamento`, `tipo_medicamento`, `uf`), por isso os `INSERT`/`UPDATE`
fazem *cast* explícito (ex.: `?::forma_pagamento`).

---

## Pré-requisitos

- [JDK 17](https://adoptium.net/) (ou superior)
- [Apache Maven 3.9+](https://maven.apache.org/download.cgi)
- [PostgreSQL](https://www.postgresql.org/download/)
- Navegador web moderno

---

## Como executar

### 1. Preparar o banco de dados

Crie um banco chamado `farmasys` no seu PostgreSQL e execute o script de criação
das tabelas e tipos enumerados (disponível na pasta de scripts do projeto). Ao
final devem existir as tabelas `cliente`, `farmaceutico`, `medicamento`, `venda`
e `item_venda`.

```sql
CREATE DATABASE farmasys;
```

### 2. Configurar a conexão

As credenciais ficam em `src/main/java/com/Lorena/Farmasys/dao/ConexaoDB.java`.
Ajuste URL, usuário e senha conforme o seu ambiente:

```java
private static final String URL     = "jdbc:postgresql://localhost:5432/farmasys";
private static final String USUARIO = "postgres";
private static final String SENHA   = "1234";
```

### 3. Rodar a aplicação

Com o Tomcat embarcado (modo de desenvolvimento):

```bash
mvn spring-boot:run
```

A aplicação sobe na porta **8081** (definida em `application.properties`).

> Alternativamente, como o projeto é empacotado em `war`, é possível gerar o
> artefato com `mvn clean package` e implantá-lo em um Tomcat externo.

### 4. Acessar

```
http://localhost:8081/
```

A aplicação abre direto na tela de login. Como o banco é criado vazio, comece
criando um farmacêutico pela opção de cadastro e alguns medicamentos para validar
o fluxo de vendas.

---

## Estrutura do projeto

```
src/main/
├── java/com/Lorena/Farmasys/
│   ├── FarmasysApplication.java      # classe principal Spring Boot
│   ├── ServletInitializer.java       # suporte a deploy como WAR
│   ├── controller/                   # Login, Cadastro, Dashboard,
│   │                                 # Medicamento, Cliente, Farmaceutico, Venda
│   ├── service/                      # regras de negócio por entidade
│   ├── dao/                          # acesso a dados (JDBC) + ConexaoDB
│   └── model/                        # POJOs do domínio
├── resources/
│   └── application.properties        # porta, view resolver, etc.
└── webapp/
    ├── WEB-INF/pages/                # views JSP (index, dashboard, ...)
    ├── css/                          # estilos
    └── img/                          # ícones e imagens
```

---

## Requisitos da disciplina atendidos

- **CRUDs (≥ 2):** Medicamentos, Clientes e Farmacêuticos.
- **Tela de processamento:** Vendas (carrinho, baixa de estoque, cálculo de total)
  e Dashboard com histórico.
- **Controle de sessão:** sessão do farmacêutico verificada em cada rota protegida.
- **Padrões Java, arquitetura MVC, uso do Spring e estrutura Maven.**
- **Banco com chaves estrangeiras** entre as cinco tabelas.
