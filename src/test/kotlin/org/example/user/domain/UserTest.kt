package org.example.user.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.example.user.domain.enum.UserRole
import org.example.user.domain.model.User
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class UserTest {
    private fun createUser(
        name: String = "테스트유저",
        role: UserRole = UserRole.BUYER,
        money: BigDecimal = BigDecimal(0),
    ): User = User(name = name, role = role)

    @Test
    fun `입금에 성공한다`() {
        val user = createUser()
        user.deposit(BigDecimal(10000))
        assertThat(user.money).isEqualTo(BigDecimal(10000))
    }

    @Test
    fun `출금에 성공한다`() {
        val user = createUser(money = BigDecimal(10000))
        user.withdraw(BigDecimal(3000))
        assertThat(user.money).isEqualTo(BigDecimal(7000))
    }
    @Test
    fun `0원을 입금할 수 없다`(){
        val user = createUser(money = BigDecimal(10000))
        assertThrows<IllegalArgumentException> {
            user.deposit(BigDecimal.ZERO)
        }
    }
    @Test
    fun `0원을 출금할 수 없다`(){
        val user = createUser()
        assertThrows<IllegalArgumentException> {
            user.withdraw(BigDecimal.ZERO)
        }
    }
    @Test
    fun `잔액이 부족하면 입금할 수 없다`() {
        val user = createUser()
        assertThrows<IllegalArgumentException> {
            user.deposit(BigDecimal(5000))
        }
    }
    @Test
    fun `출금액이 부족하면 출금할 수 없다`() {
        val user = createUser()
        assertSoftly {
            assertThrows<IllegalArgumentException> {
                user.withdraw(BigDecimal(1000))
            }
        }
    }

}