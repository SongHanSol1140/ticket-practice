package org.example.ticket.infrastructure.dto

import org.example.ticket.domain.model.Ticket
import java.math.BigDecimal
import java.time.LocalDateTime

data class TicketApiResponseDto(
    val performanceDate: LocalDateTime,
    val price: BigDecimal
) {
    fun toTicket(barcode: String): Ticket {
        return Ticket(
            barcode = barcode,
            eventDateTime = performanceDate,
            originalPrice = price
        )
    }
}

