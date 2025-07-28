package com.example.sicuan.api

import com.example.sicuan.model.request.JualRequest
import com.example.sicuan.model.request.LoginRequest
import com.example.sicuan.model.request.LupaRequest
import com.example.sicuan.model.request.MenuRequest
import com.example.sicuan.model.request.PenjualanRequest
import com.example.sicuan.model.response.LoginResponse
import com.example.sicuan.model.request.RegisterRequest
import com.example.sicuan.model.request.ResetRequest
import com.example.sicuan.model.request.StokRequest
import com.example.sicuan.model.request.TambahResepRequest
import com.example.sicuan.model.response.GeneralResponse
import com.example.sicuan.model.response.MenuResponse
import com.example.sicuan.model.response.OTPResponse
import com.example.sicuan.model.response.ProfileResponse
import com.example.sicuan.model.response.ResepResponse
import com.example.sicuan.model.response.StockResponse
import com.example.sicuan.model.response.TambahResepResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("/auth/login")
    suspend fun loginUser(
        @Body request: LoginRequest,
    ): Response<LoginResponse>

    @POST("/auth/register")
    suspend fun registerUser(
        @Body request: RegisterRequest
    ): Response<GeneralResponse>

    @GET("/profiles/")
    suspend fun getProfile(
    ): Response<ProfileResponse>

    @POST("/auth/forget-password")
    suspend fun forgetPassword(
        @Body request: LupaRequest
    ): Response<GeneralResponse>

    @POST("/auth/verify-otp")
    suspend fun verifyOtp(
        @Body otp: Map<String, String>
    ): Response<OTPResponse>

    @POST("/auth/reset-password")
    suspend fun resetPassword(
        @Header("Authorization") token: String,
        @Body request: ResetRequest
    ): Response<GeneralResponse>

    @POST("/menus")
    suspend fun addMenu(
        @Body request: MenuRequest
    ): Response<GeneralResponse>

    @GET("/menus")
    suspend fun getMenus(
    ): Response<MenuResponse>

    @POST("/recipes/{menu_id}")
    fun addResep(
        @Path("menu_id") menuId: String,
        @Body bahan: TambahResepRequest
    ): Call<TambahResepResponse<Any>>

    @GET("/recipes/{menu_id}")
    fun getResep(
        @Path("menu_id") menuId: String
    ): Call<ResepResponse>

    @PATCH("/menus/selling-price")
    suspend fun patchHargaJual(
        @Body request: JualRequest
    ): Response<GeneralResponse>

    @GET("/stocks")
    suspend fun getStocks(
    ): Response<StockResponse>

    @PATCH("/stocks/{bahan_id}")
    suspend fun patchStock(
        @Path("bahan_id") bahanId: String,
        @Body request: StokRequest
    ): Response<GeneralResponse>

    @POST("/sales/")
    suspend fun postSales(
        @Body request: PenjualanRequest
    ): Response<GeneralResponse>


}