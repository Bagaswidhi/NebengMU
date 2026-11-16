package com.gas.nebengmu.ui.screens.passenger

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

fun isWithinGeofenceRadius(point: LatLng, center: LatLng, radiusInMeters: Double): Boolean {
    val results = FloatArray(1)
    Location.distanceBetween(
        point.latitude, point.longitude,
        center.latitude, center.longitude,
        results
    )
    return results[0] <= radiusInMeters
}

// Fungsi bantu untuk mengambil inisial nama pengguna (Contoh: "Bagas Satya" -> "BS")
fun getInitials(name: String): String {
    val parts = name.trim().split(" ")
    return if (parts.size >= 2) {
        "${parts[0].take(1)}${parts[1].take(1)}".uppercase()
    } else if (parts.isNotEmpty()) {
        parts[0].take(2).uppercase()
    } else {
        "UM"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeMapScreen(
    userName: String, // Dinamis dari akun login
    onPickupClick: () -> Unit = {},
    onDestinationClick: () -> Unit = {},
    onAccountMenuClick: () -> Unit = {},
    onSearchRideClick: () -> Unit = {}
) {
    val context = LocalContext.current

    val umyCampus = LatLng(-7.8113, 110.3188)
    val geofenceRadiusMeters = 2000.0 // Radius 2 KM (Tamantirto & Gamping)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(umyCampus, 15f)
    }

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    val primaryGreen = Color(0xFF006D44)
    val primaryRed = Color(0xFFB71C1C)
    val lightGreenBg = Color(0xFFE8F5E9)

    // State pilihan lokasi (bisa diubah interaktif nanti)
    var pickupLocation by remember { mutableStateOf("Kos Nusa · Tamantirto") }
    var destinationLocation by remember { mutableStateOf("Gerbang Kampus UMY") }

    Scaffold(
        bottomBar = {
            // Bilah Navigasi Bawah disederhanakan menjadi 2 Menu: Beranda & Akun
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { /* Sudah di Beranda */ },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Beranda") },
                    label = { Text("Beranda", fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = primaryGreen, selectedTextColor = primaryGreen)
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onAccountMenuClick,
                    icon = { Icon(Icons.Default.Person, contentDescription = "Akun") },
                    label = { Text("Akun") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = primaryGreen, selectedTextColor = primaryGreen)
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 1. Google Maps sebagai Background
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = hasLocationPermission),
                uiSettings = MapUiSettings(zoomControlsEnabled = false, compassEnabled = true)
            ) {
                Marker(
                    state = MarkerState(position = umyCampus),
                    title = "Kampus UMY",
                    snippet = "Pusat Zona Layanan"
                )
                Circle(
                    center = umyCampus,
                    radius = geofenceRadiusMeters,
                    strokeColor = primaryGreen,
                    strokeWidth = 4f,
                    fillColor = primaryGreen.copy(alpha = 0.1f)
                )
            }

            // 2. Header Atas: Sapaan Dinamis & Profil Inisial Otomatis
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .align(Alignment.TopCenter)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Halo, $userName", fontSize = 13.sp, color = Color.Gray)
                        Text("Mau ke mana hari ini?", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                    // Avatar Inisial Dinamis (Mengambil huruf depan dari nama pengguna)
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0F2F1))
                            .clickable { onAccountMenuClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = getInitials(userName),
                            fontWeight = FontWeight.Bold,
                            color = primaryGreen,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // 3. Panel Bawah (Bottom Sheet Card)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                color = Color.White,
                shadowElevation = 12.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                ) {
                    // Garis Indikator Tarik
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color.LightGray)
                            .align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Lokasi Terdeteksi Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.MyLocation, contentDescription = null, tint = primaryGreen, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Lokasi terdeteksi · $pickupLocation", fontSize = 12.sp, color = Color.DarkGray, fontWeight = FontWeight.Medium)
                        }
                        IconButton(onClick = {
                            Toast.makeText(context, "Memperbarui lokasi GPS...", Toast.LENGTH_SHORT).show()
                        }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Card Titik Jemput & Tujuan (Dapat Diketuk untuk Memilih Titik Lain)
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(14.dp)),
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            // Kolom Jemput (Bisa diklik untuk ubah lokasi jemput)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onPickupClick() }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFFFBC02D)))
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("JEMPUT (Ketuk untuk ubah)", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                    Text(pickupLocation, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                                }
                                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Divider(color = Color(0xFFF0F0F0), thickness = 1.dp)
                            Spacer(modifier = Modifier.height(8.dp))

                            // Kolom Tujuan (Bisa diklik untuk pilih tujuan)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onDestinationClick() }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(2.dp)).background(primaryGreen))
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("TUJUAN (Ketuk untuk ubah)", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                    Text(destinationLocation, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                                }
                                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Status Badge Area Layanan
                    Surface(
                        color = lightGreenBg,
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = primaryGreen, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Jemput & tujuan di area layanan (Radius 2 KM)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = primaryGreen)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Peringatan / Info Khusus Area UMY
                    Surface(
                        color = Color(0xFFFFFDE7),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = Color(0xFFF57F17), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Perjalanan khusus area UMY", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Black)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    "Kedua titik wajib di dalam radius layanan UMY & Tamantirto/Gamping.",
                                    fontSize = 11.sp,
                                    color = Color.DarkGray,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tombol Cari Perjalanan
                    Button(
                        onClick = {
                            val currentPosition = cameraPositionState.position.target
                            val isInsideRadius = isWithinGeofenceRadius(currentPosition, umyCampus, geofenceRadiusMeters)

                            if (isInsideRadius) {
                                Toast.makeText(context, "Mencari perjalanan di area UMY...", Toast.LENGTH_SHORT).show()
                                onSearchRideClick()
                            } else {
                                Toast.makeText(context, "Titik penjemputan di luar radius layanan UMY!", Toast.LENGTH_LONG).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryRed)
                    ) {
                        Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cari perjalanan", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}