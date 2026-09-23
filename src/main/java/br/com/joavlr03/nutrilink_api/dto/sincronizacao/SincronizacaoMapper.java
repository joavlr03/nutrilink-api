package br.com.joavlr03.nutrilink_api.dto.sincronizacao;

import org.springframework.stereotype.Component;

import br.com.joavlr03.nutrilink_api.model.Sincronizacao;

@Component
public class SincronizacaoMapper {

    public SincronizacaoResponse toDto(Sincronizacao sincronizacao) {
        SincronizacaoResponse response = new SincronizacaoResponse();
        response.setId(sincronizacao.getId());
        response.setPayloadEnviado(sincronizacao.getPayloadEnviado());
        response.setProtocoloGerado(sincronizacao.getProtocoloGerado());
        response.setStatusSincronizacao(sincronizacao.getStatusSincronizacao());
        response.setDataSincronizacao(sincronizacao.getDataSincronizacao());

        if (sincronizacao.getColeta() != null) {
            response.setColetaId(sincronizacao.getColeta().getId());
        }

        return response;
    }
}
