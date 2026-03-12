
package org.example.deal.infrastructure.api

import org.assertj.core.api.Assertions.assertThat
import org.example.user.domain.enum.UserRole
import org.example.user.domain.model.User
import org.example.user.infrastructure.repository.UserJpaRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal

@ExtendWith(MockitoExtension::class)
class KakaoPaymentGatewayTest {

    @Mock
    private lateinit var userJpaRepository: UserJpaRepository

    @InjectMocks
    private lateinit var kakaoPaymentGateway: KakaoPaymentGateway

    @Test
    fun `결제에 성공한다`() {
        val buyer = User(name = "구매자1", role = UserRole.BUYER).apply {
            deposit(BigDecimal(50000)) }
        val seller = User(name = "판매자1", role = UserRole.SELLER)

        `when`(userJpaRepository.findByName("구매자1")).thenReturn(buyer)
        `when`(userJpaRepository.findByName("판매자1")).thenReturn(seller)

        kakaoPaymentGateway.pay("구매자1", "판매자1", BigDecimal(10000))
        assertThat(buyer.money).isEqualTo(BigDecimal(40000))
        assert(seller.money == BigDecimal(10000))
    }

    @Test
    fun `존재하지 않는 구매자일경우 구매할 수 없다`() {
        // Mock은 기본적으로 null을 반환하므로
        // findByName("없는유저") → null → requireNotNull에서 예외 발생
        assertThrows<IllegalArgumentException> {
            kakaoPaymentGateway.pay("없는유저", "판매자1", BigDecimal(10000))
        }
    }
}