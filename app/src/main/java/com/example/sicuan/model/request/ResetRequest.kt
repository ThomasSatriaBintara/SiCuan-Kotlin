package com.example.sicuan.model.request

data class ResetRequest(
    val otp: String,
    val newPassword: String,
    val confirmPassword: String
)