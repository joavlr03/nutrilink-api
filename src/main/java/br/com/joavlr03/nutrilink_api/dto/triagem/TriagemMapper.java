package br.com.joavlr03.nutrilink_api.dto.triagem;

import org.springframework.stereotype.Component;

import br.com.joavlr03.nutrilink_api.model.Triagem;

@Component 
public class TriagemMapper {
        public Triagem toModel(TriagemCreateRequest request) {
        Triagem triagem = new Triagem();
        triagem.setRespostasQuestionario(request.getRespostasQuestionario());
        triagem.setParecerProfissional(request.getParecerProfissional());
        // doadora e profissional são resolvidos no Service pelo ID
        return triagem;
    }

    public TriagemResponse toDto(Triagem triagem) {
        TriagemResponse response = new TriagemResponse();
        response.setId(triagem.getId());
        response.setRespostasQuestionario(triagem.getRespostasQuestionario());
        response.setScoreRisco(triagem.getScoreRisco());
        response.setRequerValidacaoHumana(triagem.getRequerValidacaoHumana());
        response.setStatusTriagem(triagem.getStatusTriagem());
        response.setParecerProfissional(triagem.getParecerProfissional());
        response.setDataRealizacao(triagem.getDataRealizacao());

        if (triagem.getDoadora() != null) {
            response.setDoadoraId(triagem.getDoadora().getId());
        }

        if (triagem.getProfissional() != null) {
            response.setProfissionalId(triagem.getProfissional().getId());
        }

        return response;
    }
}
