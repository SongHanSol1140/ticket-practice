package org.example.ticket.infra.dto

import org.example.ticket.domain.model.Ticket
import java.math.BigDecimal
import java.time.LocalDateTime

data class TicketApiResponseDto(
    val performanceDate: LocalDateTime,
    val price: BigDecimal
) {
    fun toTicket(): Ticket {
        return Ticket(
            expirationAt = performanceDate,
            originalPrice = price,
        )
    }
}
