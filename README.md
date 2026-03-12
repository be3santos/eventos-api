# Eventos API 🎉

API REST para gestão de eventos com autenticação JWT e integração com PostgreSQL.

## 📋 Descrição

Plataforma de eventos que permite:
- ✅ Cadastrar e autenticar usuários
- ✅ Criar, editar e deletar eventos
- ✅ Inscrever e cancelar inscrições em eventos
- ✅ Autenticação segura com JWT

---

## 🛠️ Tecnologias

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security + JWT**
- **PostgreSQL**
- **SpringDoc OpenAPI (Swagger UI)**
- **Maven**

---

## 🚀 Como rodar

### 1. **Pré-requisitos**
- Java 17+
- PostgreSQL instalado
- Maven 3.8+

### 2. **Configurar banco de dados**

Edite `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/eventos_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

### 3. **Rodar a aplicação**

```bash
mvn spring-boot:run
```

A API estará disponível em: **http://localhost:8080**

### 4. **Acessar documentação**

Abra no navegador: **http://localhost:8080/swagger-ui.html**

---

## 📚 Endpoints

### **Autenticação**
- `POST /auth/login` - Login e geração de token JWT

### **Usuários**
- `POST /users` - Criar novo usuário
- `GET /users/{id}` - Buscar usuário por ID

### **Eventos**
- `POST /events` - Criar novo evento
- `GET /events` - Listar todos os eventos
- `PUT /events/{id}` - Atualizar evento
- `DELETE /events/{id}` - Deletar evento

### **Inscrições**
- `POST /registrations` - Inscrever em evento
- `DELETE /registrations/{id}` - Cancelar inscrição
- `GET /registrations/user/{userId}` - Listar eventos do usuário

---

## 🔑 Fluxo de Autenticação

1. **Criar usuário**
```json
POST /users
{
  "name": "João",
  "email": "joao@email.com",
  "password": "senha123"
}
```

2. **Fazer login**
```json
POST /auth/login
{
  "email": "joao@email.com",
  "password": "senha123"
}
```

Resposta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "userId": 1,
  "email": "joao@email.com",
  "name": "João"
}
```

3. **Usar token** - Copie o token e clique no 🔒 no Swagger UI

---

## 📝 Exemplo de criação de evento

```json
POST /events
{
  "title": "Workshop de Java",
  "description": "Workshop sobre Spring Boot",
  "date": "2026-04-15T10:00:00",
  "location": "São Paulo",
  "category": "Tecnologia",
  "createdById": 1
}
```

---

## 🗄️ Estrutura do Projeto

```
src/main/java/com/eventos/
├── config/              # Configurações (OpenAPI/Swagger)
├── controller/          # Controladores REST
├── dto/                 # Data Transfer Objects
├── model/               # Modelos JPA
├── repository/          # Repositórios Spring Data
├── security/            # Segurança (JWT, filtros)
├── service/             # Lógica de negócio
└── EventosApplication.java  # Classe principal
```

---

## 🔐 Segurança

- Senhas criptografadas com **BCrypt**
- Tokens JWT com expiração configurável
- Endpoints protegidos requerem autenticação
- Validação de entrada com `@Valid`

---

## 📖 Documentação Adicional

Veja os arquivos de documentação:
- `JWT_AUTH.md` - Detalhes de autenticação
- `CONTROLLERS.md` - Endpoints detalhados
- `ENTIDADES_JPA.md` - Modelos de dados
- `REPOSITORIES.md` - Queries customizadas
- `SERVICES.md` - Lógica de negócio
- `SWAGGER.md` - Configuração OpenAPI

---

## 👤 Autor

**Bernardo Santos**

---

## 📄 Licença

Este projeto está sob a licença MIT.
