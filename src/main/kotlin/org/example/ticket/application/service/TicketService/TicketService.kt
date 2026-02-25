package org.example.ticket.application.service.TicketService

import org.example.ticket.application.dto.TicketCreationDto
import org.example.ticket.domain.model.Ticket
import org.example.ticket.infra.repository.TicketJpaRepository
import org.example.ticket.infrastructure.api.TicketApiClient
import org.springframework.stereotype.Service

@Service
class TicketService (
    private val ticketApiClient: List<TicketApiClient>,
    private val ticketRepository: TicketJpaRepository
) {
    fun createTicket(ticketCreationDto: TicketCreationDto){
        val ticketType = Ticket.ticketTypeCheck(ticketCreationDto.barcode);
        val apiClient = ticketApiClient.first { it.type(ticketCreationDto.barcode) == ticketType };

        val ticketResponseDto = apiClient.getTicket(ticketCreationDto.barcode);
        val ticket = ticketResponseDto.toTicket(ticketCreationDto.barcode, ticketType);
        ticketRepository.save(ticket)

    };
    fun applySellerOfferPrice(barcode: String) {
        // TODO : 구현


    };
}