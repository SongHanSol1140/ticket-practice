package org.example.user.repository

import org.example.user.domain.enum.UserRole
import org.example.user.domain.model.User
import java.math.BigDecimal

object UserJpaRepository {
    private val users = listOf(
        User(id = 1, name = "판매자1", role = UserRole.SELLER, money = BigDecimal.ZERO),
        User(id = 2, name = "판매자2", role = UserRole.SELLER, money = BigDecimal.ZERO),
        User(id = 3, name = "구매자1", role = UserRole.BUYER, money = BigDecimal(10000)),
        User(id = 4, name = "구매자2", role = UserRole.BUYER, money = BigDecimal(50000)),
    )

    fun findByName(name: String): User? = users.find { it.name == name }



}