package com.example.sicuan.repository

import com.example.sicuan.api.ApiClient
import com.example.sicuan.model.response.DashboardResponse
import com.example.sicuan.model.response.PenjualanResponse
import com.example.sicuan.model.response.ProfileResponse
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class MainRepository {

    suspend fun getProfile(): Response<ProfileResponse> {
        return ApiClient.instance.getProfile()
    }

    suspend fun getSalesSummary(): Response<DashboardResponse> {
        return ApiClient.instance.getSalesSummary()
    }

    suspend fun getSalesToday(): Response<PenjualanResponse> {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta"))
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        format.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
        val todayStr = format.format(calendar.time)

        return ApiClient.instance.getSalesCustom(todayStr, todayStr)
    }

}