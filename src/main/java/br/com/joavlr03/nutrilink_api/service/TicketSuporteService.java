package br.com.joavlr03.nutrilink_api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.joavlr03.nutrilink_api.model.Doadora;
import br.com.joavlr03.nutrilink_api.model.ProfissionalSaude;
import br.com.joavlr03.nutrilink_api.model.TicketSuporte;
import br.com.joavlr03.nutrilink_api.model.enums.StatusTicket;
import br.com.joavlr03.nutrilink_api.model.enums.TipoProfissional;
import br.com.joavlr03.nutrilink_api.repository.DoadoraRepository;
import br.com.joavlr03.nutrilink_api.repository.ProfissionalSaudeRepository;
import br.com.joavlr03.nutrilink_api.repository.TicketSuporteRepository;
import jakarta.persistence.EntityNotFoundException;

@Service 
public class TicketSuporteService {
     private final TicketSuporteRepository repository;
    private final DoadoraRepository doadoraRepository;
    private final ProfissionalSaudeRepository profissionalRepository;

    public TicketSuporteService(
            TicketSuporteRepository repository,
            DoadoraRepository doadoraRepository,
            ProfissionalSaudeRepository profissionalRepository) {
        this.repository = repository;
        this.doadoraRepository = doadoraRepository;
        this.profissionalRepository = profissionalRepository;
    }

    public TicketSuporte create(TicketSuporte ticket, UUID doadoraId) {
        Doadora doadora = doadoraRepository.findById(doadoraId)
                .orElseThrow(() -> new EntityNotFoundException("Doadora não encontrada: " + doadoraId));

        ticket.setDoadora(doadora);
        ticket.setStatusTicket(StatusTicket.ABERTO);
        ticket.setDataAbertura(LocalDateTime.now());

        return repository.save(ticket);
    }

    public Optional<TicketSuporte> findById(UUID id) {
        return repository.findById(id);
    }

    public List<TicketSuporte> findAll() {
        return repository.findAll();
    }

    public List<TicketSuporte> findByDoadoraId(UUID doadoraId) {
        return repository.findByDoadoraId(doadoraId);
    }

    public List<TicketSuporte> findByStatus(StatusTicket status) {
        return repository.findByStatusTicket(status);
    }

    public TicketSuporte assumirTicket(UUID ticketId, UUID profissionalId) {
        TicketSuporte ticket = repository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket não encontrado: " + ticketId));

        ProfissionalSaude profissional = profissionalRepository.findById(profissionalId)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado: " + profissionalId));

        if (profissional.getTipoProfissional() != TipoProfissional.ESPECIALISTA_LACTACAO) {
            throw new IllegalArgumentException("Apenas especialistas em lactação podem assumir tickets.");
        }

        if (!profissional.getCredencialAtiva()) {
            throw new IllegalArgumentException("Profissional com credencial inativa.");
        }

        ticket.setProfissional(profissional);
        ticket.setStatusTicket(StatusTicket.EM_ANDAMENTO);

        return repository.save(ticket);
    }

    public TicketSuporte fecharTicket(UUID id) {
        TicketSuporte ticket = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket não encontrado: " + id));

        ticket.setStatusTicket(StatusTicket.FECHADO);
        return repository.save(ticket);
    }

    public void deleteById(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Ticket não encontrado: " + id);
        }
        repository.deleteById(id);
    }
}
