package com.eventos.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;

/**
 * DTO para criação de inscrição.
 * Campos recebidos no corpo da requisição POST /registrations.
 */
public record CreateRegistrationRequest(@NonNull @NotNull Long userId, @NonNull @NotNull Long eventId) {
}
