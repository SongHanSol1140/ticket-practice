package org.example.ticket.application.service

import jakarta.transaction.Transactional
import org.example.ticket.application.dto.TicketCreationDto
import org.example.ticket.application.dto.TicketResponseDto
import org.example.ticket.application.dto.TicketSellingPriceOfferDto
import org.example.ticket.infrastructure.repository.TicketJpaRepository
import org.example.ticket.infrastructure.api.TicketApiClientResolver
import org.springframework.stereotype.Service

@Service
class TicketService(
    private val ticketApiClientResolver: TicketApiClientResolver,
    private val ticketRepository: TicketJpaRepository
) {
    @Transactional
    fun createTicket(ticketCreationDto: TicketCreationDto): TicketResponseDto {
        val apiClient = ticketApiClientResolver.resolve(ticketCreationDto.barcode)
        val ticketResponseDto = apiClient.getTicket(ticketCreationDto.barcode)
        val ticket = ticketRepository.save(
            ticketResponseDto.toTicket(
                ticketCreationDto.barcode,
                ticketCreationDto.sellerName,
                apiClient.type(),
            )
        )
        return TicketResponseDto(
            barcode = ticket.barcode,
            seller = ticket.sellerName,
            originalPrice = ticket.originalPrice,
            sellingPrice = ticket.sellingPrice,
            expirationDate = ticket.expirationDateTime,
            ticketStatus = ticket.ticketStatus,
        )
    }
    @Transactional
    fun applySellerOfferPrice(ticketSellingPriceOfferDto: TicketSellingPriceOfferDto): TicketResponseDto {
        val ticket = requireNotNull(ticketRepository.findByBarcode(ticketSellingPriceOfferDto.barcode)){
            "해당 티켓 정보가 존재하지 않습니다."
        }
        ticket.applySellerOfferPrice(
            ticketSellingPriceOfferDto.sellerName,
            ticketSellingPriceOfferDto.sellingPrice
        )
        ticketRepository.save(ticket)
        return TicketResponseDto(
            barcode = ticket.barcode,
            seller = ticket.sellerName,
            originalPrice = ticket.originalPrice,
            sellingPrice = ticket.sellingPrice,
            expirationDate = ticket.expirationDateTime,
            ticketStatus = ticket.ticketStatus,
        )
    }
    @Transactional
    fun reSaleTicket(ticketSellingPriceOfferDto: TicketSellingPriceOfferDto): TicketResponseDto {
        val ticket = requireNotNull(ticketRepository.findByBarcode(ticketSellingPriceOfferDto.barcode)){
            "해당 티켓 정보가 존재하지 않습니다."
        }
        ticket.reSale(
            ticketSellingPriceOfferDto.sellerName,
            ticketSellingPriceOfferDto.sellingPrice
        )
        ticketRepository.save(ticket)
        return TicketResponseDto(
            barcode = ticket.barcode,
            seller = ticket.sellerName,
            originalPrice = ticket.originalPrice,
            sellingPrice = ticket.sellingPrice,
            expirationDate = ticket.expirationDateTime,
            ticketStatus = ticket.ticketStatus,
        )
    }
}