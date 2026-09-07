package br.com.joavlr03.nutrilink_api.dto.doadora;

import org.springframework.stereotype.Component;

import br.com.joavlr03.nutrilink_api.model.Doadora;

@Component 
public class DoadoraMapper {
    public Doadora toModel(DoadoraCreateRequest request) {
        Doadora doadora = new Doadora();
        doadora.setNomeCompleto(request.getNomeCompleto());
        doadora.setCpf(request.getCpf());
        doadora.setDataNascimento(request.getDataNascimento());
        doadora.setTelefone(request.getTelefone());
        doadora.setCep(request.getCep());
        doadora.setEnderecoCompleto(request.getEnderecoCompleto());
        return doadora;
    }

    public DoadoraResponse toDto(Doadora doadora) {
        DoadoraResponse response = new DoadoraResponse();
        response.setId(doadora.getId());
        response.setNomeCompleto(doadora.getNomeCompleto());
        response.setCpf(doadora.getCpf());
        response.setDataNascimento(doadora.getDataNascimento());
        response.setTelefone(doadora.getTelefone());
        response.setCep(doadora.getCep());
        response.setEnderecoCompleto(doadora.getEnderecoCompleto());
        response.setStatusCadastro(doadora.getStatusCadastro());
        return response;
    }
}
