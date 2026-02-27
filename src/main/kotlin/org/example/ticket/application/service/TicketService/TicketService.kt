package org.example.ticket.application.service.TicketService

import org.example.ticket.application.dto.TicketCreationDto
import org.example.ticket.application.dto.TicketSellingPriceOfferDto
import org.example.ticket.domain.enum.TicketStatus
import org.example.ticket.domain.model.Ticket
import org.example.ticket.infra.repository.TicketJpaRepository
import org.example.ticket.infrastructure.api.TicketApiClient
import org.example.ticket.infrastructure.api.TicketApiClientResolver
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class TicketService(
    private val ticketApiClientResolver: TicketApiClientResolver,
    private val ticketRepository: TicketJpaRepository
) {
    fun createTicket(ticketCreationDto: TicketCreationDto): Ticket {
        val apiClient = ticketApiClientResolver.resolve(ticketCreationDto.barcode)
        val ticketResponseDto = apiClient.getTicket(ticketCreationDto.barcode)
        val ticket = ticketResponseDto.toTicket(
            ticketCreationDto.barcode,
            ticketCreationDto.sellerName,
            apiClient.type(),
        )
        return ticketRepository.save(ticket)
    };

    fun applySellerOfferPrice(ticketSellingPriceOfferDto: TicketSellingPriceOfferDto): Ticket {
        val ticket = requireNotNull(ticketRepository.findByBarcode(ticketSellingPriceOfferDto.barcode)){
            "해당 티켓 정보가 존재하지 않습니다."
        }
        ticket.applySellerOfferPrice(
            ticketSellingPriceOfferDto.sellerName,
            ticketSellingPriceOfferDto.sellingPrice
        )
        return ticketRepository.save(ticket)
    }

    fun reSaleTicket(ticketSellingPriceOfferDto: TicketSellingPriceOfferDto): Ticket {
        val ticket = requireNotNull(ticketRepository.findByBarcode(ticketSellingPriceOfferDto.barcode)){
            "해당 티켓 정보가 존재하지 않습니다."
        }
        ticket.reSale(
            ticketSellingPriceOfferDto.sellerName,
            ticketSellingPriceOfferDto.sellingPrice
        )
        return ticketRepository.save(ticket)
    }
}