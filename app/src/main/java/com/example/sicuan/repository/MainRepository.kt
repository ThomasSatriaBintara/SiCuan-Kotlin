package com.example.sicuan.repository

import com.example.sicuan.api.ApiClient
import com.example.sicuan.model.response.ProfileResponse
import retrofit2.Response

class MainRepository {
    suspend fun getProfile(): Response<ProfileResponse> {
        return ApiClient.instance.getProfile()
    }
}
