package org.example.ticket.infra.api

import org.example.ticket.domain.enumeration.TicketType
import org.example.ticket.infra.dto.TicketApiResponseDto
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.LocalDateTime

@Component
class NolTicketClient : TicketApiClient {
    override fun getTicket(varCode: String): TicketApiResponseDto {
        return TicketApiResponseDto(LocalDateTime.now().plusDays(10), BigDecimal.valueOf(20000))
    }

    override fun type(varCode: String): TicketType {
        return TicketType.NOL
    }
}
