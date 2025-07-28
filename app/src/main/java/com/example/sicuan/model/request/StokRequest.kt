package com.example.sicuan.model.request

data class StokRequest(
    val nama_bahan: String,
    val jumlah: Int,
    val jenis_transaksi: String,
    val keterangan: String,
    val minimum_stock: Int
)