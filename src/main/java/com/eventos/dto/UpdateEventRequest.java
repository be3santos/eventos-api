package com.eventos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * DTO para atualização de evento.
 * Campos recebidos no corpo da requisição PUT /events/{id}.
 */
public record UpdateEventRequest(
    @NotBlank String title,
    String description,
    @NotNull LocalDateTime date,
    String location,
    String category
) {
}
