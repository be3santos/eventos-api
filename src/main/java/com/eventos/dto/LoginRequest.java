package com.eventos.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO para requisição de login.
 */
public record LoginRequest(
    @NotBlank @Email String email,
    @NotBlank String password
) {
}
