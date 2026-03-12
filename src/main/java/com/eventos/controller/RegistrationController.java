package com.eventos.controller;

import com.eventos.dto.CreateRegistrationRequest;
import com.eventos.model.Registration;
import com.eventos.service.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Inscrições", description = "Inscrever usuários em eventos e cancelar inscrições")
@RestController
@RequestMapping("/registrations")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Operation(summary = "Inscrever em evento", description = "Inscreve um usuário em um evento. Requer autenticação.")
    @ApiResponse(responseCode = "201", description = "Inscrição realizada com sucesso")
    @ApiResponse(responseCode = "400", description = "Usuário já inscrito ou dados inválidos")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Registration create(@Valid @RequestBody CreateRegistrationRequest request) {
        return registrationService.register(request.userId(), request.eventId());
    }

    @Operation(summary = "Cancelar inscrição", description = "Cancela uma inscrição pelo ID. Requer autenticação.")
    @ApiResponse(responseCode = "204", description = "Inscrição cancelada")
    @ApiResponse(responseCode = "404", description = "Inscrição não encontrada")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Parameter(description = "ID da inscrição") @NonNull @PathVariable Long id) {
        registrationService.deleteById(id);
    }
}
