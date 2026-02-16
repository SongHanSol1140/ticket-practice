package org.example.ticket.infra.repository

import org.example.ticket.domain.model.Ticket
import org.springframework.data.jpa.repository.JpaRepository

interface TicketJpaRepository: JpaRepository<Ticket, Long> {
}
