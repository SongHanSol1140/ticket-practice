package org.example.ticket.infrastructure.api

import org.example.ticket.domain.enum.TicketType
import org.example.ticket.infrastructure.dto.TicketApiResponseDto

interface TicketApiClient {
    fun getTicket(barcode: String): TicketApiResponseDto
    fun type() : TicketType

}