# Autenticação JWT - Guia Didático

## 1. Como funciona autenticação com JWT

### O que é JWT?

**JWT (JSON Web Token)** é um padrão que representa informações em JSON de forma compacta e assinada. Em APIs REST, ele substitui sessões: em vez de guardar o estado no servidor, o cliente recebe um token e o envia a cada requisição.

### Estrutura do token

Um JWT tem 3 partes separadas por ponto (`.`):

```
header.payload.signature
```

1. **Header**: algoritmo e tipo (ex: HS256, JWT)
2. **Payload**: dados (claims) como `sub` (subject), `exp` (expiration), `userId`, etc.
3. **Signature**: assinatura para garantir que o token não foi alterado

### Fluxo de autenticação

```
1. Cliente envia POST /auth/login com email e senha
2. Servidor valida credenciais
3. Servidor gera JWT e retorna no corpo da resposta
4. Cliente guarda o token (localStorage, memória, etc.)
5. Em requisições protegidas, cliente envia: Authorization: Bearer <token>
6. Servidor valida o token e permite ou nega o acesso
```

### Por que stateless?

- **Sem sessão no servidor**: cada requisição é independente
- **Escalável**: qualquer instância da API pode validar o token
- **Token autocontido**: o payload traz as informações necessárias (ex: userId, email)

---

## 2. Configuração de segurança (SecurityConfig)

### @EnableWebSecurity
Habilita o Spring Security na aplicação.

### SecurityFilterChain
Define o comportamento da segurança:

| Configuração | Significado |
|--------------|-------------|
| `csrf().disable()` | Desativa CSRF (APIs stateless usam JWT, não cookies) |
| `sessionCreationPolicy(STATELESS)` | Sem sessão HTTP; o estado vem do token |
| `authorizeHttpRequests()` | Define regras de acesso por URL |
| `addFilterBefore(jwtFilter, ...)` | Insere o filtro JWT antes da autenticação padrão |

### Endpoints configurados

| Endpoint | Acesso |
|----------|--------|
| `POST /auth/login` | Público (qualquer um pode tentar login) |
| `POST /users` | Público (cadastro sem login) |
| Demais endpoints | Exigem token JWT válido |

### Beans importantes

- **PasswordEncoder**: BCrypt para hash de senhas
- **AuthenticationManager**: usado no login para validar credenciais

---

## 3. Login de usuário

### AuthController
- **POST /auth/login**: recebe email e senha no JSON
- Chama `AuthService.login()`

### AuthService
1. Usa `AuthenticationManager.authenticate()` com email e senha
2. Se a senha estiver correta (comparada com o hash BCrypt), a autenticação passa
3. Busca o usuário no banco
4. Gera o JWT com `JwtUtil.generateToken()`
5. Retorna o token + dados do usuário

### Exemplo de requisição

```http
POST /auth/login
Content-Type: application/json

{
  "email": "joao@email.com",
  "password": "senha123"
}
```

### Resposta de sucesso (200)

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "userId": 1,
  "email": "joao@email.com",
  "name": "João Silva"
}
```

---

## 4. Geração do token JWT

### JwtUtil
- **generateToken(email, userId)**: cria o token com:
  - `sub` (subject): email
  - `userId`: ID do usuário
  - `iat`: data de emissão
  - `exp`: data de expiração (configurável em `jwt.expiration`)

- **validateToken(token, email)**: verifica assinatura e expiração
- **getEmailFromToken(token)** e **getUserIdFromToken(token)**: leem dados do payload

### application.properties

```properties
jwt.secret=chaveSecretaLonga
jwt.expiration=86400000
```

- `jwt.secret`: chave usada na assinatura (manter em segredo)
- `jwt.expiration`: duração em ms (86400000 = 24h)

---

## 5. Proteção dos endpoints

### JwtAuthenticationFilter
Filtro que roda em toda requisição:

1. Lê o header `Authorization: Bearer <token>`
2. Extrai o token
3. Obtém o email do token via `JwtUtil`
4. Carrega `UserDetails` com `UserDetailsService`
5. Valida o token
6. Se válido, define a autenticação em `SecurityContextHolder`

Com isso, qualquer endpoint que exige `authenticated()` passa a exigir um token JWT válido.

### Como enviar o token

```http
GET /events
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### Respostas de erro

- **401 Unauthorized**: token ausente, inválido ou expirado
- **403 Forbidden**: token válido, mas sem permissão para o recurso

---

## 6. Fluxo resumido

```
[Cliente]                    [API]

  POST /auth/login  ------>  AuthController
  {email, password}            |
                               v
                         AuthService.login()
                               |
                               v
                         AuthenticationManager valida
                         (BCrypt compara senhas)
                               |
                               v
                         JwtUtil.generateToken()
                               |
  <------ 200 {token, ...}  JwtUtil
  Cliente guarda o token


  GET /events  ---------->  JwtAuthenticationFilter
  Authorization: Bearer token   |
                                v
                         Extrai e valida token
                                |
                                v
                         SecurityContextHolder
                         .setAuthentication(...)
                                |
                                v
                         EventController.listAll()
                                |
  <------ 200 [eventos]   Retorna dados
```
