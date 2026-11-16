package com.gas.nebengmu.ui.screens.passenger

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gas.nebengmu.data.SeatMatrixCard
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RideSelectionScreen(
    userGender: String = "L",
    onBackClick: () -> Unit,
    onContinueClick: (selectedMode: String) -> Unit
) {
    val kosNusa = LatLng(-7.8130, 110.3110)
    val gerbangUmy = LatLng(-7.8113, 110.3188)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(gerbangUmy, 15f)
    }

    val primaryGreen = Color(0xFF006D44)
    val lightGreenBg = Color(0xFFE8F5E9)
    val primaryRed = Color(0xFFB71C1C)

    val isFemale = userGender.equals("P", ignoreCase = true)
    val dummyPassengers = if (isFemale) {
        listOf("Nabila", "Dinda")
    } else {
        listOf("Dimas", "Farhan")
    }

    // State pilihan moda ("solo" = Nebeng Motor, "shared" = Nebeng Bareng Mobil)
    var selectedMode by remember { mutableStateOf("shared") }

    // State pengaman untuk mencegah klik ganda (double click)
    var isSubmitting by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pilih perjalanan", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
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
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(zoomControlsEnabled = false, compassEnabled = false)
            ) {
                Marker(
                    state = MarkerState(position = kosNusa),
                    title = "Kos Nusa",
                    snippet = "Titik Penjemputan"
                )
                Marker(
                    state = MarkerState(position = gerbangUmy),
                    title = "Gerbang Kampus UMY",
                    snippet = "Tujuan Perjalanan"
                )
                Polyline(
                    points = listOf(kosNusa, LatLng(-7.8130, 110.3150), gerbangUmy),
                    color = primaryGreen,
                    width = 8f
                )
            }

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
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color.LightGray)
                            .align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Berangkat caramu", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Surface(
                            color = Color(0xFFFFF8E1),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "1,8 km",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF57F17)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFFBC02D)))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Kos Nusa · Tamantirto", fontSize = 13.sp, color = Color.DarkGray, fontWeight = FontWeight.Medium)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(RoundedCornerShape(2.dp)).background(primaryGreen))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Gerbang Kampus UMY", fontSize = 13.sp, color = Color.DarkGray, fontWeight = FontWeight.Medium)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // --- OPSI 1: Nebeng / Perjalanan Sendiri (Khusus Sepeda Motor) ---
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = if (selectedMode == "solo") 2.dp else 1.dp,
                                color = if (selectedMode == "solo") primaryGreen else Color(0xFFE0E0E0),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable { selectedMode = "solo" },
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(lightGreenBg),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.TwoWheeler, contentDescription = "Sepeda Motor", tint = primaryGreen)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("Nebeng", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(color = lightGreenBg, shape = RoundedCornerShape(6.dp)) {
                                            Text(
                                                text = "Motor",
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = primaryGreen
                                            )
                                        }
                                    }
                                    Text("Sepeda Motor · 1 orang", fontSize = 12.sp, color = Color.Gray)
                                }
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Rp15.000", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                                Text("/orang", fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // --- OPSI 2: Nebeng Bareng (Khusus Mobil) ---
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = if (selectedMode == "shared") 2.dp else 1.dp,
                                color = if (selectedMode == "shared") primaryGreen else Color(0xFFE0E0E0),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable { selectedMode = "shared" },
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(lightGreenBg),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.DirectionsCar, contentDescription = "Mobil", tint = primaryGreen)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("Nebeng Bareng", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(color = Color(0xFFFFF3E0), shape = RoundedCornerShape(6.dp)) {
                                            Text(
                                                text = "Mobil",
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFE65100)
                                            )
                                        }
                                    }
                                    Text("Mobil · 3 orang · searah & sewaktu", fontSize = 12.sp, color = Color.Gray)
                                }
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Rp5.000", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = primaryGreen)
                                Text("/orang", fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Formasi kursi hanya muncul untuk Nebeng Bareng (Mobil)
                    if (selectedMode == "shared") {
                        SeatMatrixCard(
                            totalSeats = 3,
                            bookedPassengers = dummyPassengers
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    Surface(
                        color = Color(0xFFFFFDE7),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (selectedMode == "shared") {
                                val genderInfo = if (isFemale) "sesama perempuan" else "sesama laki-laki"
                                "Armada Mobil · Tarif dibagi rata untuk 3 penumpang ($genderInfo) dalam satu mobil."
                            } else {
                                "Armada Sepeda Motor · Perjalanan pribadi untuk 1 penumpang langsung menuju titik tujuan."
                            },
                            modifier = Modifier.padding(12.dp),
                            fontSize = 11.sp,
                            color = Color(0xFF795548),
                            lineHeight = 15.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tombol Lanjut dengan Proteksi isSubmitting (Anti Double Click)
                    Button(
                        onClick = {
                            if (!isSubmitting) {
                                isSubmitting = true
                                onContinueClick(selectedMode)
                            }
                        },
                        enabled = !isSubmitting,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryRed,
                            disabledContainerColor = primaryRed.copy(alpha = 0.6f)
                        )
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                        } else {
                            Text(
                                text = if (selectedMode == "shared") "Lanjut · Nebeng Bareng (Mobil)" else "Lanjut · Nebeng (Motor)",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Rincian biaya ditampilkan sebelum konfirmasi.",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}