package br.com.joavlr03.nutrilink_api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.joavlr03.nutrilink_api.dto.mensagemsuporte.MensagemSuporteCreateRequest;
import br.com.joavlr03.nutrilink_api.dto.mensagemsuporte.MensagemSuporteMapper;
import br.com.joavlr03.nutrilink_api.dto.mensagemsuporte.MensagemSuporteResponse;
import br.com.joavlr03.nutrilink_api.model.MensagemSuporte;
import br.com.joavlr03.nutrilink_api.service.MensagemSuporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v2/mensagens-suporte")
@Tag(name = "Mensagens de Suporte", description = "Gerenciamento de mensagens dos tickets de suporte")
public class MensagemSuporteController {
     private final MensagemSuporteService service;
    private final MensagemSuporteMapper mapper;

    public MensagemSuporteController(MensagemSuporteService service, MensagemSuporteMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Enviar mensagem no ticket")
    public MensagemSuporteResponse create(@RequestBody @Valid MensagemSuporteCreateRequest request) {
        MensagemSuporte mensagem = mapper.toModel(request);
        mensagem = service.create(mensagem, request.getTicketId());
        return mapper.toDto(mensagem);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar mensagem por ID")
    public MensagemSuporteResponse findById(@PathVariable UUID id) {
        return mapper.toDto(
            service.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mensagem não encontrada: " + id))
        );
    }

    @GetMapping
    @Operation(summary = "Listar todas as mensagens")
    public List<MensagemSuporteResponse> findAll() {
        return service.findAll().stream()
                .map(m -> mapper.toDto(m))
                .toList();
    }

    @GetMapping("/ticket/{ticketId}")
    @Operation(summary = "Listar mensagens de um ticket em ordem cronológica")
    public List<MensagemSuporteResponse> findByTicket(@PathVariable UUID ticketId) {
        return service.findByTicketId(ticketId).stream()
                .map(m -> mapper.toDto(m))
                .toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir mensagem")
    public void deleteById(@PathVariable UUID id) {
        service.deleteById(id);
    }
}
