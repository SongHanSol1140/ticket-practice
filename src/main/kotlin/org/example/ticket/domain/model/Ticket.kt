package org.example.ticket.domain.model

import org.example.ticket.domain.enum.TicketStatus
import org.example.ticket.domain.enum.TicketType
import org.springframework.cglib.core.Local
import java.math.BigDecimal
import java.time.LocalDateTime

class Ticket(
    val id: Long? = null, // ? => DB에 넘기기전에 id를 안넣으려고 (자동생성)
    val barcode: String,
    val eventDateTime: LocalDateTime,
    val originalPrice: BigDecimal, // BigDecimal => 10진수로 저장(소수점 계산을 위해)
    val sellingPrice: BigDecimal? = null,
    var ticketStatus: TicketStatus = TicketStatus.ON_SALE,
    var ticketType: TicketType? = null
) {
    companion object {
        fun ticketBarcodeCheck(barcode: String) {
            require(barcode.length == 8) { "바코드는 8자리여야 합니다." }
        }

        fun ticketTimeCheck(eventTime: LocalDateTime) {
            val deadLine: LocalDateTime = eventTime.plusHours(3)
            require(eventTime.isAfter(deadLine)) {
                "공연 시작 3시간 전까지만 등록할 수 있습니다."
            }
        }

        fun ticketTypeCheck(barcode: String): TicketType {
            return if (barcode.all { it.isLetter() }) {
                TicketType.MELON
            } else {
                TicketType.NOL
            }
        }


    }

    init {
        ticketBarcodeCheck(barcode);
        ticketTimeCheck(eventDateTime);
        ticketType = ticketTypeCheck(barcode);
    }

}



