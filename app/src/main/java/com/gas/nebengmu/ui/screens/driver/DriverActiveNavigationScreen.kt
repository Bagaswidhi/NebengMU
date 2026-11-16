package com.gas.nebengmu.ui.screens.driver

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.EventSeat
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gas.nebengmu.data.RetrofitClient
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverActiveNavigationScreen(
    driverGender: String = "L",
    rideId: Int,
    driverNim: String,
    activeFare: Double = 15000.0, // Tarif dari order aktif saat ini
    destinationName: String = "Gerbang UMY",
    onTripCompleted: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    val isFemaleDriver = driverGender.equals("P", ignoreCase = true)
    val manifestPassengers = if (isFemaleDriver) {
        listOf("Nabila", "Dinda", "Salma")
    } else {
        listOf("Dimas", "Farhan", "Rizky")
    }

    // State data dinamis dari database
    var completedTrips by remember { mutableStateOf(0) }
    var completedEarnings by remember { mutableStateOf(0.0) }

    // Ambil rekap pendapatan yang sudah selesai dari database saat halaman dimuat
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val response = RetrofitClient.instance.getDriverEarnings(driverNim)
                if (response.isSuccessful && response.body() != null) {
                    completedTrips = response.body()!!.total_trips
                    completedEarnings = response.body()!!.total_earnings
                }
            } catch (e: Exception) {
                // Gunakan nilai default jika gagal
            }
        }
    }

    val kosNusa = LatLng(-7.8130, 110.3110)
    val gerbangUmy = LatLng(-7.8113, 110.3188)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(gerbangUmy, 15.5f)
    }

    val primaryGreen = Color(0xFF006D44)
    val darkCardGreen = Color(0xFF004D2C)
    val primaryRed = Color(0xFFB71C1C)
    val lightGreenBg = Color(0xFFE8F5E9)

    // Perhitungan total bersih termasuk trip aktif
    val totalBersihDiterima = completedEarnings + activeFare
    val totalPerjalananSetelahSelesai = completedTrips + 1

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Menuju $destinationName", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            // 1. Banner Navigasi
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = darkCardGreen
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.NearMe,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text("Belok kanan 200 m", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Ke arah Gerbang Kampus UMY", fontSize = 12.sp, color = Color(0xCCFFFFFF))
                    }
                }
            }

            // 2. Google Maps Kompak
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    uiSettings = MapUiSettings(zoomControlsEnabled = false, compassEnabled = false)
                ) {
                    Marker(state = MarkerState(position = kosNusa), title = "Jemput")
                    Marker(state = MarkerState(position = gerbangUmy), title = "Tujuan")
                    Polyline(
                        points = listOf(kosNusa, LatLng(-7.8130, 110.3150), gerbangUmy),
                        color = primaryGreen,
                        width = 8f
                    )
                }
            }

            // 3. Panel Informasi Bawah
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .background(Color.White)
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("4 menit · 1,2 km", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Surface(
                        color = lightGreenBg,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Rute tercepat",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Tambahan rute: 200 / 500 m", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
                    Text("Dalam batas", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = primaryGreen)
                }

                Spacer(modifier = Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = { 0.4f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = primaryGreen,
                    trackColor = Color(0xFFE0E0E0),
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text("Batas +500 m disepakati · angka ilustratif, bukan aturan resmi.", fontSize = 10.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(16.dp))

                // 🌟 Kartu Visualisasi Kursi Kosong (Seat Matrix) untuk Driver
                SeatMatrixCard(
                    totalSeats = 3,
                    bookedPassengers = manifestPassengers
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Rekap Pendapatan Dinamis
                Text("Rekap hari ini", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("$completedTrips perjalanan selesai", fontSize = 12.sp, color = Color.Gray)
                        Text("Rp${completedEarnings.toInt()}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Potongan platform", fontSize = 12.sp, color = Color.Gray)
                        Text("Rp0 (0%)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = primaryGreen)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Divider(color = Color(0xFFEEEEEE))
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Bersih diterima (Termasuk trip ini)", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                    Text("Rp${totalBersihDiterima.toInt()}", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = primaryGreen)
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text("Trip aktif Rp${activeFare.toInt()} dihitung. Setelah selesai: $totalPerjalananSetelahSelesai perjalanan · Rp${totalBersihDiterima.toInt()} bersih.", fontSize = 10.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(20.dp))

                // Tombol Selesaikan Saat Tiba
                Button(
                    onClick = {
                        coroutineScope.launch {
                            try {
                                val response = RetrofitClient.instance.completeRide(rideId)
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "Perjalanan Selesai! Pendapatan ditambahkan.", Toast.LENGTH_LONG).show()
                                    onTripCompleted()
                                } else {
                                    Toast.makeText(context, "Gagal menyelesaikan trip", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Kesalahan Jaringan: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryRed)
                ) {
                    Text("Selesaikan saat tiba", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/**
 * Komponen Seat Matrix untuk DriverActiveNavigationScreen
 */
@Composable
fun SeatMatrixCard(
    totalSeats: Int = 3,
    bookedPassengers: List<String> = listOf("Nabila", "Dinda", "Farhan"),
    modifier: Modifier = Modifier
) {
    val primaryGreen = Color(0xFF006D44)
    val lightGreenBg = Color(0xFFE8F5E9)

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        border = BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.EventSeat,
                        contentDescription = null,
                        tint = primaryGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Manifest Kursi Belakang",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                val availableSeats = totalSeats - bookedPassengers.size
                Surface(
                    color = if (availableSeats > 0) lightGreenBg else Color(0xFFFFEBEE),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (availableSeats > 0) "$availableSeats Kursi Kosong" else "Penuh",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (availableSeats > 0) primaryGreen else Color(0xFFB71C1C)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                for (i in 0 until totalSeats) {
                    val isBooked = i < bookedPassengers.size
                    val passengerName = if (isBooked) bookedPassengers[i] else "Kosong"

                    SeatSlotItem(
                        seatNumber = "Kursi ${i + 1}",
                        occupantName = passengerName,
                        isBooked = isBooked,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun SeatSlotItem(
    seatNumber: String,
    occupantName: String,
    isBooked: Boolean,
    modifier: Modifier = Modifier
) {
    val primaryGreen = Color(0xFF006D44)
    val lightGreenBg = Color(0xFFE8F5E9)

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = if (isBooked) lightGreenBg else Color(0xFFFAFAFA),
        border = BorderStroke(
            width = 1.dp,
            color = if (isBooked) primaryGreen.copy(alpha = 0.3f) else Color(0xFFE0E0E0)
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(if (isBooked) primaryGreen else Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = if (isBooked) Color.White else Color.Gray,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = seatNumber,
                fontSize = 10.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = occupantName,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isBooked) primaryGreen else Color(0xFF757575),
                maxLines = 1
            )
        }
    }
}