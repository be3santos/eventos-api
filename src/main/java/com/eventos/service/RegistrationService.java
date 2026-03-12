package com.eventos.service;

import com.eventos.model.Event;
import com.eventos.model.Registration;
import com.eventos.model.User;
import com.eventos.repository.RegistrationRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço responsável pelas regras de negócio de inscrições em eventos.
 * Depende de UserService e EventService para validar usuário e evento.
 */
@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final UserService userService;
    private final EventService eventService;

    /**
     * Injeção de dependência via construtor.
     * RegistrationService precisa validar user e event antes de criar a inscrição.
     */
    public RegistrationService(RegistrationRepository registrationRepository,
                               UserService userService,
                               EventService eventService) {
        this.registrationRepository = registrationRepository;
        this.userService = userService;
        this.eventService = eventService;
    }

    /**
     * Inscreve um uário em um evento.
     * Regras de negócio: usuário e evento devem existir; usuário não pode estar já inscrito.
     *
     * @throws IllegalArgumentException se usuário já estiver inscrito
     */
    public Registration register(@NonNull Long userId, @NonNull Long eventId) {
        if (registrationRepository.existsByUserIdAndEventId(userId, eventId)) {
            throw new IllegalArgumentException("Usuário já inscrito neste evento");
        }
        User user = userService.findById(userId);
        Event event = eventService.findById(eventId);
        Registration registration = new Registration(user, event);
        return registrationRepository.save(registration);
    }

    /**
     * Cancela inscrição pelo ID.
     *
     * @throws IllegalArgumentException se a inscrição não existir
     */
    @SuppressWarnings("null")
    public void deleteById(@NonNull Long id) {
        Registration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inscrição não encontrada com id: " + id));
        registrationRepository.delete(registration);
    }

    /**
     * Cancela a inscrição de um usuário em um evento.
     *
     * @throws IllegalArgumentException se a inscrição não existir
     */
    @SuppressWarnings("null")
    public void cancel(@NonNull Long userId, @NonNull Long eventId) {
        Registration registration = registrationRepository
                .findByUserIdAndEventId(userId, eventId)
                .orElseThrow(() -> new IllegalArgumentException("Inscrição não encontrada"));
        registrationRepository.delete(registration);
    }

    /**
     * Lista os eventos em que o uério está inscrito.
     */
    public List<Event> listEventsByUser(@NonNull Long userId) {
        userService.findById(userId); // Valida que o usuário existe
        return registrationRepository.findByUserIdOrderByRegistrationDateDesc(userId)
                .stream()
                .map(Registration::getEvent)
                .toList();
    }
}
