package org.example.ticket.infrastructure.api

import org.example.ticket.domain.model.Ticket

class TicketApiClientResolver (
    private val ticketApiClients: List<TicketApiClient>
){
    fun resolve(barcode: String): TicketApiClient {
        val ticketType = Ticket.ticketTypeCheck(barcode)
        return ticketApiClients.first { it.type() == ticketType }
    }
}