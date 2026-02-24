package org.example.ticket.infrastructure.api

import org.example.ticket.infrastructure.dto.TicketApiResponseDto
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.LocalDateTime

@Component
class MelonTicketApiClient : TicketApiClient{
    override fun getTicket(barcode: String): TicketApiResponseDto {
        return TicketApiResponseDto(

            performanceDate = LocalDateTime.now().plusDays(3),
            price = BigDecimal.valueOf(10000)
        )
    }
}