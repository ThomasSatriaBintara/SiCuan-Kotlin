package com.example.sicuan.model.response

data class OTPResponse(
    val success: Boolean,
    val message: String,
    val data: OTPData
)

data class OTPData(
    val message: String,
    val resetToken: String
)