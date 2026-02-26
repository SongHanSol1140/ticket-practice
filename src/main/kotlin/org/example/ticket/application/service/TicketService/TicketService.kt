package org.example.ticket.application.service.TicketService

import org.example.ticket.application.dto.TicketCreationDto
import org.example.ticket.domain.model.Ticket
import org.example.ticket.infra.repository.TicketJpaRepository
import org.example.ticket.infrastructure.api.TicketApiClient
import org.example.ticket.infrastructure.api.TicketApiClientResolver
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class TicketService (
    private val ticketApiClientResolver: TicketApiClientResolver,
    private val ticketRepository: TicketJpaRepository
) {
    fun createTicket(ticketCreationDto: TicketCreationDto){
        val apiClient = ticketApiClientResolver.resolve(ticketCreationDto.barcode)
        val ticketResponseDto = apiClient.getTicket(ticketCreationDto.barcode)
        val ticket = ticketResponseDto.toTicket(
            ticketCreationDto.barcode,
            ticketCreationDto.sellerName,
            apiClient.type(),
        )
        ticketRepository.save(ticket)

    };

    fun applySellerOfferPrice(offerSellerName: String, barcode: String, offerPrice: BigDecimal) {
        val ticket = ticketRepository.findByBarcode(barcode)
        requireNotNull(ticket){"해당 티켓 정보가 존재하지 않습니다."}

        ticket.applySellerOfferPrice(offerSellerName, offerPrice);
        ticketRepository.save(ticket)
    }
}