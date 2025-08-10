package com.example.sicuan.model.response

data class StockDetailResponse(
    val success: Boolean,
    val message: String,
    val data: StockDetailData
)

data class StockDetailData(
    val stock: StockDetail
)

data class StockDetail(
    val id: String,
    val userId: String,
    val nama_bahan: String,
    val jumlah: Int,
    val satuan: String,
    val minimum_stock: Int,
    val createdAt: String,
    val updatedAt: String,
    val transactions: List<StockTransaction>
)

data class StockTransaction(
    val jenis_transaksi: String,
    val jumlah: Int,
    val keterangan: String,
    val createdAt: String
)