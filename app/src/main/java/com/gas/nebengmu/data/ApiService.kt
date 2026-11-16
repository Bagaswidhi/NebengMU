package com.gas.nebengmu.data

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

// 1. Format Data yang dikirim (Request) dan Diterima (Response)
data class AuthRequest(
    val email: String,
    val password: String,
    val nim: String? = null,
    val nama_lengkap: String? = null,
    val no_hp: String? = null,
    val gender: String? = null,          // "L" atau "P"
    val jenis_kendaraan: String? = null, // Khusus Pengemudi ("Sepeda Motor" / "Mobil")
    val no_plat: String? = null,         // Khusus Pengemudi
    val ktm_url: String? = null          // Khusus Pengemudi
)

data class AuthResponse(
    val message: String,
    val token: String? = null,
    val role: String? = null,
    val name: String? = null,
    val nim: String? = null,
    val gender: String? = null,
    val kendaraan: String? = null,
    val no_plat: String? = null
)

data class RideRequest(
    val passenger_nim: String,
    val pickup_location: String,
    val destination_location: String,
    val ride_mode: String,
    val fare: Double
)

data class Ride(
    val ride_id: Int,
    val passenger_nim: String,
    val passenger_name: String? = null,
    val passenger_gender: String? = null,
    val pickup_location: String,
    val destination_location: String,
    val ride_mode: String,
    val fare: Double,
    val status: String,
    val created_at: String? = null
)

data class RideResponse(
    val message: String
)

data class EarningsResponse(
    val total_trips: Int,
    val total_earnings: Double
)

// 2. Interface Endpoint API
interface ApiService {
    // --- Autentikasi Penumpang (Tabel passengers) ---
    @POST("api/auth/passenger/register")
    suspend fun registerPassenger(@Body request: AuthRequest): Response<AuthResponse>

    @POST("api/auth/passenger/login")
    suspend fun loginPassenger(@Body request: AuthRequest): Response<AuthResponse>

    // --- Autentikasi Pengemudi (Tabel drivers) ---
    @POST("api/auth/driver/register")
    suspend fun registerDriver(@Body request: AuthRequest): Response<AuthResponse>

    @POST("api/auth/driver/login")
    suspend fun loginDriver(@Body request: AuthRequest): Response<AuthResponse>

    // --- Pemesanan & Pencocokan Berbasis Gender ---
    @POST("api/rides/book")
    suspend fun bookRide(@Body request: RideRequest): Response<RideResponse>

    @GET("api/rides/searching/{driverNim}")
    suspend fun getSearchingRides(
        @Path("driverNim") driverNim: String,
        @Query("shift") shift: String? = null
    ): Response<List<Ride>>

    @PUT("api/rides/accept/{id}")
    suspend fun acceptRide(
        @Path("id") rideId: Int,
        @Body body: Map<String, String> // Mengirim map("driver_nim" to nim)
    ): Response<RideResponse>

    @PUT("api/rides/reject/{id}")
    suspend fun rejectRide(@Path("id") rideId: Int): Response<RideResponse>

    @PUT("api/rides/complete/{id}")
    suspend fun completeRide(@Path("id") rideId: Int): Response<RideResponse>

    @GET("api/driver/earnings/{nim}")
    suspend fun getDriverEarnings(
        @Path("nim") driverNim: String
    ): Response<EarningsResponse>
}

// 3. Objek Klien Retrofit (Penghubung Utama)
object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:3000/" // Alamat khusus Emulator ke PC lokal

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}