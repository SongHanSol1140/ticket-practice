package org.example.ticket.infrastructure.api

import org.example.ticket.domain.enum.TicketType
import org.springframework.stereotype.Component

@Component
class TicketApiClientResolver(
    private val ticketApiClients: List<TicketApiClient>
) {
    fun resolve(barcode: String): TicketApiClient {
        val ticketType = resolveTicketType(barcode)
        return ticketApiClients.first { it.type() == ticketType }
    }

    private fun resolveTicketType(barcode: String): TicketType {
        return when {
            barcode.all { it.isLetter() } -> TicketType.MELON
            barcode.all { it.isDigit() } -> TicketType.NOL
            else -> TicketType.MOL
        }
    }
}
