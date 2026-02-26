package org.example.user.domain.model


import org.example.user.domain.enum.UserRole
class User (
    val id: Long? = null,
    val name : String,
    val role : UserRole
){
    companion object{}
    init {}
}