package org.example.ticket.domain.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.example.ticket.domain.enum.TicketStatus
import org.example.ticket.domain.enum.TicketType
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
class Ticket(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null, // ? => DB에 넘기기전에 id를 안넣으려고 (자동생성)
    val barcode: String,
    val expirationDateTime: LocalDateTime,
    val originalPrice: BigDecimal, // BigDecimal => 10진수로 저장(소수점 계산을 위해)
    val sellerName: String,
    var sellingPrice: BigDecimal? = null,
    private var ticketStatus: TicketStatus = TicketStatus.ON_SALE,
    val ticketType: TicketType
) {
    companion object {
        private val HALF = BigDecimal("0.5")

        fun ticketBarcodeCheck(barcode: String) {
            require(barcode.length == 8) { "바코드는 8자리여야 합니다." }
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
        ticketBarcodeCheck(barcode)
        ticketTimeCheck(expirationDateTime)
    }

    fun getTicketDeadLine(): LocalDateTime {
        return expirationDateTime.minusHours(1);
    };

    fun ticketTimeCheck(performanceDateTime: LocalDateTime) {
        val now = LocalDateTime.now()
        val deadLine: LocalDateTime = getTicketDeadLine();
        require(now.isBefore(deadLine)) {
            "공연 시작 1시간 전까지만 등록할 수 있습니다."
        }
    }

    fun applySellerOfferPrice(offerSellerName: String, offerPrice: BigDecimal) {
        require(sellerName == offerSellerName) { "티켓에 등록된 판매자가 아닙니다." }
        require(offerPrice >= BigDecimal.ZERO) { "판매가격에 음수는 입력할 수 없습니다." }
        require(offerPrice <= originalPrice) { "티켓의 가격은 정가를 초과할 수 없습니다." }
        val isPerformanceDateTime = LocalDateTime.now().toLocalDate() == expirationDateTime.toLocalDate()
        if (isPerformanceDateTime) {
            val maxAllowedPrice = originalPrice.multiply(HALF)
            require(offerPrice <= maxAllowedPrice) { "공연 당일에는 가격을 정가의 50% 이하로만 설정할 수 있습니다." }
        }

        sellingPrice = offerPrice
    }
    fun reSale(offerSellerName: String, offerPrice: BigDecimal) {
        require(ticketStatus == TicketStatus.SOLD) { "구매한 티켓만 판매할 수 있습니다." }
        ticketStatus = TicketStatus.ON_SALE
        applySellerOfferPrice(offerSellerName, offerPrice);
    }

    fun ticketOnSale() {
        require(ticketStatus == TicketStatus.RESERVED) { "예약중인 티켓만 판매중으로 되돌릴 수 있습니다." }
        ticketStatus = TicketStatus.ON_SALE
    }

    fun ticketReserve() {
        require(ticketStatus == TicketStatus.ON_SALE) { "판매중인 티켓만 예약할 수 있습니다"   }
        ticketStatus = TicketStatus.RESERVED
    }

    fun ticketSold() {
        require(ticketStatus == TicketStatus.RESERVED) { "예약중인 티켓만 판매완료할 수 있습니다." }
        ticketStatus = TicketStatus.SOLD
    }

    fun ticketExpired() {
        require(ticketStatus != TicketStatus.SOLD) { "판매완료된 티켓은 만료할 수 없습니다." }
        val now = LocalDateTime.now()
        val deadLine = getTicketDeadLine()
        require(now.isAfter(deadLine)) { "만료되지 않은 티켓입니다." }
        ticketStatus = TicketStatus.EXPIRED
    }

}



