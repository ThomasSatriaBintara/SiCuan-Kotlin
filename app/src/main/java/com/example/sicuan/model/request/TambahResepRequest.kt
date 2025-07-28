package com.example.sicuan.model.request

data class TambahResepRequest(
    val nama_bahan: String,
    val harga_beli: Int,
    val jumlah_beli: Int,
    val satuan: String,
    val jumlah_digunakan: Int,
)