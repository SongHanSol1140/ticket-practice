package org.example.ticket.presentation

import org.example.ticket.application.dto.TicketCreationDto
import org.example.ticket.application.service.TicketService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TicketController(
    private val ticketService: TicketService
) {
    @PostMapping("/tickets")
    fun registerTicket(
        @RequestBody ticketCreationDto: TicketCreationDto
    ) {
        ticketService.createTicket(ticketCreationDto)
    }
}
