package com.gas.nebengmu

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gas.nebengmu.data.AuthRequest
import com.gas.nebengmu.data.RetrofitClient
import com.gas.nebengmu.data.RideRequest
import com.gas.nebengmu.ui.screens.admin.AdminEmergencyScreen
import com.gas.nebengmu.ui.screens.auth.LoginScreen
import com.gas.nebengmu.ui.screens.auth.RegisterDriverScreen
import com.gas.nebengmu.ui.screens.auth.RegisterPassengerScreen
import com.gas.nebengmu.ui.screens.auth.WelcomeScreen
import com.gas.nebengmu.ui.screens.driver.DriverActiveNavigationScreen
import com.gas.nebengmu.ui.screens.driver.DriverDashboardScreen
import com.gas.nebengmu.ui.screens.driver.DriverOrderDetailScreen
import com.gas.nebengmu.ui.screens.passenger.DriverMatchingScreen
import com.gas.nebengmu.ui.screens.passenger.EmergencyHelpScreen
import com.gas.nebengmu.ui.screens.passenger.HomeMapScreen
import com.gas.nebengmu.ui.screens.passenger.RideSelectionScreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NebengMuApp()
                }
            }
        }
    }
}

@Composable
fun NebengMuApp() {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val sessionPrefs = remember { context.getSharedPreferences("nebengmu_session", android.content.Context.MODE_PRIVATE) }
    var loggedInName by remember { mutableStateOf(sessionPrefs.getString("name", "Pengguna") ?: "Pengguna") }
    var loggedInNim by remember { mutableStateOf(sessionPrefs.getString("nim", "") ?: "") }
    var loggedInGender by remember { mutableStateOf(sessionPrefs.getString("gender", "L") ?: "L") }

    NavHost(navController = navController, startDestination = "welcome") {

        composable("welcome") {
            WelcomeScreen(
                onNavigateToAdmin = {
                    navController.navigate("admin_emergency")
                },
                onStartJourneyClick = { navController.navigate("login") }
            )
        }

        composable("login") {
            LoginScreen(
                onLoginClick = { email, password, role ->
                    coroutineScope.launch {
                        try {
                            val request = AuthRequest(
                                email = email,
                                password = password
                            )

                            val isDriver = role.equals("Pengemudi", ignoreCase = true) ||
                                    role.equals("driver", ignoreCase = true)
                            val response = if (isDriver) {
                                RetrofitClient.instance.loginDriver(request)
                            } else {
                                RetrofitClient.instance.loginPassenger(request)
                            }

                            if (response.isSuccessful && response.body() != null) {
                                val authData = response.body()!!
                                loggedInName = authData.name ?: ""
                                loggedInNim = authData.nim ?: ""
                                loggedInGender = authData.gender ?: "L"

                                sessionPrefs.edit()
                                    .putString("name", loggedInName)
                                    .putString("nim", loggedInNim)
                                    .putString("gender", loggedInGender)
                                    .apply()

                                Toast.makeText(context, authData.message, Toast.LENGTH_SHORT).show()

                                if (isDriver) {
                                    navController.navigate("driver_dashboard") {
                                        popUpTo("welcome") { inclusive = false }
                                    }
                                } else {
                                    // ✅ Diarahkan ke home_map (rute utama penumpang yang terdaftar)
                                    navController.navigate("home_map") {
                                        popUpTo("welcome") { inclusive = false }
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Login gagal! Periksa akun dan peran yang dipilih.", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Kesalahan Jaringan: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onNavigateToRegister = { targetRole ->
                    // ✅ Menggunakan underscore (_) agar cocok dengan composable("register_driver") & ("register_passenger")
                    if (targetRole == "driver") {
                        navController.navigate("register_driver")
                    } else {
                        navController.navigate("register_passenger")
                    }
                }
            )
        }

        composable("register_passenger") {
            RegisterPassengerScreen(
                onRegisterSubmit = { nama, nim, email, password, gender ->
                    coroutineScope.launch {
                        try {
                            val request = AuthRequest(
                                nim = nim,
                                nama_lengkap = nama,
                                email = email,
                                password = password,
                                gender = gender
                            )
                            val response = RetrofitClient.instance.registerPassenger(request)
                            if (response.isSuccessful) {
                                Toast.makeText(context, "Registrasi Penumpang Berhasil! Silakan login.", Toast.LENGTH_LONG).show()
                                navController.navigate("login") {
                                    popUpTo("register_passenger") { inclusive = true }
                                }
                            } else {
                                Toast.makeText(context, "Gagal mendaftar: ${response.message()}", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Kesalahan Jaringan: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("register_driver") {
            RegisterDriverScreen(
                onRegisterSubmit = { nama, nim, email, password, kendaraan, plat, gender ->
                    coroutineScope.launch {
                        try {
                            val request = AuthRequest(
                                nim = nim,
                                nama_lengkap = nama,
                                email = email,
                                password = password,
                                gender = gender,
                                jenis_kendaraan = kendaraan,
                                no_plat = plat
                            )
                            val response = RetrofitClient.instance.registerDriver(request)
                            if (response.isSuccessful) {
                                Toast.makeText(context, "Registrasi Pengemudi Berhasil! Silakan login.", Toast.LENGTH_LONG).show()
                                navController.navigate("login") {
                                    popUpTo("register_driver") { inclusive = true }
                                }
                            } else {
                                Toast.makeText(context, "Gagal mendaftar: ${response.message()}", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Kesalahan Jaringan: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("home_map") {
            HomeMapScreen(
                userName = loggedInName,
                onSearchRideClick = {
                    navController.navigate("ride_selection")
                },
                onAccountMenuClick = {
                    Toast.makeText(context, "Membuka Menu Akun", Toast.LENGTH_SHORT).show()
                }
            )
        }

        composable("ride_selection") {
            RideSelectionScreen(
                userGender = loggedInGender, // ✅ Tambahkan parameter gender penumpang
                onBackClick = { navController.popBackStack() },
                onContinueClick = { selectedMode ->
                    coroutineScope.launch {
                        try {
                            val requestData = RideRequest(
                                passenger_nim = loggedInNim.ifEmpty { "123" },
                                pickup_location = "Kos Nusa · Tamantirto",
                                destination_location = "Gerbang Kampus UMY",
                                ride_mode = selectedMode,
                                fare = if (selectedMode == "shared") 5000.0 else 15000.0
                            )
                            val response = RetrofitClient.instance.bookRide(requestData)
                            if (response.isSuccessful) {
                                Toast.makeText(context, "Pesanan dibuat! Mencari pengemudi...", Toast.LENGTH_SHORT).show()
                                navController.navigate("driver_matching/$selectedMode")
                            } else {
                                Toast.makeText(context, "Gagal membuat pesanan", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Kesalahan Jaringan: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            )
        }

        composable("driver_matching/{rideMode}") { backStackEntry ->
            val rideMode = backStackEntry.arguments?.getString("rideMode") ?: "shared"
            DriverMatchingScreen(
                rideMode = rideMode,
                userGender = loggedInGender,
                onCancelBooking = { navController.popBackStack() },
                onTripCompleted = {
                    navController.popBackStack("home_map", inclusive = false)
                },
                onEmergencyClick = {
                    // ✅ Teruskan rideMode ke layar bantuan darurat
                    navController.navigate("emergency_help/$rideMode")
                }
            )
        }

        composable("driver_dashboard") {
            DriverDashboardScreen(
                driverName = loggedInName,
                driverNim = loggedInNim,
                onLogoutClick = {
                    navController.navigate("login") {
                        popUpTo("driver_dashboard") { inclusive = true }
                    }
                },
                onOrderClick = { rideId ->
                    navController.navigate("driver_order_detail/$rideId/$loggedInNim")
                }
            )
        }

        composable("driver_order_detail/{rideId}/{driverNim}") { backStackEntry ->
            val rideId = backStackEntry.arguments?.getString("rideId")?.toIntOrNull() ?: 0
            val driverNim = backStackEntry.arguments?.getString("driverNim") ?: ""

            DriverOrderDetailScreen(
                rideId = rideId,
                driverNim = driverNim,
                driverGender = loggedInGender, // ✅ Tambahkan parameter gender pengemudi
                onBackClick = { navController.popBackStack() },
                onAcceptSuccess = {
                    navController.navigate("driver_active_nav/$rideId/$driverNim/15000.0") {
                        popUpTo("driver_dashboard")
                    }
                }
            )
        }

        composable("driver_active_nav/{rideId}/{driverNim}/{fare}") { backStackEntry ->
            val rideId = backStackEntry.arguments?.getString("rideId")?.toIntOrNull() ?: 0
            val driverNim = backStackEntry.arguments?.getString("driverNim") ?: ""
            val fare = backStackEntry.arguments?.getString("fare")?.toDoubleOrNull() ?: 15000.0

            DriverActiveNavigationScreen(
                rideId = rideId,
                driverNim = driverNim,
                driverGender = loggedInGender, // ✅ Teruskan gender pengemudi yang sedang login
                activeFare = fare,
                destinationName = "Gerbang Kampus UMY",
                onTripCompleted = {
                    navController.navigate("driver_dashboard") {
                        popUpTo("driver_dashboard") { inclusive = true }
                    }
                }
            )
        }

        composable("emergency_help/{rideMode}") { backStackEntry ->
            val rideMode = backStackEntry.arguments?.getString("rideMode") ?: "shared"
            EmergencyHelpScreen(
                userGender = loggedInGender, // ✅ Sinkron dengan gender akun login
                rideMode = rideMode,         // ✅ Sinkron dengan moda Motor / Mobil
                onBackClick = { navController.popBackStack() },
                onTripCompleted = {
                    navController.navigate("home_map") {
                        popUpTo("home_map") { inclusive = true }
                    }
                }
            )
        }

        composable("admin_emergency") {
            AdminEmergencyScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}