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
 
1. Clonar o repositório
```bash
git clone https://github.com/joavlr03/nutrilink-api.git
cd nutrilink-api
```
 
2. Criar o banco de dados MySQL `nutrilink` (ou ajustar `spring.datasource.url` em `src/main/resources/application.properties`)
  docker run -d --name mysql --rm -e MYSQL_ROOT_PASSWORD=root_pwd -e MYSQL_USER=new_user -e MYSQL_PASSWORD=my_pwd -p 3306:3306 mysql

4. Configurar as credenciais de acesso ao banco em `application.properties`, se necessário:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/nutrilink
spring.datasource.username=root
spring.datasource.password=root_pwd
```
 
4. Executar a aplicação com o Maven Wrapper
```bash
./mvnw spring-boot:run
```
 
5. A API sobe na porta configurada em `server.port` (**9000**). A documentação Swagger UI fica disponível na raiz (`springdoc.swagger-ui.path=/`):
```
http://localhost:9000/
```
 
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
