package com.example.sicuan.model.request

data class SetPasswordRequest(
    val password: String,
    val confirmPassword: String
)