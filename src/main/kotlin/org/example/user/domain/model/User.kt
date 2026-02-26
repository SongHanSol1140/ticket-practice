package org.example.user.domain.model


import org.example.user.domain.enum.UserRole
import java.math.BigDecimal

class User (
    val id: Long? = null,
    val name: String,
    val role: UserRole,
    var money: BigDecimal = BigDecimal.ZERO,
){
    companion object{}
    init {}
    fun deposit(amount: BigDecimal) {
        require(amount > BigDecimal.ZERO) { "입금액은 0보다 커야 합니다." }
        money = money.add(amount)
    }

    fun withdraw(amount: BigDecimal) {
        require(amount > BigDecimal.ZERO) { "출금액은 0보다 커야 합니다." }
        require(money >= amount) { "잔액이 부족합니다." }
        money = money.subtract(amount)
    }

}