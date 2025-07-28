package com.example.sicuan.model.response

data class GeneralResponse(
    val success: Boolean,
    val message: String,
    val data: PesanData
)

data class PesanData(
    val message: String
)