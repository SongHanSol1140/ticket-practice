package org.example.ticket.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.example.ticket.domain.enum.TicketStatus
import org.example.ticket.domain.enum.TicketType
import org.example.ticket.domain.model.Ticket
import org.example.user.domain.enum.UserRole
import org.example.user.domain.model.User
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDateTime

class TicketTest {
    private fun createUser(
        name: String = "테스트유저",
        role: UserRole = UserRole.BUYER,
    ): User = User(name = name, role = role)

    private fun createTicket(
        barcode: String = "ABCDEFGH",
        sellerName: String = "Seller A",
        expirationDateTime: LocalDateTime = LocalDateTime.now().plusHours(2),
        originalPrice: BigDecimal = BigDecimal.TEN,
        ticketType: TicketType = TicketType.MELON
    ): Ticket {
        return Ticket(
            barcode = barcode,
            sellerName = sellerName,
            expirationDateTime = expirationDateTime,
            originalPrice = originalPrice,
            ticketType = ticketType
        )
    }

    @Test
    fun `티켓_만들기`() {
        val ticket = createTicket();
        assertSoftly {
            assertThat(ticket.id).isEqualTo(1L)
            assertThat(ticket.ticketStatus).isEqualTo(TicketStatus.ON_SALE)
            assertThat(ticket.expirationDateTime).isNotNull
            assertThat(ticket.originalPrice).isEqualTo(BigDecimal.TEN)
        }
    }
    @Test
    fun `바코드가 8자리가 아니면 에러`() {
        assertThrows<IllegalArgumentException> {
            val ticket = createTicket(barcode= "ABC")
        }
    }



    @Test
    fun `티켓 유효기간이 등록 기준 시간보다 늦으면 예외이다`() {
        assertThrows<IllegalArgumentException> {
            createTicket(expirationDateTime = LocalDateTime.now().plusHours(1).minusSeconds(1),)
        }
    }

    @Test
    fun `공연 당일에는 가격을 정가의 50% 이하로만 설정 할 수 있다`() {
        val ticket = createTicket(originalPrice = BigDecimal(10000))
        assertThrows<IllegalArgumentException> {
            ticket.applySellerOfferPrice("Seller A", BigDecimal(6000))
        }
    }

    @Test
    fun `양도 가격은 정가를 초과 할 수 없다`() {
        val ticket = Ticket(
            id = 1L,
            barcode = "ABCDEFGH",
            sellerName = "Seller A",
            expirationDateTime = LocalDateTime.now().plusDays(1),
            originalPrice = BigDecimal.TEN,
            ticketType = TicketType.MELON
        )

        assertThrows<IllegalArgumentException> {
            ticket.applySellerOfferPrice("Seller A", BigDecimal(20))
        }
    }
    @Test
    fun `판매중인 티켓을 예약할 수 있다`() {
        val ticket = Ticket(
            id = 1L,
            barcode = "ABCDEFGH",
            sellerName = "Seller A",
            expirationDateTime = LocalDateTime.now().plusHours(2),
            originalPrice = BigDecimal.TEN,
            ticketType = TicketType.MELON
        )
        ticket.ticketReserve()
        assertThat(ticket.ticketStatus).isEqualTo(TicketStatus.RESERVED)
    }
    @Test
    fun `예약중인 티켓을 판매중으로 되돌릴 수 있다`() {
        val ticket = Ticket(
            id = 1L,
            barcode = "ABCDEFGH",
            sellerName = "Seller A",
            expirationDateTime = LocalDateTime.now().plusHours(2),
            originalPrice = BigDecimal.TEN,
            ticketType = TicketType.MELON
        )
        ticket.ticketReserve()
        ticket.ticketOnSale()
        assertThat(ticket.ticketStatus).isEqualTo(TicketStatus.ON_SALE)
    }
    @Test
    fun `예약중인 티켓만 판매완료할 수 있다`() {
        val ticket = Ticket(
            id = 1L,
            barcode = "ABCDEFGH",
            sellerName = "Seller A",
            expirationDateTime = LocalDateTime.now().plusHours(2),
            originalPrice = BigDecimal.TEN,
            ticketType = TicketType.MELON,
        )
        ticket.ticketReserve()
        ticket.ticketSold()
        assertThat(ticket.ticketStatus).isEqualTo(TicketStatus.SOLD)
    }

    @Test
    fun `판매중 상태에서 바로 판매완료할 수 없다`() {
        val ticket = Ticket(
            id = 1L,
            barcode = "ABCDEFGH",
            sellerName = "Seller A",
            expirationDateTime = LocalDateTime.now().plusHours(2),
            originalPrice = BigDecimal.TEN,
            ticketType = TicketType.MELON,
        )
        assertThrows<IllegalArgumentException> {
            ticket.ticketSold()
        }
    }



}
