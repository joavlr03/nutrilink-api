package br.com.joavlr03.nutrilink_api.service;

import br.com.joavlr03.nutrilink_api.model.Doadora;
import br.com.joavlr03.nutrilink_api.model.ProfissionalSaude;
import br.com.joavlr03.nutrilink_api.model.Triagem;
import br.com.joavlr03.nutrilink_api.model.enums.StatusCadastro;
import br.com.joavlr03.nutrilink_api.model.enums.StatusTriagem;
import br.com.joavlr03.nutrilink_api.model.enums.TipoProfissional;
import br.com.joavlr03.nutrilink_api.repository.DoadoraRepository;
import br.com.joavlr03.nutrilink_api.repository.ProfissionalSaudeRepository;
import br.com.joavlr03.nutrilink_api.repository.TriagemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TriagemService {

    private final TriagemRepository repository;
    private final DoadoraRepository doadoraRepository;
    private final ProfissionalSaudeRepository profissionalRepository;
    private final DoadoraService doadoraService;

    public TriagemService(
            TriagemRepository repository,
            DoadoraRepository doadoraRepository,
            ProfissionalSaudeRepository profissionalRepository,
            DoadoraService doadoraService) {
        this.repository = repository;
        this.doadoraRepository = doadoraRepository;
        this.profissionalRepository = profissionalRepository;
        this.doadoraService = doadoraService;
    }

    public Triagem create(Triagem triagem, UUID doadoraId, UUID profissionalId) {

        // 1. Resolve doadora
        Doadora doadora = doadoraRepository.findById(doadoraId)
                .orElseThrow(() -> new EntityNotFoundException("Doadora não encontrada: " + doadoraId));
        triagem.setDoadora(doadora);

        // 2. Resolve profissional (opcional)
        if (profissionalId != null) {
            ProfissionalSaude profissional = profissionalRepository.findById(profissionalId)
                    .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado: " + profissionalId));

            if (profissional.getTipoProfissional() != TipoProfissional.ANALISTA_NIVEL_1) {
                throw new IllegalArgumentException("Apenas analistas de nível 1 podem revisar triagens.");
            }

            if (!profissional.getCredencialAtiva()) {
                throw new IllegalArgumentException("Profissional com credencial inativa.");
            }

            triagem.setProfissional(profissional);
        }

        // 3. Calcula scoreRisco com base nas respostas
        int score = calcularScoreRisco(triagem.getRespostasQuestionario());
        triagem.setScoreRisco(score);

        // 4. Define status e validação humana com base no score
        if (score >= 70) {
            triagem.setRequerValidacaoHumana(false);
            triagem.setStatusTriagem(StatusTriagem.APROVADA);
        } else if (score >= 40) {
            triagem.setRequerValidacaoHumana(true);
            triagem.setStatusTriagem(StatusTriagem.PENDENTE_REVISAO);
        } else {
            triagem.setRequerValidacaoHumana(false);
            triagem.setStatusTriagem(StatusTriagem.REPROVADA);
        }

        // 5. Atualiza statusCadastro da Doadora — único ponto do sistema que faz isso
        atualizarStatusDoadora(doadora, triagem.getStatusTriagem());

        // 6. Seta timestamp
        triagem.setDataRealizacao(LocalDateTime.now());

        return repository.save(triagem);
    }

    public Optional<Triagem> findById(UUID id) {
        return repository.findById(id);
    }

    public List<Triagem> findAll() {
        return repository.findAll();
    }

    public List<Triagem> findByDoadoraId(UUID doadoraId) {
        return repository.findByDoadoraId(doadoraId);
    }

    public List<Triagem> findPendentesRevisao() {
        return repository.findByRequerValidacaoHumanaTrue();
    }

    public void deleteById(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Triagem não encontrada: " + id);
        }
        repository.deleteById(id);
    }

    // --- Métodos internos ---

    private int calcularScoreRisco(String respostasJson) {
        if (respostasJson == null || respostasJson.isBlank()) return 0;

        int score = 100;

        if (respostasJson.contains("\"medicamento\":true")) score -= 40;
        if (respostasJson.contains("\"doencaCronica\":true")) score -= 30;
        if (respostasJson.contains("\"alcool\":true")) score -= 20;
        if (respostasJson.contains("\"fumo\":true")) score -= 10;

        return Math.max(score, 0);
    }

    private void atualizarStatusDoadora(Doadora doadora, StatusTriagem statusTriagem) {
        switch (statusTriagem) {
            case APROVADA -> doadora.setStatusCadastro(StatusCadastro.APROVADA);
            case REPROVADA -> doadora.setStatusCadastro(StatusCadastro.REPROVADA);
            case PENDENTE_REVISAO -> doadora.setStatusCadastro(StatusCadastro.PENDENTE);
        }
        doadoraService.save(doadora);
    }
}