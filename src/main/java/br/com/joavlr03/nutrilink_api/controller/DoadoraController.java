package br.com.joavlr03.nutrilink_api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.joavlr03.nutrilink_api.dto.doadora.DoadoraCreateRequest;
import br.com.joavlr03.nutrilink_api.dto.doadora.DoadoraMapper;
import br.com.joavlr03.nutrilink_api.dto.doadora.DoadoraResponse;
import br.com.joavlr03.nutrilink_api.model.Doadora;
import br.com.joavlr03.nutrilink_api.service.DoadoraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v2/doadoras")
@Tag(name = "Doadoras", description = "Gerenciamento de doadoras")
public class DoadoraController {

    @Autowired
    private DoadoraService service;

    @Autowired
    private DoadoraMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar doadora")
    public DoadoraResponse create(@RequestBody @Valid DoadoraCreateRequest request) {
        Doadora doadora = mapper.toModel(request);
        doadora = service.create(doadora);
        return mapper.toDto(doadora);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar doadora por ID")
    public DoadoraResponse findById(@PathVariable UUID id) {
        return mapper.toDto(
                service.findById(id)
                        .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException(
                                "Doadora não encontrada: " + id)));
    }

    @GetMapping
    @Operation(summary = "Listar todas as doadoras")
    public List<DoadoraResponse> findAll() {
        return service.findAll().stream().map(mapper::toDto).toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir doadora")
    public void deleteById(@PathVariable UUID id) {
        service.deleteById(id);
    }
}
