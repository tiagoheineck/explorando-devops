# Projeto modelo de Observabilidade e CI/CD

Este repositório reúne uma aplicação Java Spring, infraestrutura com Docker Compose e pipeline de CI/CD para demonstrar um ambiente completo de observabilidade com Prometheus, Grafana e Graylog.

## Estrutura do projeto

```text
.
├── README.md
├── Vagrantfile
├── playbooks/
├── spring/
│   ├── Dockerfile
│   ├── docker-compose.yml
│   ├── pom.xml
│   ├── src/
│   ├── prometheus/
│   └── grafana/
└── .github/
    └── workflows/
```

## Pré-requisitos

- Docker
- Docker Compose
- Git
- Acesso à Internet para baixar imagens

## 1) Clonar o projeto

```bash
git clone <url-do-repositorio>
cd explorando-devops
```

## 2) Subir a stack completa

Entre na pasta da aplicação Spring e execute:

```bash
cd spring
docker compose up --build
```

Esse comando vai construir a imagem da aplicação Java e subir toda a pilha:

- Aplicação Spring em http://localhost:8080
- Prometheus em http://localhost:9090
- Grafana em http://localhost:3000
- Graylog em http://localhost:9000

## 3) Acessar os painéis

### Aplicação

```text
http://localhost:8080/
http://localhost:8080/hello
http://localhost:8080/simulate
http://localhost:8080/actuator/health
http://localhost:8080/actuator/prometheus
```

### Prometheus

Acesse:

```text
http://localhost:9090
```

No Prometheus, confirme que o job `spring-app` está sendo coletado corretamente.

### Grafana

Acesse:

```text
http://localhost:3000
```

Credenciais padrão:

```text
Usuário: admin
Senha: admin
```

Na interface do Grafana, confirme que a fonte de dados do Prometheus está disponível e o dashboard do JVM foi provisionado automaticamente.

### Graylog

Acesse:

```text
http://localhost:9000
```

Credenciais padrão:

```text
Usuário: admin
Senha: admin
```

O Graylog recebe os logs em UDP na porta 12201, então a aplicação pode enviar mensagens estruturadas em GELF sem necessidade de configuração extra.

## 4) Testar geração de métricas e logs

A aplicação expõe métricas do Actuator e Prometheus em `/actuator/prometheus` e também inclui endpoints para gerar eventos de log.

### Checar saúde da app

```bash
curl http://localhost:8080/actuator/health
```

### Ver métricas Prometheus

```bash
curl http://localhost:8080/actuator/prometheus | head
```

### Gerar tráfego e logs

```bash
curl http://localhost:8080/simulate
```

Esse endpoint gera mensagens `INFO`, `WARN` e `ERROR`, simulando um cenário real para observabilidade.

## 5) Pipeline de CI/CD

A pipeline do GitHub Actions está em:

```text
.github/workflows/ci-cd.yml
```

Ela realiza:

- checkout do código
- configuração do JDK 21
- execução dos testes com Maven
- build da imagem Docker
- login no registry e push da imagem

## 6) Parar e limpar os containers

```bash
docker compose down
```

Para remover volumes também:

```bash
docker compose down -v
```

## 7) Observações

- A aplicação foi desenhada para funcionar sem banco de dados local, focando em observabilidade e entrega contínua.
- O Graylog depende de MongoDB e OpenSearch para funcionar corretamente.
- A stack facilita aprendizado prático sobre CI/CD, monitoramento, dashboards e centralização de logs.
