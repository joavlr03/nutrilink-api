package br.com.joavlr03.nutrilink_api.dto.coleta;

import org.springframework.stereotype.Component;

import br.com.joavlr03.nutrilink_api.model.Coleta;

@Component 
public class ColetaMapper {
        public Coleta toModel(ColetaCreateRequest request) {
        Coleta coleta = new Coleta();
        coleta.setDataAgendada(request.getDataAgendada());
        coleta.setVolumeEstimadoMl(request.getVolumeEstimadoMl());
        return coleta;
    }

    public ColetaResponse toDto(Coleta coleta) {
        ColetaResponse response = new ColetaResponse();
        response.setId(coleta.getId());
        response.setDataAgendada(coleta.getDataAgendada());
        response.setVolumeEstimadoMl(coleta.getVolumeEstimadoMl());
        response.setStatusColeta(coleta.getStatusColeta());

        if (coleta.getDoadora() != null) {
            response.setDoadoraId(coleta.getDoadora().getId());
        }

        if (coleta.getCorredor() != null) {
            response.setCorredorId(coleta.getCorredor().getId());
        }

        return response;
    }
    
}
