package org.example.ticket.infrastructure.dto

import org.example.ticket.domain.enum.TicketType
import org.example.ticket.domain.model.Ticket
import java.math.BigDecimal
import java.time.LocalDateTime

data class TicketApiResponseDto(
    val performanceDateTime: LocalDateTime,
    val price: BigDecimal
) {
    fun toTicket(barcode: String, sellerId: String, ticketType: TicketType,): Ticket {
        return Ticket(
            barcode = barcode,
            ticketType = ticketType,
            sellerId = sellerId,
            expirationDateTime = performanceDateTime,
            originalPrice = price
        )
    }
}

