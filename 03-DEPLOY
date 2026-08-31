# Guia Completo: Deploy Contínuo de Spring Boot no Railway via GitHub

Este guia aborda o passo a passo para configurar uma pipeline de entrega contínua (CD), onde cada `push` na branch principal do GitHub dispara automaticamente o build e o deploy da sua aplicação Spring Boot no Railway.

---

## 1. Pré-requisitos

* Conta ativa no [GitHub](https://github.com) com o repositório do projeto.
* Conta ativa no [Railway](https://railway.app) (login via GitHub recomendado).
* Projeto Spring Boot (Java 17+ ou 21+) configurado com Maven (`pom.xml`) ou Gradle (`build.gradle`).

---

## 2. Preparação do Projeto Spring Boot

O Railway injeta dinamicamente a porta de execução através da variável de ambiente `PORT`. O Spring Boot precisa escutar essa porta.

### 2.1. Ajuste no `application.properties` ou `application.yml`

**`src/main/resources/application.properties`**:

```properties
server.port=${PORT:8080}

```

**`src/main/resources/application.yml`**:

```yaml
server:
  port: ${PORT:8080}

```

### 2.2. Configuração de Banco de Dados (Exemplo com PostgreSQL)

Caso utilize PostgreSQL fornecido pelo Railway:

```properties
spring.datasource.url=${DATABASE_URL:jdbc:postgresql://localhost:5432/postgres}
spring.datasource.username=${PGUSER:postgres}
spring.datasource.password=${PGPASSWORD:postgres}
spring.jpa.hibernate.ddl-auto=update

```

---

## 3. Estratégia de Build: Dockerfile (Opcional, mas Recomendado)

O Railway detecta projetos Java automaticamente via **Nixpacks**. No entanto, um `Dockerfile` multi-stage garante controle total sobre a versão do JDK e reduz o tamanho da imagem final.

Crie um arquivo chamado `Dockerfile` na raiz do repositório:

```dockerfile
# Estágio 1: Build da aplicação
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Estágio 2: Execução
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

```

> **Para Gradle:** Substitua o estágio de build por `gradle:jdk21-alpine` e o caminho do artefato para `/app/build/libs/*.jar`.

---

## 4. Criando e Configurando o Projeto no Railway

### Passo 1: Criar novo projeto

1. Acesse o dashboard do [Railway](https://railway.app).
2. Clique em **New Project** > **Deploy from GitHub repo**.
3. Selecione o repositório da sua aplicação Spring Boot.

### Passo 2: Adicionar Banco de Dados (se aplicável)

1. No painel do projeto, clique em **+ New** > **Database** > **Add PostgreSQL**.
2. O Railway provisionará o banco e criará variáveis padrão (`DATABASE_URL`, `PGUSER`, `PGPASSWORD`, etc.).

### Passo 3: Configurar Variáveis de Ambiente

1. Clique no card do serviço da sua aplicação Spring Boot.
2. Acesse a aba **Variables**.
3. Se estiver usando o banco do Railway, adicione uma variável de referência:
* Chave: `DATABASE_URL`
* Valor: `${{Postgres.DATABASE_URL}}`


4. Adicione quaisquer outras variáveis de produção necessárias (ex: `SPRING_PROFILES_ACTIVE=prod`, chaves de API, secrets JWT).

### Passo 4: Gerar Domínio Público

1. No serviço da aplicação, vá até a aba **Settings**.
2. Na seção **Networking**, clique em **Generate Domain**.
3. Um endereço público no formato `seu-projeto.up.railway.app` será criado.

---

## 5. Configuração do Deploy Contínuo (CI/CD)

O Railway conecta diretamente via Webhook ao GitHub:

1. Na aba **Settings** do serviço, localize a seção **Service**.
2. Em **Source Repo**, confirme que o repositório correto está vinculado.
3. Em **Branch**, selecione a branch alvo (normalmente `main` ou `master`).
4. Ative a opção **Automatic Deployments**.

---

## 6. Fluxo de Trabalho e Teste

### Testando o Gatilho Automático

1. Faça uma alteração no código localmente:
```bash
git add .
git commit -m "feat: health check endpoint"
git push origin main

```


2. Acesse o Railway e abra a aba **Deployments**.
3. Observe o build iniciar automaticamente. O log exibirá a compilação do Maven/Gradle ou a execução do Dockerfile.
4. Após o status mudar para **Active**, acesse a URL pública gerada para validar o funcionamento.

---

## 7. Troubleshooting Comum

| Problema | Causa Provável | Solução |
| --- | --- | --- |
| **Crash no startup (Port binding)** | Aplicação fixou a porta `8080` e ignorou a porta do ambiente. | Garanta o uso de `server.port=${PORT:8080}`. |
| **Connection Refused no Banco** | Tentativa de conexão antes do container do banco inicializar. | Verifique as variáveis de conexão `${{Postgres.DATABASE_URL}}`. |
| **Out of Memory (OOM)** | JVM consumindo mais memória que o limite da instância. | Adicione a variável `JAVA_TOOL_OPTIONS` com o valor `-Xmx384m -Xss512k`. |
