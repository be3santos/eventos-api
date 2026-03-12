# Camada de Repositories - Spring Data JPA

## 1. O que é JpaRepository?

`JpaRepository<T, ID>` é uma interface do Spring Data JPA que combina várias funcionalidades:

- **CRUD básico**: `save()`, `findById()`, `findAll()`, `delete()`, `count()`, `existsById()`
- **Paginação e ordenação**: `findAll(Pageable)`, `findAll(Sort)`
- **Padrão Repository**: abstrai o acesso ao banco, deixando a camada de serviço mais limpa

### Hierarquia de interfaces

```
Repository (marcador)
    └── CrudRepository (CRUD básico)
            └── PagingAndSortingRepository (+ paginação/ordenação)
                    └── JpaRepository (flush, batch, etc.)
```

### Por que só declarar a interface?

O Spring Data JPA **implementa a interface em tempo de execução**. Você não precisa criar uma classe concreta: basta estender `JpaRepository<Entidade, TipoDoId>` e o Spring gera a implementação automaticamente.

```java
public interface UserRepository extends JpaRepository<User, Long> { }
// O Spring cria: UserRepositoryImpl com findAll(), save(), etc.
```

---

## 2. Consultas personalizadas

### Método por nome (Derived Query)

O Spring Data interpreta o nome do método e gera a query automaticamente:

| Nome do método | Query gerada (conceitual) |
|----------------|---------------------------|
| `findByCategory(String cat)` | `WHERE category = ?` |
| `findByLocation(String loc)` | `WHERE location = ?` |
| `findByLocationContainingIgnoreCase(String loc)` | `WHERE LOWER(location) LIKE LOWER('%' \|\| ? \|\| '%')` |
| `findByUser(User u)` | `WHERE user_id = ?` |
| `findByUserId(Long id)` | `WHERE user_id = ?` |
| `existsByUserIdAndEventId(Long u, Long e)` | `EXISTS (SELECT 1 WHERE user_id = ? AND event_id = ?)` |

**Palavras-chave comuns**: `And`, `Or`, `Between`, `LessThan`, `GreaterThan`, `Like`, `Containing`, `IgnoreCase`, `OrderBy`.

### Consulta com @Query (JPQL)

Quando o nome do método ficaria complexo ou a lógica é específica, use `@Query`:

```java
@Query("SELECT e FROM Event e WHERE e.category = :category AND e.date > CURRENT_TIMESTAMP")
List<Event> findUpcomingByCategory(@Param("category") String category);
```

- **JPQL**: usa nomes de entidades e atributos (ex: `Event`, `e.date`), não nomes de tabelas/colunas.
- `:category` é o parâmetro nomeado; `@Param("category")` liga ao argumento do método.

---

## 3. Consultas implementadas

### EventRepository

| Método | Descrição |
|--------|-----------|
| `findByCategory(String)` | Eventos por categoria (exata) |
| `findByLocation(String)` | Eventos por cidade/local (exata) |
| `findByLocationContainingIgnoreCase(String)` | Eventos cujo local contenha o texto (case-insensitive) |
| `findUpcomingByCategory(String)` | Eventos futuros por categoria (@Query JPQL) |

### RegistrationRepository

| Método | Descrição |
|--------|-----------|
| `findByUser(User)` | Inscrições de um usuário |
| `findByUserId(Long)` | Inscrições de um usuário (por ID) |
| `existsByUserIdAndEventId(Long, Long)` | Verifica se usuário está inscrito no evento |
| `findByUserIdAndEventId(Long, Long)` | Busca inscrição específica (para cancelamento) |
| `findByUserIdOrderByRegistrationDateDesc(Long)` | Inscrições ordenadas por data (@Query) |

### UserRepository

| Método | Descrição |
|--------|-----------|
| `findByEmail(String)` | Usuário por e-mail (login) |
| `existsByEmail(String)` | Verifica se e-mail já está cadastrado |
