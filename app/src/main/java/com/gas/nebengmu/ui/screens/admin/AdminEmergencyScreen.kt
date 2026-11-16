package com.gas.nebengmu.ui.screens.admin

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Phone
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminEmergencyScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val primaryGreen = Color(0xFF006D44)
    val primaryRed = Color(0xFFB71C1C)
    val lightRedBg = Color(0xFFFFEBEE)

    // State untuk mensimulasikan apakah darurat sudah ditangani/diselesaikan oleh Satpam
    var alertResolved by remember { mutableStateOf(false) }

    // Koordinat simulasi posisi live location penumpang saat menekan panic button (sekitar Tamantirto)
    val livePassengerLocation = LatLng(-7.8130, 110.3110)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(livePassengerLocation, 16f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pusat Darurat · Satpam UMY", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
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
                .background(Color(0xFFFAFAFA))
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Status Pemantauan
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Peringatan Darurat Aktif", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text("Pantauan langsung sinyal Panic Button penumpang", fontSize = 12.sp, color = Color.Gray)
                }
                Badge(
                    containerColor = if (!alertResolved) primaryRed else primaryGreen
                ) {
                    Text(
                        text = if (!alertResolved) "1 Darurat" else "Aman",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (!alertResolved) {
                // Kartu Daftar Peringatan Darurat dengan Live Map
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    shadowElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Baris Judul Peringatan & Status Live
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(lightRedBg),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Warning, contentDescription = null, tint = primaryRed, modifier = Modifier.size(20.dp))
                                }
                                Column {
                                    Text("PANIC BUTTON DITEKAN", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = primaryRed)
                                    Text("12.27 WIB · Sinyal GPS Aktif", fontSize = 11.sp, color = Color.Gray)
                                }
                            }

                            // Badge Indikator Live Tracking Berkedip / Aktif
                            Surface(
                                color = lightRedBg,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "🔴 LIVE GPS",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = primaryRed
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // 🌟 1. MINI GOOGLE MAPS UNTUK LIVE LOCATION PENUMPANG
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(12.dp))
                        ) {
                            GoogleMap(
                                modifier = Modifier.fillMaxSize(),
                                cameraPositionState = cameraPositionState,
                                uiSettings = MapUiSettings(
                                    zoomControlsEnabled = false,
                                    compassEnabled = false,
                                    scrollGesturesEnabled = false, // Dikunci agar fokus ke lokasi darurat
                                    zoomGesturesEnabled = true
                                )
                            ) {
                                // Marker posisi live penumpang saat darurat
                                Marker(
                                    state = MarkerState(position = livePassengerLocation),
                                    title = "Lokasi Darurat: Nabila",
                                    snippet = "Kos Nusa, Tamantirto (Bergerak ke UMY)"
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Divider(color = Color(0xFFEEEEEE))
                        Spacer(modifier = Modifier.height(12.dp))

                        // Detail Pelapor & Perjalanan
                        Text("Detail Penumpang:", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Text("Nabila (NIM: 20230140)", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("📍 Titik Terkini: Kos Nusa, Tamantirto (Dekat PKU Gamping)", fontSize = 12.sp, color = Color.DarkGray)
                        Text("🚗 Pengemudi: Rizky Aditya (Avanza Hitam · AB 1284 YK)", fontSize = 12.sp, color = Color.DarkGray)

                        Spacer(modifier = Modifier.height(16.dp))

                        // Tombol Aksi Admin / Satpam
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    Toast.makeText(context, "Menghubungkan panggilan darurat ke penumpang...", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(45.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = primaryRed)
                            ) {
                                Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Telepon", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = {
                                    alertResolved = true
                                    Toast.makeText(context, "Status darurat ditandai selesai/aman oleh Satpam.", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(45.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = primaryGreen)
                            ) {
                                Text("Tandai Aman", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else {
                // Tampilan jika peringatan sudah diselesaikan
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFE8F5E9),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = primaryGreen, modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Semua Aman", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = primaryGreen)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Tidak ada peringatan darurat aktif saat ini.", fontSize = 12.sp, color = Color.DarkGray)
                    }
                }
            }
        }
    }
}