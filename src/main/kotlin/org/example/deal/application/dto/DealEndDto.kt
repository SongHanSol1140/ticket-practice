package org.example.deal.application.dto

import org.example.deal.domain.enum.PaymentType

data class DealEndDto(
    val barcode: String,
    val payementType: PaymentType
)
