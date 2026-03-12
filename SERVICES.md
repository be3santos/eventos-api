# Camada de Service

## 1. Papel da camada de serviço

A camada **Service** concentra as **regras de negócio** da aplicação. Ela:

| Responsabilidade | Exemplo |
|------------------|---------|
| **Orquestrar operações** | Cria evento verificando se o criador existe |
| **Validar regras** | Impedir e-mail duplicado, inscrição duplicada |
| **Centralizar lógica** | Converter inscrições em lista de eventos |
| **Desacoplar Controller e Repository** | O controller não conhece o banco; o repository não conhece regras de negócio |

### Fluxo da requisição

```
Controller (recebe HTTP) → Service (aplica regras) → Repository (acessa dados)
```

O Service recebe os dados do Controller, aplica as validações, chama os Repositories necessários e devolve o resultado. Se algo falhar nas regras de negócio, lança exceção com mensagem adequada.

---

## 2. Injeção de dependência

### O que é?

Em vez de criar objetos manualmente (`new UserRepository()`), o Spring **injeta** as dependências nas classes. Isso:

- Facilita trocar implementações (por exemplo, em testes)
- Garante uma única instância (Singleton) dos beans
- Deixa o código mais flexível e testável

### Formas de injeção

| Forma | Exemplo | Recomendação |
|-------|---------|--------------|
| **Construtor** | `public UserService(UserRepository repo) { ... }` | Preferida |
| Campo | `@Autowired private UserRepository repo;` | Evitar |
| Setter | `@Autowired public void setRepo(...)` | Quando opcional |

### Por que construtor?

```java
public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
}
```

- Dependências explícitas e obrigatórias
- Facilita testes (passar mocks no construtor)
- Permite `userRepository` ser `final` (imutável)
- Spring Boot 2.6+ não exige `@Autowired` em construtor único

### Grafo de dependências

```
RegistrationService
    ├── RegistrationRepository
    ├── UserService
    │       └── UserRepository
    └── EventService
            ├── EventRepository
            └── UserService
```

O Spring resolve o grafo e instancia os beans na ordem correta.

---

## 3. Resumo dos services

### UserService

| Método | Descrição | Regra de negócio |
|--------|-----------|------------------|
| `create(User)` | Cria usuário | E-mail único |
| `findById(Long)` | Busca por ID | Lança exceção se não existir |

### EventService

| Método | Descrição | Regra de negócio |
|--------|-----------|------------------|
| `create(Event, Long)` | Cria evento | Validar criador |
| `update(Long, Event)` | Atualiza evento | Evento deve existir |
| `delete(Long)` | Remove evento | Evento deve existir |
| `listAll()` | Lista todos | — |
| `findById(Long)` | Busca por ID | Lança exceção se não existir |

### RegistrationService

| Método | Descrição | Regra de negócio |
|--------|-----------|------------------|
| `register(Long, Long)` | Inscreve em evento | User e Event existem; sem inscrição duplicada |
| `cancel(Long, Long)` | Cancela inscrição | Inscrição deve existir |
| `listEventsByUser(Long)` | Eventos inscritos | Usuário deve existir |
