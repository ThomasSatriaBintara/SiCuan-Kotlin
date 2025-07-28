package com.example.sicuan.model.response

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val data: LoginData
)

data class LoginData(
    val message: String,
    val userID: String,
    val username: String,
    val deviceInfo: String,
    val access_token: String,
    val expiresAt: String
)