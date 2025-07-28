package com.example.sicuan.model.response

data class ResepResponse(
    val success: Boolean,
    val message: String,
    val data: RecipeData
)

data class RecipeData(
    val recipes: List<Resep>
)

data class Resep(
    val id: String,
    val menuid: String,
    val bahanid: String,
    val nama_bahan: String,
    val harga_beli: Int,
    val jumlah_beli: Double,
    val jumlah_digunakan: Double,
    val biaya: Int
)
