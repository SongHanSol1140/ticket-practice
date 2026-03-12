package org.example.deal.infrastructure.api

import org.example.deal.domain.enum.PaymentType
import org.example.user.infrastructure.repository.UserJpaRepository
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class TossPaymentGateway(
    private val userJpaRepository: UserJpaRepository
) : PaymentGateway {
    override fun pay(buyerName: String, sellerName: String, price: BigDecimal) {
        val buyer = requireNotNull(userJpaRepository.findByName(buyerName)) { "구매자 정보를 찾을 수 없습니다." }
        val seller = requireNotNull(userJpaRepository.findByName(sellerName)) { "판매자 정보를 찾을 수 없습니다." }
        buyer.withdraw(price)
        seller.deposit(price)
    }

    override fun type(): PaymentType {
        return PaymentType.TOSS
    }
}