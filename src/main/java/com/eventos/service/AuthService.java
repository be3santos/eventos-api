package com.eventos.service;

import com.eventos.dto.LoginResponse;
import com.eventos.model.User;
import com.eventos.repository.UserRepository;
import com.eventos.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável pela autenticação (login).
 * Valida credenciais e gera o token JWT.
 */
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Autentica o usuário com e-mail e senha.
     * Se válido, gera e retorna o token JWT.
     */
    public LoginResponse login(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        String token = jwtUtil.generateToken(user.getEmail(), user.getId());

        return LoginResponse.of(token, user.getId(), user.getEmail(), user.getName());
    }
}
