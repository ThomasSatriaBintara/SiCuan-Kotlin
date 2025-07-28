package com.example.sicuan.api

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://sicuan-api-678045816602.asia-southeast2.run.app"

    // Context untuk mengakses SharedPreferences
    private var appContext: Context? = null

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    private val retrofit: Retrofit by lazy {
        // Logging interceptor untuk debugging
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val authInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")

            // URL endpoint yang tidak perlu Authorization
            val excludedPaths = listOf(
                "/auth/login",
                "/auth/register",
                "/auth/forget-password",
                "/auth/reset-password"
            )

            val requestPath = originalRequest.url.encodedPath

            // Tambahkan Authorization hanya jika endpoint tidak ada dalam pengecualian
            if (excludedPaths.none { requestPath.contains(it) }) {
                val token = getAuthToken()
                if (token.isNotEmpty()) {
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                }
            }

            chain.proceed(requestBuilder.build())
        }

        // Build OkHttpClient dengan interceptors
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val instance: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    private fun getAuthToken(): String {
        return try {
            appContext?.let { context ->
                val sharedPref = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                sharedPref.getString("token", "") ?: ""
            } ?: ""
        } catch (e: Exception) {
            ""
        }
    }
}