package com.eventos.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO para criação de usuário.
 * Campos recebidos no corpo da requisição POST /users.
 */
public record CreateUserRequest(
    @NotBlank(message = "Nome é obrigatório") String name,
    @NotBlank @Email String email,
    @NotBlank(message = "Senha é obrigatória") String password
) {
}
