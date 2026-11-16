package com.gas.nebengmu.ui.screens.driver

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
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
fun DriverOrderDetailScreen(
    rideId: Int,
    driverNim: String, // ✅ Tambahkan parameter driverNim di sini agar dikenal
    passengerNim: String = "20230140",
    pickupLocation: String = "Kos Nusa · Tamantirto",
    destinationLocation: String = "Gerbang Kampus UMY",
    driverGender: String = "L",
    rideMode: String = "shared",
    fare: Double = 15000.0,
    onBackClick: () -> Unit,
    onAcceptSuccess: () -> Unit
) {
    val isFemaleDriver = driverGender.equals("P", ignoreCase = true)
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Koordinat simulasi peta
    val kosNusa = LatLng(-7.8130, 110.3110)
    val gerbangUmy = LatLng(-7.8113, 110.3188)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(gerbangUmy, 15f)
    }
    val seat1Name = if (isFemaleDriver) "Nabila" else "Dimas"
    val seat2Name = if (isFemaleDriver) "Dinda" else "Farhan"
    val seat3Name = if (isFemaleDriver) "Salma" else "Rizky"

    val primaryGreen = Color(0xFF006D44)
    val primaryRed = Color(0xFFB71C1C)
    val lightGreenBg = Color(0xFFE8F5E9)

    // ❌ Baris requestBody & response yang sebelumnya di luar (baris 54-55) SUDAH DIHAPUS dari sini

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order baru untukmu", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    TextButton(onClick = {}) {
                        Text("Sif Siang", fontWeight = FontWeight.Bold, color = primaryGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 1. Google Maps dengan Garis Rute (Polyline)
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(zoomControlsEnabled = false, compassEnabled = false)
            ) {
                Marker(state = MarkerState(position = kosNusa), title = "Jemput: Kos Nusa")
                Marker(state = MarkerState(position = gerbangUmy), title = "Tujuan: UMY")
                Polyline(
                    points = listOf(kosNusa, LatLng(-7.8130, 110.3150), gerbangUmy),
                    color = primaryGreen,
                    width = 8f
                )
            }

            // 2. Panel Bawah Sesuai Wireframe 11
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                color = Color.White,
                shadowElevation = 16.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                ) {
                    // Handle Bar
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color.LightGray)
                            .align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Badge Status & Jarak Jemput
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = lightGreenBg,
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(primaryGreen))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Online · Order masuk", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = primaryGreen)
                            }
                        }

                        Surface(
                            color = Color(0xFFFFF8E1),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Jemput 450 m",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF57F17)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Ada Penumpang !", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)

                    Spacer(modifier = Modifier.height(10.dp))

                    // Detail Titik Jemput & Tujuan
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFFBC02D)))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("JEMPUT", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(pickupLocation, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(RoundedCornerShape(2.dp)).background(primaryGreen))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("TUJUAN", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(destinationLocation, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Badge Mode & Jumlah Penumpang
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            color = lightGreenBg,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = if (rideMode == "shared") "Nebeng Bareng" else "Perjalanan Sendiri",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryGreen
                            )
                        }
                        Surface(
                            color = Color(0xFFFFF8E1),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = if (rideMode == "shared") "3 penumpang" else "1 penumpang",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF57F17)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Jadwal Jemput
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Jadwal jemput", fontSize = 12.sp, color = Color.Gray)
                        Text("Hari ini · 12.30", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Kartu Tarif
                    Surface(
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Tarif penuh untukmu", fontSize = 11.sp, color = Color.Gray)
                                Text("3 × Rp5.000 · potongan platform Rp0", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
                            }
                            Text("Rp${fare.toInt()}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = primaryGreen)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Rute searah · 1,8 km perjalanan. Batas putar yang disepakati: +500 m (contoh).",
                        fontSize = 10.sp,
                        color = Color.Gray,
                        lineHeight = 14.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Tombol Aksi: Tolak & Terima Order
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                coroutineScope.launch {
                                    try {
                                        val response = RetrofitClient.instance.rejectRide(rideId)
                                        if (response.isSuccessful) {
                                            Toast.makeText(context, "Order ditolak & dihapus dari daftar", Toast.LENGTH_SHORT).show()
                                            onBackClick()
                                        } else {
                                            Toast.makeText(context, "Gagal menolak order", Toast.LENGTH_SHORT).show()
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Kesalahan Jaringan: ${e.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = primaryRed)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Tolak", fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    try {
                                        val requestBody = mapOf("driver_nim" to driverNim)
                                        val response = RetrofitClient.instance.acceptRide(rideId, requestBody)

                                        if (response.isSuccessful) {
                                            Toast.makeText(context, "Order diterima! Menuju lokasi jemput...", Toast.LENGTH_LONG).show()
                                            onAcceptSuccess()
                                        } else {
                                            Toast.makeText(context, "Gagal menerima order (mungkin sudah diambil driver lain)", Toast.LENGTH_SHORT).show()
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Kesalahan Jaringan: ${e.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                            },
                            modifier = Modifier
                                .weight(1.5f)
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = primaryRed)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Terima order", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}