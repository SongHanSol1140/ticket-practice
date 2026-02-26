package org.example.deal.domain.model

import org.example.deal.domain.enum.DealStatus
import java.math.BigDecimal
import java.time.LocalDateTime

class Deal(
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
        require(dealStatus == DealStatus.RESERVED) { "예약 상태인 거래만 완료할 수 있습니다." }
        dealStatus = DealStatus.COMPLETED
    }
    fun reservedTimeExpiredCheck():Boolean{
        return dealStatus == DealStatus.RESERVED && reservedDateTime.plusMinutes(10).isBefore(LocalDateTime.now())
    }

}
