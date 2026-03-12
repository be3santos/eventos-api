package com.eventos.controller;

import com.eventos.dto.CreateUserRequest;
import com.eventos.model.User;
import com.eventos.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Usuários", description = "Cadastro e consulta de usuários")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Criar usuário", description = "Cadastra um novo usuário. Endpoint público.")
    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso")
    @ApiResponse(responseCode = "400", description = "E-mail já cadastrado ou dados inválidos")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody CreateUserRequest request) {
        User user = new User(request.name(), request.email(), request.password());
        return userService.create(user);
    }

    @Operation(summary = "Buscar usuário por ID", description = "Retorna os dados do usuário. Requer autenticação.")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado")
    @ApiResponse(responseCode = "401", description = "Token não informado ou inválido")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @GetMapping("/{id}")
    public User getById(@Parameter(description = "ID do usuário") @NonNull @PathVariable Long id) {
        return userService.findById(id);
    }
}
