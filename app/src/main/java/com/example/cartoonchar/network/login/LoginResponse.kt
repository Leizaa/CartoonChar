package com.example.cartoonchar.network.login

data class LoginResponse(
    val status: Boolean,
    val message: String,
    val data: OneUser?,
    val token: String?
)

data class OneUser(
    val id: String,
    val email: String
)
