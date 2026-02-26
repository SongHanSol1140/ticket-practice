package org.example.ticket.application.dto

import java.math.BigDecimal

data class TicketSellingPriceOfferDto(
    val sellerName: String,
    val barcode: String,
    val sellingPrice: BigDecimal,

)
