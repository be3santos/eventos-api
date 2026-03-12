package com.eventos.service;

import com.eventos.model.Event;
import com.eventos.model.User;
import com.eventos.repository.EventRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço responsável pelas regras de negócio relacionadas a eventos.
 * Orquestra operações entre Event e User.
 */
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserService userService;

    /**
     * Injeção de dependência: EventService depende de EventRepository e UserService.
     * O Spring resolve toda a cadeia de dependências automaticamente.
     */
    public EventService(EventRepository eventRepository, UserService userService) {
        this.eventRepository = eventRepository;
        this.userService = userService;
    }

    /**
     * Cria um novo evento.
     * Regra de negócio: o criador deve existir no sistema.
     */
    public Event create(Event event, @NonNull Long createdById) {
        User creator = userService.findById(createdById);
        event.setCreatedBy(creator);
        return eventRepository.save(event);
    }

    /**
     * Atualiza um evento existente.
     *
     * @throws IllegalArgumentException se o evento não for encontrado
     */
    public Event update(@NonNull Long id, Event eventData) {
        Event event = findById(id);
        event.setTitle(eventData.getTitle());
        event.setDescription(eventData.getDescription());
        event.setDate(eventData.getDate());
        event.setLocation(eventData.getLocation());
        event.setCategory(eventData.getCategory());
        return eventRepository.save(event);
    }

    /**
     * Remove um evento.
     *
     * @throws IllegalArgumentException se o evento não for encontrado
     */
    public void delete(@NonNull Long id) {
        Event event = findById(id);
        eventRepository.delete(event);
    }

    /**
     * Lista todos os eventos.
     */
    public List<Event> listAll() {
        return eventRepository.findAll();
    }

    /**
     * Busca evento por ID.
     *
     * @throws IllegalArgumentException se o evento não for encontrado
     */
    @SuppressWarnings("null")
    public @NonNull Event findById(@NonNull Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento não encontrado com id: " + id));
    }
}
