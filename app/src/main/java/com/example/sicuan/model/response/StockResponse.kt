package com.example.sicuan.model.response

data class StockResponse(
    val success: Boolean,
    val message: String,
    val data: StockData
)

data class StockData(
    val stocks: List<Stock>
)

data class Stock(
    val id: String,
    val userId: String,
    val nama_bahan: String,
    val jumlah: Int,
    val satuan: String,
    val minimum_stock: Int
)