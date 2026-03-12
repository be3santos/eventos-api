package com.eventos.repository;

import com.eventos.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositório para acesso aos dados de User.
 * Extende JpaRepository e herda operações CRUD automáticas.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca um usuário pelo e-mail (único no sistema).
     * Útil para login e validação de cadastro.
     */
    boolean existsByEmail(String email);

    /**
     * Retorna o usuário com o e-mail informado, ou Optional.empty() se não existir.
     */
    Optional<User> findByEmail(String email);
}
