package org.example.deal.infrastructure

import org.example.deal.domain.enum.PaymentType
import org.springframework.stereotype.Component
import org.example.user.repository.UserJpaRepository
import java.math.BigDecimal


@Component
class KakaoPaymentGateway: PaymentGateway {
    override fun pay(buyerName: String, sellerName: String, price: BigDecimal) {
        val buyer = requireNotNull(UserJpaRepository.findByName(buyerName)) { "구매자 정보를 찾을 수 없습니다." }
        val seller = requireNotNull(UserJpaRepository.findByName(sellerName)) { "판매자 정보를 찾을 수 없습니다." }
        buyer.withdraw(price)
        seller.deposit(price)
    }
    override fun type(): PaymentType {
        return PaymentType.KAKAO
    }
}