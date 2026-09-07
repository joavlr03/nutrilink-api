package br.com.joavlr03.nutrilink_api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.joavlr03.nutrilink_api.model.MensagemSuporte;
import br.com.joavlr03.nutrilink_api.model.TicketSuporte;
import br.com.joavlr03.nutrilink_api.model.enums.RemetenteTipo;
import br.com.joavlr03.nutrilink_api.model.enums.StatusTicket;
import br.com.joavlr03.nutrilink_api.repository.DoadoraRepository;
import br.com.joavlr03.nutrilink_api.repository.MensagemSuporteRepository;
import br.com.joavlr03.nutrilink_api.repository.ProfissionalSaudeRepository;
import br.com.joavlr03.nutrilink_api.repository.TicketSuporteRepository;
import jakarta.persistence.EntityNotFoundException;


@Service 
public class MensagemSuporteService {
    private final MensagemSuporteRepository repository;
    private final TicketSuporteRepository ticketRepository;
    private final DoadoraRepository doadoraRepository;
    private final ProfissionalSaudeRepository profissionalRepository;

    public MensagemSuporteService(
            MensagemSuporteRepository repository,
            TicketSuporteRepository ticketRepository,
            DoadoraRepository doadoraRepository,
            ProfissionalSaudeRepository profissionalRepository) {
        this.repository = repository;
        this.ticketRepository = ticketRepository;
        this.doadoraRepository = doadoraRepository;
        this.profissionalRepository = profissionalRepository;
    }

    public MensagemSuporte create(MensagemSuporte mensagem, UUID ticketId) {

        // 1. Resolve ticket
        TicketSuporte ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket não encontrado: " + ticketId));

        // 2. Valida se ticket está aberto ou em andamento
        if (ticket.getStatusTicket() == StatusTicket.FECHADO) {
            throw new IllegalStateException("Não é possível enviar mensagens em um ticket fechado.");
        }

        // 3. Valida se remetenteId existe de acordo com o tipo
        if (mensagem.getRemetenteTipo() == RemetenteTipo.DOADORA) {
            doadoraRepository.findById(mensagem.getRemetenteId())
                    .orElseThrow(() -> new EntityNotFoundException(
                        "Doadora não encontrada: " + mensagem.getRemetenteId()));
        } else {
            profissionalRepository.findById(mensagem.getRemetenteId())
                    .orElseThrow(() -> new EntityNotFoundException(
                        "Profissional não encontrado: " + mensagem.getRemetenteId()));
        }

        mensagem.setTicket(ticket);
        mensagem.setDataEnvio(LocalDateTime.now());

        return repository.save(mensagem);
    }

    public Optional<MensagemSuporte> findById(UUID id) {
        return repository.findById(id);
    }

    public List<MensagemSuporte> findAll() {
        return repository.findAll();
    }

    public List<MensagemSuporte> findByTicketId(UUID ticketId) {
        return repository.findByTicketIdOrderByDataEnvioAsc(ticketId);
    }

    public void deleteById(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Mensagem não encontrada: " + id);
        }
        repository.deleteById(id);
    }
}
