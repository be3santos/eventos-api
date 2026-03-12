package com.eventos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

/**
 * DTO para criação de evento.
 * Campos recebidos no corpo da requisição POST /events.
 */
public record CreateEventRequest(
    @NotBlank String title,
    String description,
    @NotNull LocalDateTime date,
    String location,
    String category,
    @NonNull @NotNull Long createdById
) {
}
