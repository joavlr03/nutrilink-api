# 🍼 NutriLink API
 
![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-6DB33F?logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8-4479A1?logo=mysql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9.16-C71A36?logo=apachemaven&logoColor=white)
![Swagger](https://img.shields.io/badge/OpenAPI-Swagger%20UI-85EA2D?logo=swagger&logoColor=black)
![Lombok](https://img.shields.io/badge/Lombok-enabled-red)
![License](https://img.shields.io/badge/license-unspecified-lightgrey)
 
**NutriLink API** é um serviço REST em **Spring Boot** para gestão de um **banco de leite humano**: cadastro e triagem de doadoras, alocação de profissionais de saúde, agendamento e rastreio de coletas por corredores logísticos, sincronização de coletas com sistemas externos e um canal de suporte via tickets e mensagens.
 
O projeto foi construído para equipes que operam a logística de doação de leite humano, automatizando regras que hoje seriam manuais: elegibilidade da doadora, validação de área de cobertura logística, controle de credenciais dos profissionais e o ciclo de vida de tickets de suporte.
 
---
## Link Projeto : https://github.com/joavlr03/nutrilink-api
## Equipe - Nutrilink

- Giovanni Sguizzardi Conde - RM565123
- Nicole Alves Nogueira - RM555182
- Lucas Lima Franco - RM550255
- Bruno César Toledo D Oliveira - RM554878
- João Victor Oliveira Avellar - RM550283
## 📌 Sumário
 
- [Funcionalidades](#-funcionalidades)
- [Tecnologias](#-tecnologias)
- [Arquitetura](#-arquitetura)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Organização do Código](#-organização-do-código)
- [Modelo de Domínio](#-modelo-de-domínio)
- [Fluxo da Aplicação](#-fluxo-da-aplicação)
- [Endpoints Principais](#-endpoints-principais)
- [Banco de Dados](#-banco-de-dados)
- [Requisitos](#-requisitos)
- [Como Executar](#-como-executar)
- [Dependências Principais](#-dependências-principais)
- [Possíveis Melhorias](#-possíveis-melhorias)
- [Autor](#-autor)
---
 
## ✅ Funcionalidades
 
Com base nos controllers e services do projeto:
 
- 👩 **Cadastro de doadoras**, com validação de CPF único e idade mínima (18 anos)
- 🩺 **Triagem de elegibilidade**, com cálculo automático de `scoreRisco` a partir de um questionário e decisão automática (aprovação, reprovação ou envio para revisão humana)
- 👨‍⚕️ **Cadastro e gestão de profissionais de saúde**, com dois perfis (`ANALISTA_NIVEL_1` e `ESPECIALISTA_LACTACAO`) e controle de ativação/inativação de credencial
- 🚚 **Cadastro de corredores logísticos**, com CEPs atendidos e homologação (habilitar/desabilitar)
- 📦 **Agendamento de coletas**, com validações de negócio: doadora aprovada, corredor homologado e CEP compatível com o corredor
- 🔄 Atualização do **status da coleta** (`AGENDADA`, `EM_ROTA`, `CONCLUIDA`, `CANCELADA`)
- 🔁 Modelo de **Sincronização** de coletas com sistemas externos (payload enviado, protocolo gerado e status de sincronização)
- 🎫 **Tickets de suporte** abertos por doadoras, que podem ser assumidos exclusivamente por especialistas em lactação com credencial ativa
- 💬 **Mensagens de suporte** trocadas dentro de um ticket (doadora ou profissional), bloqueadas quando o ticket está fechado
- 📑 Documentação de API automática via **Swagger UI / OpenAPI**
- ⚠️ Tratamento de regras de negócio com exceções dedicadas (`EntityNotFoundException`, `IllegalStateException`, `IllegalArgumentException`)
---
 
## 🛠 Tecnologias
 
Somente as tecnologias efetivamente encontradas no `pom.xml` e no código-fonte:
 
| Categoria | Tecnologia |
|---|---|
| Linguagem | Java 17 |
| Framework | Spring Boot 4.1.1 |
| Persistência | Spring Data JPA / Hibernate |
| Validação | Spring Boot Starter Validation (Jakarta Bean Validation) |
| Web | Spring Web MVC |
| Documentação de API | springdoc-openapi (Swagger UI) |
| Banco de Dados | MySQL (mysql-connector-j) |
| Boilerplate | Lombok |
| Produtividade | Spring Boot DevTools |
| Build | Maven (via Maven Wrapper) |
 
---
 
## 🏗 Arquitetura
 
O projeto segue uma **arquitetura em camadas (Layered Architecture)**, típica de APIs Spring Boot, organizada por responsabilidade técnica:
 
```
Controller  →  Service  →  Repository  →  Banco de Dados
     ↑
   DTO + Mapper
```
 
- **Controller**: expõe os endpoints REST, valida entrada (`@Valid`) e delega regras de negócio ao Service.
- **Service**: concentra as regras de negócio (validações, cálculo de score, transições de status).
- **Repository**: interfaces `JpaRepository` responsáveis pelo acesso a dados.
- **Model**: entidades JPA mapeadas para as tabelas do MySQL.
- **DTO + Mapper**: cada entidade possui `CreateRequest`, `Response` e um `Mapper` dedicado, isolando o modelo de persistência do contrato exposto pela API.
---
 
## 📁 Estrutura do Projeto
 
```
nutrilink-api/
├── pom.xml
├── mvnw / mvnw.cmd
├── .mvn/wrapper/maven-wrapper.properties
└── src/
    ├── main/
    │   ├── java/br/com/joavlr03/nutrilink_api/
    │   │   ├── NutrilinkApiApplication.java
    │   │   ├── controller/        # Endpoints REST
    │   │   ├── service/           # Regras de negócio
    │   │   ├── repository/        # Acesso a dados (Spring Data JPA)
    │   │   ├── model/             # Entidades JPA
    │   │   │   └── enums/         # Enums de domínio
    │   │   └── dto/               # CreateRequest, Response e Mapper por entidade
    │   └── resources/
    │       └── application.properties
    └── test/
        └── java/.../NutrilinkApiApplicationTests.java
```
 
---
 
## 🧩 Organização do Código
 
| Pacote | Responsabilidade |
|---|---|
| `controller/` | Recebe requisições HTTP, aplica validação de entrada e devolve DTOs de resposta |
| `service/` | Implementa as regras de negócio: elegibilidade de doadora, homologação de corredor, score de triagem, transições de status de coleta/ticket |
| `repository/` | Interfaces `JpaRepository` com consultas derivadas (ex.: `findByStatusColeta`, `findByCredencialAtivaTrue`) |
| `model/` | Entidades JPA (`Doadora`, `Coleta`, `CorredorLogistico`, `ProfissionalSaude`, `TicketSuporte`, `MensagemSuporte`, `Triagem`, `Sincronizacao`) e seus enums |
| `dto/<entidade>/` | `*CreateRequest` (entrada validada), `*Response` (saída) e `*Mapper` (conversão model ↔ dto) para cada entidade |
 
---
 
## 🧬 Modelo de Domínio
 
| Entidade | Descrição |
|---|---|
| `Doadora` | Pessoa cadastrada para doação de leite; possui `statusCadastro` (`PENDENTE`, `APROVADA`, `REPROVADA`) |
| `Triagem` | Avaliação de elegibilidade da doadora; calcula `scoreRisco` e define se precisa de revisão humana |
| `ProfissionalSaude` | Analista (`ANALISTA_NIVEL_1`) ou especialista em lactação (`ESPECIALISTA_LACTACAO`), com credencial ativável/inativável |
| `CorredorLogistico` | Rota logística homologada, associada a uma lista de CEPs atendidos |
| `Coleta` | Agendamento de coleta de leite vinculado a uma doadora e a um corredor; possui `statusColeta` |
| `Sincronizacao` | Registro (1:1 com `Coleta`) do envio da coleta a um sistema externo, com payload e status (`PENDENTE`, `SUCESSO`, `FALHA`) |
| `TicketSuporte` | Chamado de suporte aberto por uma doadora, podendo ser assumido por um especialista em lactação |
| `MensagemSuporte` | Mensagem trocada dentro de um ticket, enviada por `DOADORA` ou `PROFISSIONAL` |
 
---
 
## 🔄 Fluxo da Aplicação
 
**Cadastro e aprovação de uma doadora:**
 
```
Cadastro da doadora (status PENDENTE)
        ↓
Triagem de elegibilidade (score calculado a partir do questionário)
        ↓
   score ≥ 70            40 ≤ score < 70            score < 40
        ↓                        ↓                        ↓
   APROVADA          PENDENTE_REVISAO (analista)      REPROVADA
        ↓                        ↓                        ↓
statusCadastro=APROVADA   statusCadastro=PENDENTE   statusCadastro=REPROVADA
```
 
**Agendamento de coleta (somente doadora aprovada):**
 
```
Doadora aprovada + Corredor homologado
        ↓
CEP da doadora compatível com o corredor?
        ↓ sim
Coleta criada com status AGENDADA
        ↓
Atualizações de status → EM_ROTA → CONCLUIDA / CANCELADA
```
 
**Ticket de suporte:**
 
```
Doadora abre ticket (ABERTO)
        ↓
Especialista em lactação com credencial ativa assume (EM_ANDAMENTO)
        ↓
Mensagens trocadas (bloqueadas se o ticket estiver FECHADO)
        ↓
Ticket fechado (FECHADO)
```
 
---
 
## 🔌 Endpoints Principais
 
Documentação interativa disponível via Swagger UI (ver seção [Como Executar](#-como-executar)). Principais rotas por recurso:
 
### `/api/v2/doadoras`
| Método | Rota | Descrição |
|---|---|---|
| POST | `/` | Cadastrar doadora |
| GET | `/{id}` | Buscar doadora por ID |
| GET | `/` | Listar todas as doadoras |
| DELETE | `/{id}` | Excluir doadora |
 
### `/api/v2/triagens`
| Método | Rota | Descrição |
|---|---|---|
| POST | `/` | Realizar triagem de doadora |
| GET | `/{id}` | Buscar triagem por ID |
| GET | `/` | Listar todas as triagens |
| GET | `/doadora/{doadoraId}` | Listar triagens de uma doadora |
| GET | `/pendentes-revisao` | Listar triagens pendentes de revisão humana |
| DELETE | `/{id}` | Excluir triagem |
 
### `/api/v2/logistica`
| Método | Rota | Descrição |
|---|---|---|
| POST | `/` | Cadastrar corredor logístico |
| GET | `/{id}` | Buscar corredor por ID |
| GET | `/` | Listar todos os corredores |
| GET | `/ativos` | Listar corredores homologados ativos |
| PATCH | `/{id}/habilitar` | Habilitar corredor |
| PATCH | `/{id}/desabilitar` | Desabilitar corredor |
| DELETE | `/{id}` | Excluir corredor |
 
### `/api/v2/coleta`
| Método | Rota | Descrição |
|---|---|---|
| POST | `/` | Agendar coleta |
| GET | `/{id}` | Buscar coleta por ID |
| GET | `/` | Listar todas as coletas |
| GET | `/doadora/{doadoraId}` | Listar coletas de uma doadora |
| GET | `/status/{status}` | Listar coletas por status |
| PATCH | `/{id}/status/{novoStatus}` | Atualizar status da coleta |
| DELETE | `/{id}` | Excluir coleta |
 
### `/api/v2/profissionais-saude`
| Método | Rota | Descrição |
|---|---|---|
| POST | `/` | Cadastrar profissional de saúde |
| GET | `/{id}` | Buscar profissional por ID |
| GET | `/` | Listar todos os profissionais |
| GET | `/ativos` | Listar profissionais com credencial ativa |
| GET | `/tipo/{tipo}` | Listar profissionais por tipo |
| PATCH | `/{id}/inativar` | Inativar credencial |
| PATCH | `/{id}/reativar` | Reativar credencial |
| DELETE | `/{id}` | Excluir profissional |
 
### `/api/v2/tickets-suporte`
| Método | Rota | Descrição |
|---|---|---|
| POST | `/` | Abrir ticket de suporte |
| GET | `/{id}` | Buscar ticket por ID |
| GET | `/` | Listar todos os tickets |
| GET | `/doadora/{doadoraId}` | Listar tickets de uma doadora |
| GET | `/status/{status}` | Listar tickets por status |
| PATCH | `/{ticketId}/assumir/{profissionalId}` | Especialista assume o ticket |
| PATCH | `/{id}/fechar` | Fechar ticket |
| DELETE | `/{id}` | Excluir ticket |
 
### `/api/v2/mensagens-suporte`
| Método | Rota | Descrição |
|---|---|---|
| POST | `/` | Enviar mensagem no ticket |
| GET | `/{id}` | Buscar mensagem por ID |
| GET | `/` | Listar todas as mensagens |
| GET | `/ticket/{ticketId}` | Listar mensagens de um ticket em ordem cronológica |
| DELETE | `/{id}` | Excluir mensagem |
 
---
 
## 🗄 Banco de Dados
 
- **MySQL**, acessado via **Spring Data JPA / Hibernate**
- `spring.jpa.hibernate.ddl-auto=update` — o schema é atualizado automaticamente a partir das entidades
- Principais tabelas: `tb_doadoras`, `tb_triagens`, `tb_profissionais_saude` (implícita), `tb_corredores_logisticos`, `tb_coletas`, `sincronizacoes`, `tb_tickets_suporte`, `tb_mensagens_suporte`
---
 
## 📋 Requisitos
 
- JDK 17
- Maven 3.9+ (ou uso do wrapper `./mvnw` incluso no projeto)
- MySQL em execução localmente (ou acessível pela rede)
---
 
## ▶️ Como Executar

### Pré-requisitos

- JDK 17 ou 21
- Docker (para o MySQL). Se preferir um MySQL instalado localmente, pule o passo 2 e garanta um usuário `root` com senha `root_pwd`.
- Não é necessário instalar o Maven, pois o projeto inclui o Maven Wrapper (`mvnw`).

### 1. Clonar o repositório

```bash
git clone https://github.com/joavlr03/nutrilink-api.git
cd nutrilink-api
```

### 2. Subir o MySQL com Docker

```bash
docker run -d --name nutrilink-mysql \
  -e MYSQL_ROOT_PASSWORD=root_pwd \
  -e MYSQL_DATABASE=nutrilink \
  -p 3306:3306 \
  mysql:8.4
```

O banco `nutrilink` é criado automaticamente pelo container, e as tabelas são criadas pelo Hibernate (`ddl-auto=update`) na primeira execução. Aguarde de 20 a 30 segundos até o MySQL terminar de inicializar. Para acompanhar, use `docker logs -f nutrilink-mysql` e espere a mensagem `ready for connections`.

> Se a porta 3306 já estiver em uso, troque para `-p 3307:3306` e ajuste a porta em `spring.datasource.url`.

### 3. Conferir a configuração (`src/main/resources/application.properties`)

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/nutrilink?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=root_pwd
```

### 4. Executar a aplicação

Linux / macOS:
```bash
./mvnw spring-boot:run
```

Windows (PowerShell ou CMD):
```bash
mvnw.cmd spring-boot:run
```

A API sobe na porta **9000**. A documentação Swagger UI fica na raiz: **http://localhost:9000/**

### 5. Encerrar e remover o ambiente

Pare a aplicação com `Ctrl + C` e remova o container do banco:

```bash
docker stop nutrilink-mysql
docker rm nutrilink-mysql
```

---


## 🧪 Testando a API (roteiro completo)

Todos os testes podem ser feitos pelo **Swagger UI** (http://localhost:9000/) ou pelos comandos `curl` abaixo. Os IDs são UUIDs gerados pelo banco: copie o `id` de cada resposta e substitua nos passos seguintes.

As etapas precisam seguir esta ordem, porque cada uma depende da anterior:

```
Profissionais → Doadora → Triagem (aprova a doadora) → Corredor → Coleta → Sincronização
                   └──────────→ Ticket de suporte → Especialista assume → Mensagens → Fechar
```

### 1. Cadastrar profissionais de saúde

Especialista em lactação (atende os tickets):
```bash
curl -X POST http://localhost:9000/api/v2/profissionais-saude \
  -H "Content-Type: application/json" \
  -d '{
    "nomeCompleto": "Dra. Ana Souza",
    "registroConselho": "CRM-SP 123456",
    "tipoProfissional": "ESPECIALISTA_LACTACAO"
  }'
```

Analista (opcional, pode ser vinculado à triagem):
```bash
curl -X POST http://localhost:9000/api/v2/profissionais-saude \
  -H "Content-Type: application/json" \
  -d '{
    "nomeCompleto": "Carlos Lima",
    "registroConselho": "COREN-SP 987654",
    "tipoProfissional": "ANALISTA_NIVEL_1"
  }'
```

### 2. Cadastrar uma doadora

A doadora começa com status `PENDENTE`. Ela precisa ter 18 anos ou mais, e o CPF (11 dígitos) e o CEP (8 dígitos) devem ser enviados sem pontuação.

```bash
curl -X POST http://localhost:9000/api/v2/doadoras \
  -H "Content-Type: application/json" \
  -d '{
    "nomeCompleto": "Maria Oliveira",
    "cpf": "12345678901",
    "dataNascimento": "1995-04-20",
    "telefone": "11987654321",
    "cep": "07010000",
    "enderecoCompleto": "Rua das Flores, 100 - Centro"
  }'
```

### 3. Realizar a triagem

O campo `respostasQuestionario` é um **texto contendo um JSON**, por isso as aspas internas são escapadas. O score começa em 100 e sofre descontos conforme as respostas:

| Resposta `true` | Desconto |
|---|---|
| `medicamento` | -40 |
| `doencaCronica` | -30 |
| `alcool` | -20 |
| `fumo` | -10 |

O resultado depende do score final: **≥ 70** resulta em `APROVADA` (a doadora passa a `APROVADA`), **de 40 a 69** em `PENDENTE_REVISAO`, e **abaixo de 40** em `REPROVADA`. As chaves devem ser escritas sem espaço após os dois-pontos (`"medicamento":true`).

Triagem aprovada (score 100):
```bash
curl -X POST http://localhost:9000/api/v2/triagens \
  -H "Content-Type: application/json" \
  -d '{
    "doadoraId": "<ID_DOADORA>",
    "respostasQuestionario": "{\"medicamento\":false,\"doencaCronica\":false,\"alcool\":false,\"fumo\":false}"
  }'
```

Para testar a revisão humana (score 60), envie `"{\"medicamento\":true}"`. A triagem aparecerá em `GET /api/v2/triagens/pendentes-revisao`.

### 4. Cadastrar um corredor logístico

Em `cepsAtendidos`, informe os **3 primeiros dígitos** dos CEPs atendidos, separados por vírgula. O corredor já é criado homologado.

```bash
curl -X POST http://localhost:9000/api/v2/logistica \
  -H "Content-Type: application/json" \
  -d '{
    "nomeCorredor": "Corredor Guarulhos Centro",
    "cepsAtendidos": "070,071,072"
  }'
```

### 5. Agendar uma coleta

Para agendar, três condições precisam ser atendidas: a doadora deve estar `APROVADA`, o corredor deve estar homologado, e o CEP da doadora deve pertencer ao corredor. A data precisa ser futura.

```bash
curl -X POST http://localhost:9000/api/v2/coleta \
  -H "Content-Type: application/json" \
  -d '{
    "doadoraId": "<ID_DOADORA>",
    "corredorId": "<ID_CORREDOR>",
    "dataAgendada": "2027-01-15T09:00:00",
    "volumeEstimadoMl": 300
  }'
```

Atualizar o status da coleta (`AGENDADA`, `EM_ROTA`, `CONCLUIDA`, `CANCELADA`):
```bash
curl -X PATCH http://localhost:9000/api/v2/coleta/<ID_COLETA>/status/EM_ROTA
```

### 6. Sincronizar a coleta com o sistema externo

Registrar o envio (status `PENDENTE`). O payload é gerado automaticamente a partir da coleta:
```bash
curl -X POST http://localhost:9000/api/v2/sincronizacoes \
  -H "Content-Type: application/json" \
  -d '{ "coletaId": "<ID_COLETA>" }'
```

Confirmar com o protocolo devolvido pelo sistema externo (status `SUCESSO`):
```bash
curl -X PATCH http://localhost:9000/api/v2/sincronizacoes/<ID_SINCRONIZACAO>/confirmar \
  -H "Content-Type: application/json" \
  -d '{ "protocoloGerado": "BLH-2027-000123" }'
```

Existe também o fluxo alternativo de falha: `PATCH /api/v2/sincronizacoes/<ID>/falha` leva ao status `FALHA`, e `PATCH /api/v2/sincronizacoes/<ID>/reprocessar` devolve ao status `PENDENTE`.

### 7. Suporte: ticket e mensagens

Abrir um ticket:
```bash
curl -X POST http://localhost:9000/api/v2/tickets-suporte \
  -H "Content-Type: application/json" \
  -d '{
    "doadoraId": "<ID_DOADORA>",
    "assunto": "Dúvida sobre armazenamento do leite"
  }'
```

O especialista assume o ticket (somente `ESPECIALISTA_LACTACAO` com credencial ativa):
```bash
curl -X PATCH http://localhost:9000/api/v2/tickets-suporte/<ID_TICKET>/assumir/<ID_ESPECIALISTA>
```

Enviar uma mensagem (`remetenteTipo` pode ser `DOADORA` ou `PROFISSIONAL`):
```bash
curl -X POST http://localhost:9000/api/v2/mensagens-suporte \
  -H "Content-Type: application/json" \
  -d '{
    "ticketId": "<ID_TICKET>",
    "remetenteTipo": "DOADORA",
    "remetenteId": "<ID_DOADORA>",
    "conteudoMensagem": "Posso congelar o leite em pote de vidro?"
  }'
```

Listar as mensagens e fechar o ticket:
```bash
curl http://localhost:9000/api/v2/mensagens-suporte/ticket/<ID_TICKET>
curl -X PATCH http://localhost:9000/api/v2/tickets-suporte/<ID_TICKET>/fechar
```

### 8. Testes de validação e erros

Todos os erros seguem o mesmo formato:

```json
{
  "timestamp": "2026-09-23T14:30:00",
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "Dados inválidos na requisição",
  "caminho": "/api/v2/doadoras",
  "campos": { "cpf": "CPF deve ter 11 dígitos" }
}
```

| Cenário | Como provocar | Resposta esperada |
|---|---|---|
| Campo obrigatório ausente ou inválido | `POST /doadoras` com `"cpf": "123"` | **400** com a lista de `campos` |
| JSON malformado ou enum inexistente | `"tipoProfissional": "MEDICO"` | **400** |
| UUID inválido na URL | `GET /api/v2/doadoras/abc` | **400** |
| Regra de negócio | Cadastrar o mesmo CPF duas vezes ou uma doadora menor de idade | **400** |
| Recurso inexistente | `GET /api/v2/doadoras/00000000-0000-0000-0000-000000000000` | **404** |
| Estado inválido | Agendar coleta para doadora `PENDENTE` ou enviar mensagem em ticket `FECHADO` | **409** |
| Integridade | Excluir uma doadora que já possui triagem ou coleta | **409** |

---
 
## 📦 Dependências Principais
 
Extraídas do `pom.xml` (dependências internas do Spring Boot Starter Parent não são listadas individualmente):
 
- `spring-boot-starter-data-jpa`
- `spring-boot-starter-validation`
- `spring-boot-starter-webmvc`
- `springdoc-openapi-starter-webmvc-ui` (3.1.0)
- `spring-boot-devtools` (runtime, opcional)
- `mysql-connector-j` (runtime)
- `lombok` (opcional)
- Dependências de teste: `spring-boot-starter-data-jpa-test`, `spring-boot-starter-validation-test`, `spring-boot-starter-webmvc-test`
---
 
## 🚀 Possíveis Melhorias
 
- Adicionar testes automatizados de unidade e integração (atualmente há apenas o teste de contexto padrão do Spring Boot)
- Implementar autenticação e autorização (não há camada de segurança no código atual)
- Adicionar tratamento global de exceções (`@ControllerAdvice`) para padronizar respostas de erro
- Implementar CI/CD para build e deploy automatizados
- Externalizar credenciais sensíveis do `application.properties` (usuário/senha do banco) via variáveis de ambiente
- Adicionar paginação nos endpoints de listagem (`findAll`)
---
 
## 👤 Autor
 
 [**joavlr03**](https://github.com/joavlr03).
