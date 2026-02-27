package org.example.deal.infrastructure

import org.example.deal.domain.enum.PaymentType
import java.math.BigDecimal

interface PaymentGateway{
    fun pay(buyerName:String, sellerName:String, price: BigDecimal)
    fun type(): PaymentType
}