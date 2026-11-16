package com.gas.nebengmu.ui.screens.passenger

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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyHelpScreen(
    userGender: String = "L",
    rideMode: String = "shared",
    onBackClick: () -> Unit,
    onTripCompleted: () -> Unit
) {
    val context = LocalContext.current
    val primaryGreen = Color(0xFF006D44)
    val lightGreenBg = Color(0xFFE8F5E9)
    val primaryRed = Color(0xFFB71C1C)
    val lightAmberBg = Color(0xFFFFF8E1)
    val amberText = Color(0xFFE65100)

    val isFemale = userGender.equals("P", ignoreCase = true)
    val isShared = rideMode.equals("shared", ignoreCase = true)

    // Data pengemudi, kendaraan, dan penumpang menyesuaikan gender & moda perjalanan
    val matchedDriverName = if (isFemale) "Aisyah Rahma" else "Rizky Aditya"
    val genderBadgeText = if (isFemale) "Sesama Perempuan" else "Sesama Laki-laki"
    val matchedVehicle = when {
        isShared && isFemale -> "Brio putih · AB 1742 UY"
        isShared && !isFemale -> "Avanza hitam · AB 1284 YK"
        !isShared && isFemale -> "Vario putih · AB 4312 UY"
        else -> "Beat hitam · AB 5821 YK"
    }
    val manifestText = when {
        isShared && isFemale -> "Kamu, Dinda, Salma · 3 penumpang"
        isShared && !isFemale -> "Kamu, Dimas, Farhan · 3 penumpang"
        else -> "Perjalanan Sendiri · 1 penumpang"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Bantuan darurat",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Header Status Permintaan Terkirim
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(lightGreenBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = primaryGreen,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Permintaan bantuan terkirim",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Permintaanmu sudah dikirim ke Satpam UMY dan kontak tepercaya. Tetap tenang, ya.",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        lineHeight = 17.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Kartu 1: Status Lokasi Dibagikan
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFE0E0E0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = primaryGreen,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Lokasi dibagikan",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Ke Satpam UMY & kontak tepercaya",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Terkirim 12.27 WIB · pembaruan 12.27 WIB",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        color = lightAmberBg,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Menunggu tanggapan Satpam",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = amberText
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Belum ada konfirmasi dari Satpam.",
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Kartu 2: Detail Lokasi & Identitas Pengemudi/Penumpang Sesuai Gender
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFE0E0E0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Lokasi kamu · Kos Nusa, Tamantirto",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFBC02D))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Kos Nusa · Tamantirto", fontSize = 12.sp, color = Color.DarkGray)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(primaryGreen)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Gerbang Kampus UMY", fontSize = 12.sp, color = Color.DarkGray)
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 14.dp),
                        color = Color(0xFFEEEEEE)
                    )

                    Text(
                        text = matchedDriverName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Mahasiswa UMY terverifikasi · $genderBadgeText",
                        fontSize = 11.sp,
                        color = primaryGreen,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = matchedVehicle,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = manifestText,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Tombol Telepon Satpam
            Button(
                onClick = {
                    Toast.makeText(context, "Menghubungi Pos Satpam UMY...", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryRed)
            ) {
                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Telepon Satpam", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Jika memungkinkan, pindah ke tempat ramai dan tetap buka aplikasi.",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 11.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Saya sudah aman / Salah tekan",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryRed
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}