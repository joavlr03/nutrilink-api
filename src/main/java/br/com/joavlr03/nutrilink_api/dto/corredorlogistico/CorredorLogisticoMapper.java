package br.com.joavlr03.nutrilink_api.dto.corredorlogistico;

import org.springframework.stereotype.Component;

import br.com.joavlr03.nutrilink_api.model.CorredorLogistico;

@Component 
public class CorredorLogisticoMapper {
     public CorredorLogistico toModel(CorredorLogisticoCreateRequest request) {
        CorredorLogistico corredor = new CorredorLogistico();
        corredor.setNomeCorredor(request.getNomeCorredor());
        corredor.setCepsAtendidos(request.getCepsAtendidos());
        return corredor;
    }

    public CorredorLogisticoResponse toDto(CorredorLogistico corredor) {
        CorredorLogisticoResponse response = new CorredorLogisticoResponse();
        response.setId(corredor.getId());
        response.setNomeCorredor(corredor.getNomeCorredor());
        response.setCepsAtendidos(corredor.getCepsAtendidos());
        response.setStatusHomologacao(corredor.getStatusHomologacao());
        return response;
    }
}
