package org.example.ticket.infra.api

import org.example.ticket.domain.enumeration.TicketType
import org.example.ticket.infra.dto.TicketApiResponseDto
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.LocalDateTime

@Component
class MelonTicketApiClient : TicketApiClient {
    override fun getTicket(varCode: String): TicketApiResponseDto {
        return TicketApiResponseDto(
            LocalDateTime.now().plusDays(20), BigDecimal.valueOf(10000)
        )
    }

    override fun type(varCode: String): TicketType {
        return TicketType.MELON
    }
}
