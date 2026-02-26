package org.example.deal.domain.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.example.deal.domain.enum.DealStatus
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
class Deal(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val barcode: String,
    val sellerName: String,
    val buyerName: String,
    val sellingPrice: BigDecimal,
    val reservedDateTime: LocalDateTime = LocalDateTime.now(),
    var dealStatus: DealStatus = DealStatus.RESERVED
) {
    companion object {}

    init {
        require(sellerName != buyerName) { "본인이 등록한 티켓은 구매할 수 없습니다." }
    }

    fun dealComplete(){
        require(dealStatus == DealStatus.RESERVED) { "예약 상태인 거래만 완료할 수 있습니다." }
        dealStatus = DealStatus.COMPLETED

    }
    fun dealCancel(){
        require(dealStatus == DealStatus.RESERVED) { "예약 상태인 거래만 취소 할 수 있습니다." }
        dealStatus = DealStatus.CANCELLED
    }
    fun reservedTimeExpiredCheck():Boolean{
        return dealStatus == DealStatus.RESERVED && reservedDateTime.plusMinutes(10).isBefore(LocalDateTime.now())
    }

}
