package com.example.sicuan.model.response

data class PenjualanResponse(
    val success: Boolean,
    val message: String,
    val data: PenjualanData
)

data class PenjualanData(
    val sales: List<Penjualan>
)

data class Penjualan(
    val id: String,
    val userId: String,
    val nama_menu: String,
    val laku: Int,
    val income: Int,
    val profit: Int
)
