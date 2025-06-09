# Sistema de Processamento de Pedidos

Este sistema representa uma arquitetura simplificada para um processo de pedidos, onde um cliente interage com um microserviço responsável pelo processamento.

![Diagrama da Arquitetura](https://raw.githubusercontent.com/fortunecapitalizacoes/Pedidos/refs/heads/main/Pedido.jpg)

## Visão Geral

### Protocolos Utilizados

- **HTTP/HTTPS**: Comunicação síncrona entre serviços (ex: chamadas REST via API Gateway).
- **AMQP**: Protocolo usado pelo RabbitMQ para comunicação assíncrona via filas de mensagens.
- **TCP/IP**: Comunicação de baixo nível entre containers gerenciada pelo Docker.
- **gRPC/HTTP2**: Protocolo de comunicação para chamadas de procedimentos remotos.

### 1. Cliente
- **Tipo:** Pessoa ou Bot
- **Descrição:** Este cliente pode ser um usuário humano ou um agente automatizado que realiza operações no sistema.

### 2. Operações Realizadas pelo Cliente
- GRUD

### 3. Microserviço: `pedidos-app`
- **Tecnologia:** Spring Boot
- **Descrição:** Microserviço responsável pelo processamento de pedidos.
- **Responsabilidades:**
  - Receber requisições do cliente
  - Processar pedidos
  - atualizar status do pedido

## Tecnologias Utilizadas
- Java
- Spring Boot
- Arquitetura baseada em microserviços
---

## 🚀 Como subir o projeto localmente

### Pré-requisitos

- Docker + Docker Compose instalados
- Java 17+ instalado

### Passo 1 – Clonar o repositório

```bash
git clone https://github.com/fortunecapitalizacoes/Pagamentos.git
cd Pagamentos
```

### Passo 2 – Subir a infraestrutura

O projeto já contém um arquivo `docker-compose.yml` na raiz, com toda a infraestrutura necessária (RabbitMQ, PostgreSQL, etc). Basta executar:

```bash
docker-compose up -d
```

Aguarde alguns segundos até que os contêineres estejam completamente iniciados.

### Passo 3 – Rodar a aplicação Spring Boot

Com os contêineres rodando, inicie o projeto Spring Boot:

```bash
./mvnw spring-boot:run
```

Ou, se estiver usando o Maven instalado:

```bash
mvn spring-boot:run
```
### Projeto Possui Swagger
> O projeto será iniciado na porta padrão (geralmente `8081`, verifique em `application.yml`).

---

## Veja o projeto de pagamentos

https://github.com/fortunecapitalizacoes/Pagamentos




