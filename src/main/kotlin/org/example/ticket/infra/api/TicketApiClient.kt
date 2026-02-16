package org.example.ticket.infra.api

import org.example.ticket.domain.enumeration.TicketType
import org.example.ticket.infra.dto.TicketApiResponseDto

interface TicketApiClient {
    fun getTicket(varCode: String) : TicketApiResponseDto
    fun type(varCode: String) : TicketType
}
