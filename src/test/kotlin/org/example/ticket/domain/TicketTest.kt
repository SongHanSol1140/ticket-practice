package org.example.ticket.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.example.ticket.domain.enum.TicketType
import org.example.ticket.domain.model.Ticket
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDateTime

class TicketTest {
    @Test
    fun `티켓_만들기`() {
        val ticket = Ticket(
            id = 1L,
            barcode = "ABCDEFGH",
            sellerName = "Seller A",
            expirationDateTime = LocalDateTime.now().plusHours(1),
            originalPrice = BigDecimal.TEN,
            ticketType = TicketType.MELON
        )

        assertSoftly {
            assertThat(ticket.id).isEqualTo(1L)
            assertThat(ticket.expirationDateTime).isNotNull
            assertThat(ticket.originalPrice).isEqualTo(BigDecimal.TEN)
        }
    }

    @Test
    fun `티켓 유효기간이 등록 기준 시간보다 늦으면 예외이다`() {
        assertThrows<IllegalArgumentException> {
            Ticket(
                barcode = "ABCDEFGH",
                sellerName = "Seller A",
                expirationDateTime = LocalDateTime.now().plusHours(1).minusSeconds(1),
                originalPrice = BigDecimal.TEN,
                ticketType = TicketType.MELON
            )
        }
    }

    @Test
    fun `공연 당일에는 가격을 정가의 50% 이하로만 설정 할 수 있다`() {
        val ticketPrice = BigDecimal.TEN
        val ticket = Ticket(
            id = 1L,
            barcode = "ABCDEFGH",
            sellerName = "Seller A",
            expirationDateTime = LocalDateTime.now().plusHours(1).minusSeconds(1),
            originalPrice = ticketPrice,
            ticketType = TicketType.MELON
        )

        assertThrows<IllegalArgumentException> {
            ticket.applySellerOfferPrice("seller_A", BigDecimal.valueOf(100.0))
        }
    }

    @Test
    fun `양도 가격은 정가를 초과 할 수 없다`() {
        assertThrows<IllegalArgumentException> {
            Ticket(
                id = 1L,
                barcode = "ABCDEFGH",
                sellerName = "Seller A",
                expirationDateTime = LocalDateTime.now().plusDays(1),
                originalPrice = BigDecimal.TEN,
                ticketType = TicketType.MELON
            )
        }
    }
}
