package com.example.cartoonchar.network.login

import androidx.annotation.Keep

@Keep
data class LoginResponse(
    val status: Boolean,
    val message: String,
    val data: OneUser?,
    val token: String?
)

@Keep
data class OneUser(
    val id: String,
    val email: String
)
