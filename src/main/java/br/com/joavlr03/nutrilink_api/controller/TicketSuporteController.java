package br.com.joavlr03.nutrilink_api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.joavlr03.nutrilink_api.dto.ticketsuporte.TicketSuporteCreateRequest;
import br.com.joavlr03.nutrilink_api.dto.ticketsuporte.TicketSuporteMapper;
import br.com.joavlr03.nutrilink_api.dto.ticketsuporte.TicketSuporteResponse;
import br.com.joavlr03.nutrilink_api.model.TicketSuporte;
import br.com.joavlr03.nutrilink_api.model.enums.StatusTicket;
import br.com.joavlr03.nutrilink_api.service.TicketSuporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v2/tickets-suporte")
@Tag(name = "Tickets de Suporte", description = "Gerenciamento de chamados de suporte das doadoras")
public class TicketSuporteController {
     private final TicketSuporteService service;
    private final TicketSuporteMapper mapper;

    public TicketSuporteController(TicketSuporteService service, TicketSuporteMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Abrir ticket de suporte")
    public TicketSuporteResponse create(@RequestBody @Valid TicketSuporteCreateRequest request) {
        TicketSuporte ticket = mapper.toModel(request);
        ticket = service.create(ticket, request.getDoadoraId());
        return mapper.toDto(ticket);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar ticket por ID")
    public TicketSuporteResponse findById(@PathVariable UUID id) {
        return mapper.toDto(
            service.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket não encontrado: " + id))
        );
    }

    @GetMapping
    @Operation(summary = "Listar todos os tickets")
    public List<TicketSuporteResponse> findAll() {
        return service.findAll().stream()
                .map(t -> mapper.toDto(t))
                .toList();
    }

    @GetMapping("/doadora/{doadoraId}")
    @Operation(summary = "Listar tickets de uma doadora")
    public List<TicketSuporteResponse> findByDoadora(@PathVariable UUID doadoraId) {
        return service.findByDoadoraId(doadoraId).stream()
                .map(t -> mapper.toDto(t))
                .toList();
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Listar tickets por status")
    public List<TicketSuporteResponse> findByStatus(@PathVariable StatusTicket status) {
        return service.findByStatus(status).stream()
                .map(t -> mapper.toDto(t))
                .toList();
    }

    @PatchMapping("/{ticketId}/assumir/{profissionalId}")
    @Operation(summary = "Especialista assume o ticket")
    public TicketSuporteResponse assumirTicket(
            @PathVariable UUID ticketId,
            @PathVariable UUID profissionalId) {
        return mapper.toDto(service.assumirTicket(ticketId, profissionalId));
    }

    @PatchMapping("/{id}/fechar")
    @Operation(summary = "Fechar ticket de suporte")
    public TicketSuporteResponse fecharTicket(@PathVariable UUID id) {
        return mapper.toDto(service.fecharTicket(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir ticket")
    public void deleteById(@PathVariable UUID id) {
        service.deleteById(id);
    }
}
