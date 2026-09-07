package br.com.joavlr03.nutrilink_api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.joavlr03.nutrilink_api.dto.coleta.ColetaCreateRequest;
import br.com.joavlr03.nutrilink_api.dto.coleta.ColetaMapper;
import br.com.joavlr03.nutrilink_api.dto.coleta.ColetaResponse;
import br.com.joavlr03.nutrilink_api.model.Coleta;
import br.com.joavlr03.nutrilink_api.model.enums.StatusColeta;
import br.com.joavlr03.nutrilink_api.service.ColetaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v2/coleta")
public class ColetaController {
    
    private final ColetaService service;
    private final ColetaMapper mapper;

    public ColetaController(ColetaService service, ColetaMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Agendar coleta")
    public ColetaResponse create(@RequestBody @Valid ColetaCreateRequest request) {
        Coleta coleta = mapper.toModel(request);
        coleta = service.create(coleta, request.getDoadoraId(), request.getCorredorId());
        return mapper.toDto(coleta);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar coleta por ID")
    public ColetaResponse findById(@PathVariable UUID id) {
        return mapper.toDto(
            service.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Coleta não encontrada: " + id))
        );
    }

    @GetMapping
    @Operation(summary = "Listar todas as coletas")
    public List<ColetaResponse> findAll() {
        return service.findAll().stream()
                .map(c -> mapper.toDto(c))
                .toList();
    }

    @GetMapping("/doadora/{doadoraId}")
    @Operation(summary = "Listar coletas de uma doadora")
    public List<ColetaResponse> findByDoadora(@PathVariable UUID doadoraId) {
        return service.findByDoadoraId(doadoraId).stream()
                .map(c -> mapper.toDto(c))
                .toList();
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Listar coletas por status")
    public List<ColetaResponse> findByStatus(@PathVariable StatusColeta status) {
        return service.findByStatus(status).stream()
                .map(c -> mapper.toDto(c))
                .toList();
    }

    @PatchMapping("/{id}/status/{novoStatus}")
    @Operation(summary = "Atualizar status da coleta")
    public ColetaResponse atualizarStatus(@PathVariable UUID id, @PathVariable StatusColeta novoStatus) {
        return mapper.toDto(service.atualizarStatus(id, novoStatus));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir coleta")
    public void deleteById(@PathVariable UUID id) {
        service.deleteById(id);
    }
}
