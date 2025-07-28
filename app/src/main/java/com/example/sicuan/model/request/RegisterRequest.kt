package com.example.sicuan.model.request

data class RegisterRequest(
    val email: String,
    val username: String,
    val password: String,
    val confirmPassword: String,
    val nama_usaha: String,
)
