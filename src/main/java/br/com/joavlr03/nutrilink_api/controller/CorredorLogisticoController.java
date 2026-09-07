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

import br.com.joavlr03.nutrilink_api.dto.corredorlogistico.CorredorLogisticoCreateRequest;
import br.com.joavlr03.nutrilink_api.dto.corredorlogistico.CorredorLogisticoMapper;
import br.com.joavlr03.nutrilink_api.dto.corredorlogistico.CorredorLogisticoResponse;
import br.com.joavlr03.nutrilink_api.model.CorredorLogistico;
import br.com.joavlr03.nutrilink_api.service.CorredorLogisticoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v2/logistica")
public class CorredorLogisticoController {
    private final CorredorLogisticoService service;
    private final CorredorLogisticoMapper mapper;

    public CorredorLogisticoController(CorredorLogisticoService service, CorredorLogisticoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar corredor logístico")
    public CorredorLogisticoResponse create(@RequestBody @Valid CorredorLogisticoCreateRequest request) {
        CorredorLogistico corredor = mapper.toModel(request);
        corredor = service.create(corredor);
        return mapper.toDto(corredor);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar corredor por ID")
    public CorredorLogisticoResponse findById(@PathVariable UUID id) {
        return mapper.toDto(
            service.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Corredor não encontrado: " + id))
        );
    }

    @GetMapping
    @Operation(summary = "Listar todos os corredores")
    public List<CorredorLogisticoResponse> findAll() {
        return service.findAll().stream()
                .map(c -> mapper.toDto(c))
                .toList();
    }

    @GetMapping("/ativos")
    @Operation(summary = "Listar corredores homologados ativos")
    public List<CorredorLogisticoResponse> findAtivos() {
        return service.findAtivos().stream()
                .map(c -> mapper.toDto(c))
                .toList();
    }

    @PatchMapping("/{id}/desabilitar")
    @Operation(summary = "Desabilitar corredor logístico")
    public CorredorLogisticoResponse desabilitar(@PathVariable UUID id) {
        return mapper.toDto(service.desabilitar(id));
    }

    @PatchMapping("/{id}/habilitar")
    @Operation(summary = "Habilitar corredor logístico")
    public CorredorLogisticoResponse habilitar(@PathVariable UUID id) {
        return mapper.toDto(service.habilitar(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir corredor logístico")
    public void deleteById(@PathVariable UUID id) {
        service.deleteById(id);
    }
}
