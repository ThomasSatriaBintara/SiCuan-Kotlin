package com.example.sicuan.model.response

data class TambahResepResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null
)