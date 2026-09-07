package br.com.joavlr03.nutrilink_api.dto.profissionalsaude;

import br.com.joavlr03.nutrilink_api.model.ProfissionalSaude;
import org.springframework.stereotype.Component;

@Component
public class ProfissionalSaudeMapper {

    public ProfissionalSaude toModel(ProfissionalSaudeCreateRequest request) {
        ProfissionalSaude profissional = new ProfissionalSaude();
        profissional.setNomeCompleto(request.getNomeCompleto());
        profissional.setRegistroConselho(request.getRegistroConselho());
        profissional.setTipoProfissional(request.getTipoProfissional());
        return profissional;
    }

    public ProfissionalSaudeResponse toDto(ProfissionalSaude profissional) {
        ProfissionalSaudeResponse response = new ProfissionalSaudeResponse();
        response.setId(profissional.getId());
        response.setNomeCompleto(profissional.getNomeCompleto());
        response.setRegistroConselho(profissional.getRegistroConselho());
        response.setTipoProfissional(profissional.getTipoProfissional());
        response.setCredencialAtiva(profissional.getCredencialAtiva());
        return response;
    }
}