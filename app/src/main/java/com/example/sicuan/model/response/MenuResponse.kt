package com.example.sicuan.model.response

data class MenuResponse(
    val success: Boolean,
    val message: String,
    val data: MenuData
)

data class MenuData(
    val menus: List<Menu>
)

data class Menu(
    val id: String,
    val userId: String,
    val nama_menu: String,
    val hpp: Int,
    val keuntungan: Int?,
    val harga_jual: Int?,
    val createdAt: String,
    val updatedAt: String
)