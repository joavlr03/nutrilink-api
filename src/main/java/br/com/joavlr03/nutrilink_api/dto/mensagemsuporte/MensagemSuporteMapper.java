package br.com.joavlr03.nutrilink_api.dto.mensagemsuporte;

import org.springframework.stereotype.Component;

import br.com.joavlr03.nutrilink_api.model.MensagemSuporte;

@Component 
public class MensagemSuporteMapper {
    public MensagemSuporte toModel(MensagemSuporteCreateRequest request) {
        MensagemSuporte mensagem = new MensagemSuporte();
        mensagem.setRemetenteTipo(request.getRemetenteTipo());
        mensagem.setRemetenteId(request.getRemetenteId());
        mensagem.setConteudoMensagem(request.getConteudoMensagem());
        return mensagem;
    }

    public MensagemSuporteResponse toDto(MensagemSuporte mensagem) {
        MensagemSuporteResponse response = new MensagemSuporteResponse();
        response.setId(mensagem.getId());
        response.setRemetenteTipo(mensagem.getRemetenteTipo());
        response.setRemetenteId(mensagem.getRemetenteId());
        response.setConteudoMensagem(mensagem.getConteudoMensagem());
        response.setDataEnvio(mensagem.getDataEnvio());

        if (mensagem.getTicket() != null) {
            response.setTicketId(mensagem.getTicket().getId());
        }

        return response;
    }
}
