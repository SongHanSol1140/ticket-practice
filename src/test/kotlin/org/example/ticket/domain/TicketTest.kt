package org.example.ticket.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.example.ticket.domain.model.Ticket
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDateTime

class TicketTest {
    @Test
    fun `티켓_만들기`() {
        val ticket = Ticket(1L, LocalDateTime.now(), BigDecimal.TEN)

        assertSoftly {
            assertThat(ticket.id).isEqualTo(1L)
            assertThat(ticket.expirationAt).isNotNull
            assertThat(ticket.originalPrice).isEqualTo(BigDecimal.TEN)
        }
    }

    @Test
    fun `티켓 유효기간이 등록 기준 시간보다 늦으면 예외이다`() {
        assertThrows<IllegalArgumentException> {
            Ticket(expirationAt = Ticket.ACTIVATION_DEAD_LINE.minusSeconds(1), originalPrice = BigDecimal.TEN)
        }
    }

    @Test
    fun `공연 당일에는 가격을 정가의 50% 이하로만 설정 할 수 있다`() {
        val ticketPrice = BigDecimal.TEN
        val ticket = Ticket(1L, Ticket.ACTIVATION_DEAD_LINE.plusSeconds(1), ticketPrice)

        assertThrows<IllegalArgumentException> {
            ticket.applySellerOfferPrice(BigDecimal.valueOf(100.0))
        }
    }

    @Test
    fun `양도 가격은 정가를 초과 할 수 없다`() {
        // API
        assertThrows<IllegalArgumentException> {
            Ticket(1L, LocalDateTime.now(), BigDecimal.TEN)
        }
    }
}
