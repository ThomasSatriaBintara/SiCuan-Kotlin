package com.example.sicuan.model.response

data class ProfileResponse(
    val success: Boolean,
    val message: String,
    val data: ProfileData
)

data class ProfileData(
    val profile: Profile
)

data class Profile(
    val userId: String,
    val username: String,
    val email: String,
    val nama_usaha: String
)