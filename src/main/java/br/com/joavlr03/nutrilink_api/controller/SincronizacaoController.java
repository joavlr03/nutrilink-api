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

import br.com.joavlr03.nutrilink_api.dto.sincronizacao.SincronizacaoConfirmacaoRequest;
import br.com.joavlr03.nutrilink_api.dto.sincronizacao.SincronizacaoCreateRequest;
import br.com.joavlr03.nutrilink_api.dto.sincronizacao.SincronizacaoMapper;
import br.com.joavlr03.nutrilink_api.dto.sincronizacao.SincronizacaoResponse;
import br.com.joavlr03.nutrilink_api.model.enums.StatusSincronizacao;
import br.com.joavlr03.nutrilink_api.service.SincronizacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v2/sincronizacoes")
@Tag(name = "Sincronizações", description = "Envio de coletas para sistemas externos")
public class SincronizacaoController {

    private final SincronizacaoService service;
    private final SincronizacaoMapper mapper;

    public SincronizacaoController(SincronizacaoService service, SincronizacaoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar envio de uma coleta ao sistema externo (status PENDENTE)")
    public SincronizacaoResponse create(@RequestBody @Valid SincronizacaoCreateRequest request) {
        return mapper.toDto(service.create(request.getColetaId()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar sincronização por ID")
    public SincronizacaoResponse findById(@PathVariable UUID id) {
        return mapper.toDto(
            service.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sincronização não encontrada: " + id))
        );
    }

    @GetMapping
    @Operation(summary = "Listar todas as sincronizações")
    public List<SincronizacaoResponse> findAll() {
        return service.findAll().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/coleta/{coletaId}")
    @Operation(summary = "Buscar a sincronização de uma coleta")
    public SincronizacaoResponse findByColeta(@PathVariable UUID coletaId) {
        return mapper.toDto(service.findByColetaId(coletaId));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Listar sincronizações por status")
    public List<SincronizacaoResponse> findByStatus(@PathVariable StatusSincronizacao status) {
        return service.findByStatus(status).stream().map(mapper::toDto).toList();
    }

    @PatchMapping("/{id}/confirmar")
    @Operation(summary = "Confirmar sincronização com o protocolo devolvido pelo sistema externo")
    public SincronizacaoResponse confirmar(@PathVariable UUID id,
                                           @RequestBody @Valid SincronizacaoConfirmacaoRequest request) {
        return mapper.toDto(service.confirmar(id, request.getProtocoloGerado()));
    }

    @PatchMapping("/{id}/falha")
    @Operation(summary = "Registrar falha no envio")
    public SincronizacaoResponse registrarFalha(@PathVariable UUID id) {
        return mapper.toDto(service.registrarFalha(id));
    }

    @PatchMapping("/{id}/reprocessar")
    @Operation(summary = "Reprocessar uma sincronização que falhou (volta para PENDENTE)")
    public SincronizacaoResponse reprocessar(@PathVariable UUID id) {
        return mapper.toDto(service.reprocessar(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir sincronização")
    public void deleteById(@PathVariable UUID id) {
        service.deleteById(id);
    }
}
