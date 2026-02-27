package org.example.deal.domain

import org.example.deal.domain.model.Deal
import org.example.deal.domain.enum.DealStatus
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.assertj.core.api.Assertions.assertThat
import org.example.ticket.domain.enum.TicketType
import org.example.ticket.domain.model.Ticket
import java.math.BigDecimal
import java.time.LocalDateTime

class DealTest {

    @Test
    fun `판매자는 자신의 티켓을 구매할 수 없다`() {
        assertThrows<IllegalArgumentException> {
            Deal(
                barcode = "ABCDEFGH",
                sellerName = "판매자1",
                buyerName = "판매자1",
                sellingPrice = BigDecimal(10000)
            )
        }
    }

    @Test
    fun `거래 생성 시 상태는 RESERVED이다`() {
        val deal = Deal(
            barcode = "ABCDEFGH",
            sellerName = "판매자1",
            buyerName = "구매자1",
            sellingPrice = BigDecimal(10000)
        )
        assertThat(deal.getDealStatus()).isEqualTo(DealStatus.RESERVED)
    }


    @Test
    fun `예약 상태인 거래만 완료할 수 있다`() {
        val deal = Deal(
            barcode = "ABCDEFGH",
            sellerName = "판매자1",
            buyerName = "구매자1",
            sellingPrice = BigDecimal(10000),
            dealStatus = DealStatus.COMPLETED
        )
        assertThrows<IllegalArgumentException> {
            deal.dealComplete()
        }
    }

    @Test
    fun `10분이 지나면 예약이 만료된다`() {
        val deal = Deal(
            barcode = "ABCDEFGH",
            sellerName = "판매자1",
            buyerName = "구매자1",
            sellingPrice = BigDecimal(10000),
            reservedDateTime = LocalDateTime.now().minusMinutes(11) // 11분 전
        )
        assertThat(deal.reservedTimeExpiredCheck()).isTrue()
    }
}

