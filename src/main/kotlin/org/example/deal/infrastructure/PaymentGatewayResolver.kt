package org.example.deal.infrastructure

import org.example.deal.domain.enum.PaymentType
import org.springframework.stereotype.Component

@Component
class PaymentGatewayResolver(
    private val gateways: List<PaymentGateway>
) {
    fun resolve(offerPaymentType: PaymentType): PaymentGateway {
        val paymentType = gateways.find { it.type() == offerPaymentType }
        return paymentType ?: throw IllegalArgumentException("지원하지 않는 결제 수단입니다.")
    }
}