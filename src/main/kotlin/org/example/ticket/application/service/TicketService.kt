package org.example.ticket.application.service

import org.example.ticket.application.dto.TicketCreationDto
import org.example.ticket.domain.model.Ticket
import org.example.ticket.infra.api.TicketApiClient
import org.example.ticket.infra.repository.TicketJpaRepository
import org.springframework.stereotype.Service

@Service
class TicketService(
    private val ticketApiClient: List<TicketApiClient>,
    private val ticketRepository: TicketJpaRepository
) {
    fun createTicket(ticketCreationDto: TicketCreationDto) {
        val ticketType = Ticket.ticketType(ticketCreationDto.varCode)
        val apiClient = ticketApiClient.first { it.type(ticketCreationDto.varCode) == ticketType }

        val ticketResponseDto = apiClient.getTicket(ticketCreationDto.varCode)
        val ticket = ticketResponseDto.toTicket()

        ticketRepository.save(ticket)
    }

    fun applySellerOfferPrice() {
        // TODO : 구현
    }
}
