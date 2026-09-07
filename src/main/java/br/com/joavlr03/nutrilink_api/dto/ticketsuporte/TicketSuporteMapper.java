package br.com.joavlr03.nutrilink_api.dto.ticketsuporte;

import org.springframework.stereotype.Component;

import br.com.joavlr03.nutrilink_api.model.TicketSuporte;

@Component 
public class TicketSuporteMapper {
       public TicketSuporte toModel(TicketSuporteCreateRequest request) {
        TicketSuporte ticket = new TicketSuporte();
        ticket.setAssunto(request.getAssunto());
        return ticket;
    }

    public TicketSuporteResponse toDto(TicketSuporte ticket) {
        TicketSuporteResponse response = new TicketSuporteResponse();
        response.setId(ticket.getId());
        response.setAssunto(ticket.getAssunto());
        response.setStatusTicket(ticket.getStatusTicket());
        response.setDataAbertura(ticket.getDataAbertura());

        if (ticket.getDoadora() != null) {
            response.setDoadoraId(ticket.getDoadora().getId());
        }

        if (ticket.getProfissional() != null) {
            response.setProfissionalId(ticket.getProfissional().getId());
        }

        return response;
    }
}
