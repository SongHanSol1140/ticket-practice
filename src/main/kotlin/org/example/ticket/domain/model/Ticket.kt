package org.example.ticket.domain.model

import org.example.ticket.domain.enumeration.TicketStatus
import org.example.ticket.domain.enumeration.TicketType
import java.math.BigDecimal
import java.time.LocalDateTime

class Ticket(
    val id: Long? = null,
    val expirationAt: LocalDateTime,
    val originalPrice: BigDecimal,
    private var sellingPrice: BigDecimal? = null,
    val ticketStatus: TicketStatus = TicketStatus.ON_SALE,
) {
    companion object {
        val ACTIVATION_DEAD_LINE: LocalDateTime = LocalDateTime.now().plusHours(3L)
        private val HALF = BigDecimal("0.5")

        fun ticketType(varCode: String): TicketType {
            return if (varCode.contains("MELON")) {
                TicketType.MELON
            } else {
                TicketType.NOL
            }
        }
    }

    init {
        require(expirationAt.isAfter(ACTIVATION_DEAD_LINE)) { "ticket cannot be registered after expiration" }
    }

    fun applySellerOfferPrice(offerPrice: BigDecimal) {
        val isPerformanceDay = LocalDateTime.now().toLocalDate() == expirationAt.toLocalDate()
        if (isPerformanceDay) {
            val maxAllowedPrice = originalPrice.multiply(HALF)
            require(offerPrice <= maxAllowedPrice) {
                "on performance day, selling price must be <= 50% of regular price"
            }
        }

        this.sellingPrice = offerPrice
    }
}
