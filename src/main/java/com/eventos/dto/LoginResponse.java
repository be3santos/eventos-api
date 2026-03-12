package com.eventos.dto;

/**
 * DTO de resposta do login.
 * Contém o token JWT e informações básicas do usuário.
 */
public record LoginResponse(
    String token,
    String type,
    Long userId,
    String email,
    String name
) {
    public static LoginResponse of(String token, Long userId, String email, String name) {
        return new LoginResponse(token, "Bearer", userId, email, name);
    }
}
