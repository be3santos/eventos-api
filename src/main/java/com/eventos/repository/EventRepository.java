package com.eventos.repository;

import com.eventos.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repositório para acesso aos dados de Event.
 * Inclui consultas personalizadas por categoria e localização (cidade).
 */
public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * Busca eventos por categoria (correspondência exata).
     * Spring Data JPA gera automaticamente: SELECT * FROM events WHERE category = ?
     */
    List<Event> findByCategory(String category);

    /**
     * Busca eventos por localização/cidade (correspondência exata).
     * SELECT * FROM events WHERE location = ?
     */
    List<Event> findByLocation(String location);

    /**
     * Busca eventos cuja localização contenha o texto informado.
     * Útil para busca parcial (ex: "São Paulo" encontra "São Paulo - Centro").
     * Spring gera: SELECT * FROM events WHERE location ILIKE '%' || ? || '%' (no PostgreSQL)
     */
    List<Event> findByLocationContainingIgnoreCase(String location);

    /**
     * Exemplo com @Query: busca eventos futuros por categoria.
     * Usa JPQL (linguagem orientada a objetos, não SQL puro).
     */
    @Query("SELECT e FROM Event e WHERE e.category = :category AND e.date > CURRENT_TIMESTAMP")
    List<Event> findUpcomingByCategory(@Param("category") String category);
}
