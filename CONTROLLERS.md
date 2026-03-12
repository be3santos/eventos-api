# Controllers REST - API de Gestão de Eventos

## 1. Anotações do Spring MVC

### @RestController
Combina `@Controller` + `@ResponseBody`. Indica que:
- A classe é um controller Spring MVC
- O retorno dos métodos é serializado automaticamente em JSON (não precisa de `@ResponseBody` em cada método)

### @RequestMapping("/path")
Define o prefixo da URL para todos os endpoints do controller.
- `@RequestMapping("/users")` → base: `/users`
- Com `@GetMapping("/{id}")` → endpoint final: `GET /users/{id}`

### @GetMapping, @PostMapping, @PutMapping, @DeleteMapping
Mapeiam métodos aos verbos HTTP:

| Anotação      | Verbo HTTP | Uso típico |
|---------------|------------|------------|
| `@GetMapping` | GET        | Buscar/Listar |
| `@PostMapping`| POST       | Criar      |
| `@PutMapping` | PUT        | Atualizar  |
| `@DeleteMapping` | DELETE  | Remover    |

### @PathVariable
Captura variável da URL:
```java
@GetMapping("/{id}")
public User getById(@PathVariable Long id)
```
Requisição: `GET /users/1` → `id = 1`

### @RequestBody
Deserializa o corpo JSON da requisição no objeto indicado:
```java
@PostMapping
public User create(@RequestBody CreateUserRequest request)
```
O Spring usa Jackson para converter JSON → objeto.

### @ResponseStatus(HttpStatus.CREATED)
Define o código HTTP da resposta:
- `201 CREATED` para POST que cria recurso
- `204 NO_CONTENT` para DELETE (corpo vazio)

### @Valid
Ativa validação Jakarta Bean Validation nos atributos do objeto:
- `@NotBlank`, `@NotNull`, `@Email` nos DTOs
- Se inválido → `400 Bad Request` com detalhes dos erros

---

## 2. Endpoints disponíveis

### UserController

| Método | Endpoint      | Descrição        |
|--------|---------------|------------------|
| POST   | /users        | Criar usuário    |
| GET    | /users/{id}   | Buscar por ID    |

### EventController

| Método | Endpoint       | Descrição      |
|--------|----------------|----------------|
| POST   | /events        | Criar evento   |
| GET    | /events        | Listar todos   |
| PUT    | /events/{id}   | Atualizar      |
| DELETE | /events/{id}   | Remover        |

### RegistrationController

| Método | Endpoint            | Descrição      |
|--------|---------------------|----------------|
| POST   | /registrations      | Criar inscrição|
| DELETE | /registrations/{id} | Cancelar       |

---

## 3. Exemplos de requisições JSON

### POST /users
```http
POST /users
Content-Type: application/json

{
  "name": "João Silva",
  "email": "joao@email.com",
  "password": "senha123"
}
```

**Resposta 201:**
```json
{
  "id": 1,
  "name": "João Silva",
  "email": "joao@email.com",
  "createdAt": "2025-03-10T14:30:00"
}
```
*(A senha não é retornada por @JsonIgnore)*

---

### GET /users/1
```http
GET /users/1
```

---

### POST /events
```http
POST /events
Content-Type: application/json

{
  "title": "Meetup Java",
  "description": "Encontro de desenvolvedores Java",
  "date": "2025-04-15T19:00:00",
  "location": "São Paulo - Centro",
  "category": "Tecnologia",
  "createdById": 1
}
```

---

### PUT /events/1
```http
PUT /events/1
Content-Type: application/json

{
  "title": "Meetup Java - Atualizado",
  "description": "Encontro de devs Java e Spring",
  "date": "2025-04-20T19:00:00",
  "location": "São Paulo - Vila Madalena",
  "category": "Tecnologia"
}
```

---

### POST /registrations
```http
POST /registrations
Content-Type: application/json

{
  "userId": 1,
  "eventId": 1
}
```

**Resposta 201:**
```json
{
  "id": 1,
  "user": { "id": 1, "name": "João Silva", "email": "joao@email.com", "createdAt": "..." },
  "event": { "id": 1, "title": "Meetup Java", "date": "...", ... },
  "registrationDate": "2025-03-10T15:00:00"
}
```

---

### DELETE /registrations/1
```http
DELETE /registrations/1
```
**Resposta 204** (sem corpo)
