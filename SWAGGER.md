# Documentação Swagger / OpenAPI

## 1. Configuração

O projeto utiliza **SpringDoc OpenAPI**, biblioteca compatível com Spring Boot 3 que gera documentação Swagger a partir dos controllers.

### Dependência (pom.xml)

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

### SecurityConfig

Os endpoints do Swagger foram liberados no Spring Security:

- `/swagger-ui/**` — interface gráfica
- `/v3/api-docs/**` — especificação OpenAPI em JSON

---

## 2. Como acessar a documentação

Com a aplicação rodando (`mvn spring-boot:run` ou pela IDE):

### Swagger UI (interface interativa)

Tente estas URLs (uma delas deve funcionar):

```
http://localhost:8080/swagger-ui/index.html
```

```
http://localhost:8080/swagger-ui.html
```

```
http://localhost:8080/swagger-ui
```

### Especificação OpenAPI (JSON)

```
http://localhost:8080/v3/api-docs
```

---

## 3. Uso do Swagger UI

1. **Navegação**: Cada controller aparece como tag (Autenticação, Usuários, Eventos, Inscrições).
2. **Testar endpoints**: Clique no endpoint → "Try it out" → preencha os parâmetros → "Execute".
3. **Autenticação JWT**:
   - Faça login em `POST /auth/login` com email e senha.
   - Copie o valor do campo `token` da resposta.
   - Clique em **Authorize** (cadeado no topo).
   - Cole o token (sem "Bearer ") ou use o formato `Bearer <token>`.
   - Clique em "Authorize" e "Close".
   - Requisições protegidas passarão o token automaticamente.

---

## 4. Endpoints documentados

| Tag | Endpoints |
|-----|-----------|
| **Autenticação** | POST /auth/login |
| **Usuários** | POST /users, GET /users/{id} |
| **Eventos** | POST /events, GET /events, PUT /events/{id}, DELETE /events/{id} |
| **Inscrições** | POST /registrations, DELETE /registrations/{id} |

---

## 5. Anotações OpenAPI usadas

| Anotação | Uso |
|----------|-----|
| `@Tag` | Agrupa endpoints por controller |
| `@Operation` | Descrição e resumo do endpoint |
| `@ApiResponse` | Códigos HTTP e descrições de resposta |
| `@Parameter` | Descrição de parâmetros de path/query |
