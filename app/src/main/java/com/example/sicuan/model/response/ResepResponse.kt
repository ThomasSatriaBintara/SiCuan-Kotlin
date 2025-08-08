package com.example.sicuan.model.response

import com.google.gson.annotations.SerializedName

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

    @SerializedName("menuId")
    val menuId: String,

    @SerializedName("bahanId")
    val bahanId: String,

    @SerializedName("nama_bahan")
    val nama_bahan: String,

    val harga_beli: Int,
    val jumlah_beli: Double,

    val satuan: String,

    val jumlah_digunakan: Double,
    val biaya: Int
)

