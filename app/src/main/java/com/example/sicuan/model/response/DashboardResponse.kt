package com.example.sicuan.model.response

data class DashboardResponse(
    val success: Boolean,
    val message: String,
    val data: SalesSummaryData
)

data class SalesSummaryData(
    val totalPenjualan: Int,
    val totalKeuntungan: Int
)
