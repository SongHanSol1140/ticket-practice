package org.example.deal.application.dto

import org.example.deal.domain.enum.DealStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class DealResponseDto(
    val barcode: String,
    val sellerName: String,
    val buyerName: String,
    val sellingPrice: BigDecimal,
    val reservedDateTime: LocalDateTime,
    val dealStatus: DealStatus
)
