package com.gas.nebengmu.ui.screens.passenger

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SportsMotorsports
import androidx.compose.material.icons.filled.Warning
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
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverMatchingScreen(
    rideMode: String = "shared",
    userGender: String = "L",
    onCancelBooking: () -> Unit,
    onTripCompleted: () -> Unit,
    onEmergencyClick: () -> Unit
) {
    val context = LocalContext.current
    val primaryGreen = Color(0xFF006D44)
    val lightGreenBg = Color(0xFFE8F5E9)
    val primaryRed = Color(0xFFB71C1C)

    val isShared = rideMode.equals("shared", ignoreCase = true)
    val isFemale = userGender.equals("P", ignoreCase = true)

    // Data statis pengemudi & penumpang disesuaikan dengan gender akun yang login
    val matchedDriverName = if (isFemale) "Aisyah Rahma" else "Rizky Aditya"
    val matchedVehicle = if (isFemale) "Brio putih · AB 1742 UY" else "Avanza hitam · AB 1284 YK"
    val genderBadgeText = if (isFemale) "Sesama Perempuan" else "Sesama Laki-laki"
    val sharedManifestText = if (isFemale) {
        "Kamu, Dinda, Salma · 3 penumpang"
    } else {
        "Kamu, Dimas, Farhan · 3 penumpang"
    }

    // Simulasi koordinat peta sekitar UMY
    val kosNusa = LatLng(-7.8130, 110.3110)
    val gerbangUmy = LatLng(-7.8113, 110.3188)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(gerbangUmy, 15f)
    }

    var isMatched by remember { mutableStateOf(false) }

    // Simulasi otomatis 'ketemu driver' setelah 3 detik untuk keperluan demo
    LaunchedEffect(Unit) {
        delay(3000)
        isMatched = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when {
                            isMatched -> "Pengemudi menuju kamu"
                            isShared -> "Mencari Pengemudi Searah..."
                            else -> "Mencari Pengemudi..."
                        },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
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
            // 1. Peta Latar Belakang
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(zoomControlsEnabled = false, compassEnabled = false)
            ) {
                Marker(state = MarkerState(position = kosNusa), title = "Titik Jemput: Kos Nusa")
                Marker(state = MarkerState(position = gerbangUmy), title = "Tujuan: Kampus UMY")
                Polyline(
                    points = listOf(kosNusa, LatLng(-7.8130, 110.3150), gerbangUmy),
                    color = primaryGreen,
                    width = 8f
                )
            }

            // 2. Panel Bawah Lengkap
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
                    // Handle bar kecil di atas panel
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color.LightGray)
                            .align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    if (!isMatched) {
                        // Status Sedang Mencari
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            CircularProgressIndicator(
                                color = primaryGreen,
                                modifier = Modifier.size(28.dp),
                                strokeWidth = 3.dp
                            )
                            Column {
                                Text("Menghubungkan ke Mitra UMY...", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text(
                                    text = if (isShared) "Mencocokkan rute searah ($genderBadgeText)..." else "Mencari pengemudi terdekat ($genderBadgeText)...",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    } else {
                        // Estimasi Waktu Tiba & Badge "Menuju jemput"
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Tiba ±3 menit", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("Jemput 12.30 · sampai sekitar 12.38", fontSize = 12.sp, color = Color.Gray)
                            }

                            Surface(
                                color = lightGreenBg,
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.NearMe, contentDescription = null, tint = primaryGreen, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Menuju jemput",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = primaryGreen
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Detail Profil Driver & Kendaraan (Dinamis sesuai gender)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(lightGreenBg),
                                contentAlignment = Alignment.Center
                            ) {
                                if(isShared) Icon (Icons.Default.DirectionsCar, contentDescription = null, tint = primaryGreen, modifier = Modifier.size(24.dp))
                                else Icon (Icons.Default.SportsMotorsports, contentDescription = null, tint = primaryGreen, modifier = Modifier.size(24.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(matchedDriverName, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                                Text(
                                    text = "Mahasiswa UMY terverifikasi · $genderBadgeText",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = primaryGreen
                                )
                                Text(matchedVehicle, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Kartu Ringkasan Manifest Penumpang (Dinamis sesuai rideMode & gender)
                        Surface(
                            color = lightGreenBg,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isShared) sharedManifestText else "Sendirian · 1 penumpang",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.DarkGray
                                )
                                Text(
                                    text = if (isShared) "Rp5.000" else "Rp15.000",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = primaryGreen
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Titik Jemput & Tujuan
                        Column(modifier = Modifier.padding(vertical = 2.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFFBC02D)))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Kos Nusa · Tamantirto", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(8.dp).clip(RoundedCornerShape(2.dp)).background(primaryGreen))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Gerbang Kampus UMY", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Tombol Sekunder: Bagikan Perjalanan
                        OutlinedButton(
                            onClick = {
                                Toast.makeText(context, "Link pelacakan perjalanan disalin ke clipboard.", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = primaryGreen)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Bagikan perjalanan", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // --- PANIC BUTTON (Tombol Darurat Utama) ---
                        Button(
                            onClick = onEmergencyClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = primaryRed)
                        ) {
                            Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Panic Button · Kirim lokasi", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Satu ketuk → Satpam UMY & kontak terpercaya",
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            fontSize = 10.sp,
                            color = primaryRed,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Tombol Simulasi Selesai
                        Button(
                            onClick = onTripCompleted,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(42.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                        ) {
                            Text("Selesaikan Simulasi Perjalanan", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Berbagi lokasi real-time dirancang untuk darurat. Simulasi: pelacakan & pengiriman tidak aktif.",
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            fontSize = 9.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}