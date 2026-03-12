# Modelagem JPA - API de Gestão de Eventos

## 1. Relacionamentos entre as entidades

### Diagrama conceitual

```
    User (1) ──────────────── (N) Event
      │                            │
      │ createdBy                  │
      │                            │
      │         Registration       │
      └─────────── (N) ─── (N) ────┘
                  user    event
```

### Resumo dos relacionamentos

| Entidade A | Relacionamento | Entidade B | Significado |
|------------|----------------|------------|-------------|
| User | **1 : N** | Event | Um usuário pode criar vários eventos |
| Event | **N : 1** | User | Cada evento é criado por um usuário |
| User | **1 : N** | Registration | Um usuário pode ter várias inscrições |
| Registration | **N : 1** | User | Cada inscrição pertence a um usuário |
| Event | **1 : N** | Registration | Um evento pode ter várias inscrições |
| Registration | **N : 1** | Event | Cada inscrição é em um evento |

### Regra de negócio implícita

- **User ↔ Event (via Registration)**: relação **N : N** — um usuário se inscreve em muitos eventos e um evento tem muitos usuários inscritos.
- A entidade **Registration** é a tabela associativa que modela essa N:N e permite guardar dados extras (`registrationDate`).

---

## 2. Anotações JPA utilizadas

### `@Entity`
- Marca a classe como entidade JPA (mapeada para uma tabela).
- O JPA gerencia o ciclo de vida dos objetos dessa classe.

### `@Table(name = "...")`
- Define o nome da tabela no banco.
- Sem isso, o padrão é o nome da classe em snake_case.

### `@Id`
- Indica o campo que é a chave primária (PK).

### `@GeneratedValue(strategy = GenerationType.IDENTITY)`
- Gera automaticamente o ID no banco (auto-incremento).
- `IDENTITY`: usa o mecanismo nativo do banco (SERIAL no PostgreSQL).

### `@Column`
- Mapeia o atributo para uma coluna.
- Parâmetros comuns:
  - `nullable = false`
  - `unique = true`
  - `length = N`
  - `name = "nome_coluna"`
  - `columnDefinition = "TEXT"` (tipo SQL customizado)

### `@ManyToOne`
- Indica relacionamento **N : 1** (várias entidades apontam para uma).
- A entidade que tem `@ManyToOne` guarda a FK.
- Ex.: `Event` tem `@ManyToOne User createdBy` → coluna `created_by_id`.

### `@OneToMany(mappedBy = "...")`
- Indica relacionamento **1 : N** (uma entidade possui várias).
- `mappedBy`: nome do campo na entidade filha que faz o vínculo.
- O lado `@OneToMany` não possui coluna FK; ela fica no lado `@ManyToOne`.

### `@JoinColumn(name = "coluna_fk")`
- Define o nome da coluna de FK.
- Usado junto com `@ManyToOne` ou `@OneToOne`.

### `@UniqueConstraint`
- Cria restrição UNIQUE no banco.
- Ex.: em `Registration`, `(user_id, event_id)` impede inscrição duplicada.

### `FetchType.LAZY`
- Carrega o relacionamento só quando for acessado (lazy loading).
- Reduz a quantidade de dados carregados por padrão.

### `CascadeType.ALL` e `orphanRemoval = true`
- **Cascade**: ao salvar/atualizar/remover a entidade pai, as filhas são propagadas.
- **orphanRemoval = true**: ao remover um filho da coleção, ele é excluído do banco.

### `@PrePersist`
- Método executado antes do primeiro `persist` no banco.
- Usado para preencher campos como `createdAt` e `registrationDate`.

---

## 3. Tabelas geradas no PostgreSQL

### `users`
| Coluna | Tipo |
|--------|------|
| id | BIGSERIAL (PK) |
| name | VARCHAR(100) NOT NULL |
| email | VARCHAR(255) NOT NULL UNIQUE |
| password | VARCHAR(255) NOT NULL |
| created_at | TIMESTAMP NOT NULL |

### `events`
| Coluna | Tipo |
|--------|------|
| id | BIGSERIAL (PK) |
| title | VARCHAR(200) NOT NULL |
| description | TEXT |
| date | TIMESTAMP NOT NULL |
| location | VARCHAR(200) |
| category | VARCHAR(100) |
| created_at | TIMESTAMP NOT NULL |
| created_by_id | BIGINT NOT NULL (FK → users.id) |

### `registrations`
| Coluna | Tipo |
|--------|------|
| id | BIGSERIAL (PK) |
| user_id | BIGINT NOT NULL (FK → users.id) |
| event_id | BIGINT NOT NULL (FK → events.id) |
| registration_date | TIMESTAMP NOT NULL |
| UNIQUE (user_id, event_id) | |
