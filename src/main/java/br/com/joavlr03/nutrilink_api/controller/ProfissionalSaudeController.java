package br.com.joavlr03.nutrilink_api.controller;

import br.com.joavlr03.nutrilink_api.dto.profissionalsaude.ProfissionalSaudeCreateRequest;
import br.com.joavlr03.nutrilink_api.dto.profissionalsaude.ProfissionalSaudeMapper;
import br.com.joavlr03.nutrilink_api.dto.profissionalsaude.ProfissionalSaudeResponse;
import br.com.joavlr03.nutrilink_api.model.ProfissionalSaude;
import br.com.joavlr03.nutrilink_api.model.enums.TipoProfissional;
import br.com.joavlr03.nutrilink_api.service.ProfissionalSaudeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v2/profissionais-saude")
@Tag(name = "Profissionais de Saúde", description = "Gerenciamento de analistas e especialistas")
public class ProfissionalSaudeController {

    private final ProfissionalSaudeService service;
    private final ProfissionalSaudeMapper mapper;

    public ProfissionalSaudeController(ProfissionalSaudeService service, ProfissionalSaudeMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar profissional de saúde")
    public ProfissionalSaudeResponse create(@RequestBody @Valid ProfissionalSaudeCreateRequest request) {
        ProfissionalSaude profissional = mapper.toModel(request);
        profissional = service.create(profissional);
        return mapper.toDto(profissional);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar profissional por ID")
    public ProfissionalSaudeResponse findById(@PathVariable UUID id) {
        return mapper.toDto(
            service.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado: " + id))
        );
    }

    @GetMapping
    @Operation(summary = "Listar todos os profissionais")
    public List<ProfissionalSaudeResponse> findAll() {
        return service.findAll().stream()
                .map(p -> mapper.toDto(p))
                .toList();
    }

    @GetMapping("/ativos")
    @Operation(summary = "Listar profissionais com credencial ativa")
    public List<ProfissionalSaudeResponse> findAtivos() {
        return service.findAtivos().stream()
                .map(p -> mapper.toDto(p))
                .toList();
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Listar profissionais por tipo")
    public List<ProfissionalSaudeResponse> findByTipo(@PathVariable TipoProfissional tipo) {
        return service.findByTipo(tipo).stream()
                .map(p -> mapper.toDto(p))
                .toList();
    }

    @PatchMapping("/{id}/inativar")
    @Operation(summary = "Inativar credencial do profissional")
    public ProfissionalSaudeResponse inativar(@PathVariable UUID id) {
        return mapper.toDto(service.inativar(id));
    }

    @PatchMapping("/{id}/reativar")
    @Operation(summary = "Reativar credencial do profissional")
    public ProfissionalSaudeResponse reativar(@PathVariable UUID id) {
        return mapper.toDto(service.reativar(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir profissional")
    public void deleteById(@PathVariable UUID id) {
        service.deleteById(id);
    }
}