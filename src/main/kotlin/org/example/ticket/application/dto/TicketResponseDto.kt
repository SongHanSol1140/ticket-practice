package org.example.ticket.application.dto

import org.example.ticket.domain.enum.TicketStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class TicketResponseDto(
    val barcode: String,
    val seller: String,
    val originalPrice: BigDecimal,
    val sellingPrice: BigDecimal?,
    val expirationDate: LocalDateTime,
    val ticketStatus: TicketStatus
)
