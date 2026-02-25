package org.example.ticket.domain.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.example.ticket.domain.enum.TicketStatus
import org.example.ticket.domain.enum.TicketType
import org.springframework.cglib.core.Local
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
class Ticket(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null, // ? => DB에 넘기기전에 id를 안넣으려고 (자동생성)
    val barcode: String,
    val expirationDateTime: LocalDateTime,
    val originalPrice: BigDecimal, // BigDecimal => 10진수로 저장(소수점 계산을 위해)
    var sellingPrice: BigDecimal? = null,
    var ticketStatus: TicketStatus = TicketStatus.ON_SALE,
    val ticketType: TicketType
) {
    companion object {
        fun getActivationDeadLine(): LocalDateTime {
            return LocalDateTime.now().minusHours(1);
        };

        fun ticketBarcodeCheck(barcode: String) {
            require(barcode.length == 8) { "바코드는 8자리여야 합니다." }
        }

        fun ticketTimeCheck(performanceDateTime: LocalDateTime) {
            val now = LocalDateTime.now()
            val deadLine: LocalDateTime = performanceDateTime.minusHours(1)
            require(now.isAfter(deadLine)) {
                "공연 시작 1시간 전까지만 등록할 수 있습니다."
            }
        }

        fun ticketTypeCheck(barcode: String): TicketType {
            return when {
                barcode.all { it.isLetter() } -> TicketType.MELON
                barcode.all { it.isDigit() } -> TicketType.NOL
                else -> TicketType.MOL
            }

        }


    }

    init {
        ticketBarcodeCheck(barcode);
    }

    fun applySellerOfferPrice(offerPrice: BigDecimal) {
        val isPerformanceDateTime = LocalDateTime.now().toLocalDate() == expirationDateTime.toLocalDate()
    }

}



