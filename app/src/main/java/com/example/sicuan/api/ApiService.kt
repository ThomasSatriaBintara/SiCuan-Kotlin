package com.example.sicuan.api

import com.example.sicuan.model.request.JualRequest
import com.example.sicuan.model.request.LoginRequest
import com.example.sicuan.model.request.LupaRequest
import com.example.sicuan.model.request.MenuRequest
import com.example.sicuan.model.request.PenjualanRequest
import com.example.sicuan.model.response.LoginResponse
import com.example.sicuan.model.request.RegisterRequest
import com.example.sicuan.model.request.ResetRequest
import com.example.sicuan.model.request.SetPasswordRequest
import com.example.sicuan.model.request.StokRequest
import com.example.sicuan.model.request.TambahResepRequest
import com.example.sicuan.model.response.DashboardResponse
import com.example.sicuan.model.response.GeneralResponse
import com.example.sicuan.model.response.MenuResponse
import com.example.sicuan.model.response.OTPResponse
import com.example.sicuan.model.response.PenjualanResponse
import com.example.sicuan.model.response.ProfileResponse
import com.example.sicuan.model.response.ResepResponse
import com.example.sicuan.model.response.StockResponse
import com.example.sicuan.model.response.TambahResepResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("/auth/register") //DONE FIX
    suspend fun registerUser(
        @Body request: RegisterRequest
    ): Response<GeneralResponse>

    @POST("/auth/login") //DONE FIX
    suspend fun loginUser(
        @Body request: LoginRequest,
    ): Response<LoginResponse>

    @POST("/auth/forget-password") //DONE FIX
    suspend fun forgetPassword(
        @Body request: LupaRequest
    ): Response<GeneralResponse>

    @POST("/auth/verify-otp") //DONE FIX
    suspend fun verifyOtp(
        @Body otp: Map<String, String>
    ): Response<OTPResponse>

    @POST("/auth/reset-password") //DONE FIX
    suspend fun resetPassword(
        @Header("Authorization") token: String,
        @Body request: ResetRequest
    ): Response<GeneralResponse>

    @GET("/profiles/") //DONE FIX
    suspend fun getProfile(
    ): Response<ProfileResponse>

    @PATCH("/profiles/password") //DONE FIX
    suspend fun patchPassword(
        @Body request: SetPasswordRequest
    ): Response<GeneralResponse>

    @GET("/menus") // DONE FIX
    suspend fun getMenus(
    ): Response<MenuResponse>

    //GET MENU DETAIL BELUM

    @POST("/menus") // DONE FIX
    suspend fun addMenu(
        @Body request: MenuRequest
    ): Response<GeneralResponse>

    @PATCH("/menus/{menu_id}") // DONE FIX
    suspend fun updateMenu(
        @Path("menu_id") menuId: String,
        @Body request: MenuRequest
    ): Response<GeneralResponse>

    @DELETE("/menus/{menu_id}") // DONE FIX
    suspend fun deleteMenu(
        @Path("menu_id") menuId: String
    ): Response<GeneralResponse>

    @PATCH("/menus/selling-price") // DONE FIX
    suspend fun patchHargaJual(
        @Body request: JualRequest
    ): Response<GeneralResponse>

    @GET("/recipes/{menu_id}") // DONE FIX
    fun getResep(
        @Path("menu_id") menuId: String
    ): Call<ResepResponse>

    // GET MENU RESEP DETAIL BELUM

    @POST("/recipes/{menu_id}") // DONE FIX
    fun addResep(
        @Path("menu_id") menuId: String,
        @Body bahan: TambahResepRequest
    ): Call<TambahResepResponse<Any>>

    @PUT("/recipes/{menu_id}/{recipe_id}") // DONE FIX
    suspend fun updateResep(
        @Path("menu_id") menuId: String,
        @Path("recipe_id") recipeId: String,
        @Body request: TambahResepRequest
    ): Response<GeneralResponse>

    @DELETE("/recipes/{menu_id}/{recipe_id}") // DONE FIX
    suspend fun deleteBahan(
        @Path("menu_id") menuId: String,
        @Path("recipe_id") recipeId: String
    ): Response<GeneralResponse>

    @GET("/stocks") // DONE FIX
    suspend fun getStocks(
    ): Response<StockResponse>

    // GET STOCK DETAIL BELUM

    @PATCH("/stocks/{bahan_id}") // DONE FIX
    suspend fun patchStock(
        @Path("bahan_id") bahanId: String,
        @Body request: StokRequest
    ): Response<GeneralResponse>

    @DELETE("/stocks/{bahan_id}") // DONE FIX
    suspend fun deleteStock(
        @Path("bahan_id") bahanId: String
    ): Response<GeneralResponse>

    @GET("/sales") // DONE TAPI LOKAL
    suspend fun getSalesToday(
    ): Response<PenjualanResponse>

    @GET("/sales/custom") // DONE FIX
    suspend fun getSalesCustom(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Response<PenjualanResponse>

    @DELETE("/sales/{sales_id}") // DONE FIX
    suspend fun deletePenjualan(
        @Path("sales_id") salesId: String
    ): Response<GeneralResponse>

    @POST("/sales") // DONE FIX
    suspend fun postSales(
        @Body request: PenjualanRequest
    ): Response<GeneralResponse>

    // GET STOCK SUMMARY BELUM

    @GET("/dashboard/sales-summary") // DONE FIX
    suspend fun getSalesSummary(
    ): Response<DashboardResponse>

}