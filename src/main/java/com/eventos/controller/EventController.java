package com.eventos.controller;

import com.eventos.dto.CreateEventRequest;
import com.eventos.dto.UpdateEventRequest;
import com.eventos.model.Event;
import com.eventos.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Eventos", description = "Criação, listagem, edição e exclusão de eventos")
@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Operation(summary = "Criar evento", description = "Cadastra um novo evento. Requer autenticação.")
    @ApiResponse(responseCode = "201", description = "Evento criado com sucesso")
    @ApiResponse(responseCode = "401", description = "Token não informado ou inválido")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Event create(@Valid @RequestBody CreateEventRequest request) {
        Event event = new Event(
                request.title(),
                request.description(),
                request.date(),
                request.location(),
                request.category(),
                null  // createdBy será definido pelo service
        );
        return eventService.create(event, request.createdById());
    }

    @Operation(summary = "Listar eventos", description = "Retorna todos os eventos cadastrados. Requer autenticação.")
    @ApiResponse(responseCode = "200", description = "Lista de eventos")
    @GetMapping
    public List<Event> listAll() {
        return eventService.listAll();
    }

    @Operation(summary = "Atualizar evento", description = "Atualiza um evento existente. Requer autenticação.")
    @ApiResponse(responseCode = "200", description = "Evento atualizado")
    @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    @PutMapping("/{id}")
    public Event update(@Parameter(description = "ID do evento") @NonNull @PathVariable Long id,
                        @Valid @RequestBody UpdateEventRequest request) {
        Event eventData = new Event();
        eventData.setTitle(request.title());
        eventData.setDescription(request.description());
        eventData.setDate(request.date());
        eventData.setLocation(request.location());
        eventData.setCategory(request.category());
        return eventService.update(id, eventData);
    }

    @Operation(summary = "Excluir evento", description = "Remove um evento. Requer autenticação.")
    @ApiResponse(responseCode = "204", description = "Evento excluído")
    @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Parameter(description = "ID do evento") @NonNull @PathVariable Long id) {
        eventService.delete(id);
    }
}
