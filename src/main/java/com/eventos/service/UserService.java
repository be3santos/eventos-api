package com.eventos.service;

import com.eventos.model.User;
import com.eventos.repository.UserRepository;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável pelas regras de negócio relacionadas a usuários.
 * Usa injeção de dependência para obter o UserRepository.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Cria um novo usuário.
     * A senha é hasheada com BCrypt antes de salvar.
     *
     * @throws IllegalArgumentException se o e-mail já estiver cadastrado
     */
    public User create(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado: " + user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Busca usuário por ID.
     *
     * @throws IllegalArgumentException se o usuário não for encontrado
     */
    @SuppressWarnings("null")
    public @NonNull User findById(@NonNull Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com id: " + id));
    }
}
