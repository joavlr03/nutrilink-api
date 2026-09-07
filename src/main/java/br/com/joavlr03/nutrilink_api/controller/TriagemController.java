package br.com.joavlr03.nutrilink_api.controller;

import br.com.joavlr03.nutrilink_api.dto.triagem.TriagemCreateRequest;
import br.com.joavlr03.nutrilink_api.dto.triagem.TriagemMapper;
import br.com.joavlr03.nutrilink_api.dto.triagem.TriagemResponse;
import br.com.joavlr03.nutrilink_api.model.Triagem;
import br.com.joavlr03.nutrilink_api.service.TriagemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v2/triagens")
@Tag(name = "Triagens", description = "Gerenciamento de triagens de elegibilidade")
public class TriagemController {

    private final TriagemService service;
    private final TriagemMapper mapper;

    public TriagemController(TriagemService service, TriagemMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Realizar triagem de doadora")
    public TriagemResponse create(@RequestBody @Valid TriagemCreateRequest request) {
        Triagem triagem = mapper.toModel(request);
        triagem = service.create(triagem, request.getDoadoraId(), request.getProfissionalId());
        return mapper.toDto(triagem);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar triagem por ID")
    public TriagemResponse findById(@PathVariable UUID id) {
        return mapper.toDto(
            service.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Triagem não encontrada: " + id))
        );
    }

    @GetMapping
    @Operation(summary = "Listar todas as triagens")
    public List<TriagemResponse> findAll() {
        return service.findAll().stream()
                .map(t -> mapper.toDto(t))
                .toList();
    }

    @GetMapping("/doadora/{doadoraId}")
    @Operation(summary = "Listar triagens de uma doadora")
    public List<TriagemResponse> findByDoadora(@PathVariable UUID doadoraId) {
        return service.findByDoadoraId(doadoraId).stream()
                .map(t -> mapper.toDto(t))
                .toList();
    }

    @GetMapping("/pendentes-revisao")
    @Operation(summary = "Listar triagens pendentes de revisão humana")
    public List<TriagemResponse> findPendentesRevisao() {
        return service.findPendentesRevisao().stream()
                .map(t -> mapper.toDto(t))
                .toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir triagem")
    public void deleteById(@PathVariable UUID id) {
        service.deleteById(id);
    }
}