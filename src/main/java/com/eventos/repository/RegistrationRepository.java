package com.eventos.repository;

import com.eventos.model.Registration;
import com.eventos.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para acesso aos dados de Registration.
 * Inclui consulta de inscrições por usuário.
 */
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    /**
     * Busca todas as inscrições de um usuário.
     * Spring Data JPA gera: SELECT * FROM registrations WHERE user_id = ?
     */
    List<Registration> findByUser(User user);

    /**
     * Versão alternativa: busca por ID do usuário (evita carregar o User antes).
     * Útil quando você já tem o userId disponível.
     */
    List<Registration> findByUserId(Long userId);

    /**
     * Verifica se o usuário já está inscrito no evento.
     * Evita inscrição duplicada (a constraint UNIQUE também protege no banco).
     */
    boolean existsByUserIdAndEventId(Long userId, Long eventId);

    /**
     * Busca uma inscrição específica por usuário e evento.
     * Útil para cancelamento de inscrição.
     */
    Optional<Registration> findByUserIdAndEventId(Long userId, Long eventId);

    /**
     * Exemplo com @Query JPQL: inscrições de um usuário ordenadas por data.
     */
    @Query("SELECT r FROM Registration r WHERE r.user.id = :userId ORDER BY r.registrationDate DESC")
    List<Registration> findByUserIdOrderByRegistrationDateDesc(@Param("userId") Long userId);
}
