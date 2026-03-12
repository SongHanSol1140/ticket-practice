package org.example.user.infrastructure.repository

import org.example.user.domain.enum.UserRole
import org.example.user.domain.model.User
import org.springframework.stereotype.Repository
import java.math.BigDecimal

@Repository
class UserJpaRepository {
    private val users = mutableListOf(
        User(id = 1, name = "판매자1", role = UserRole.SELLER),
        User(id = 2, name = "판매자2", role = UserRole.SELLER),
        User(id = 3, name = "구매자1", role = UserRole.BUYER).apply { deposit(BigDecimal(10000)) },
        User(id = 4, name = "구매자2", role = UserRole.BUYER).apply { deposit(BigDecimal(50000)) },
    )

    fun findByName(name: String): User? = users.find { it.name == name }
}