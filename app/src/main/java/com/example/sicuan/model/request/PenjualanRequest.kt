package com.example.sicuan.model.request

data class PenjualanRequest(
    val tanggal: String,
    val nama_menu: String,
    val jumlah_laku: Int,
    val keterangan: String? = null
)