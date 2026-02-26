package org.example.user.repository

import org.example.user.domain.enum.UserRole
import org.example.user.domain.model.User

object UserRepository {
    private val users = listOf(
        User(id = 1, name = "판매자1", role = UserRole.SELLER),
        User(id = 2, name = "판매자2", role = UserRole.SELLER),
        User(id = 3, name = "구매자1", role = UserRole.BUYER),
        User(id = 4, name = "구매자2", role = UserRole.BUYER),
    )

    fun findByName(name: String): User? = users.find { it.name == name }
}