package com.gas.nebengmu.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Group
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

@Composable
fun WelcomeScreen(
    onStartJourneyClick: () -> Unit,
    onNavigateToAdmin: () -> Unit
) {
    val primaryGreen = Color(0xFF006D44)
    val primaryRed = Color(0xFFB71C1C)
    val lightGreenBg = Color(0xFFE8F5E9)

    // State untuk mengontrol muncul tidaknya dialog PIN rahasia admin
    var showAdminDialog by remember { mutableStateOf(false) }
    var pinInput by remember { mutableStateOf("") }
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Bagian Atas: Logo & Judul Aplikasi
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(lightGreenBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AltRoute,
                            contentDescription = "Logo",
                            tint = primaryGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "NebengMU",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Ilustrasi Card / Mock Peta Geofencing
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFF4F6F4)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = "Map Illustration",
                            tint = primaryGreen,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Ilustrasi Area Kampus UMY & Geofencing",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Badge Tagline
                Surface(
                    color = lightGreenBg,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = " 🎓 DARI MAHASISWA, UNTUK MAHASISWA ",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryGreen
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Judul Utama
                Text(
                    text = "Ke kampus, lebih dekat. Lebih bareng.",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        color = Color.Black
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Mobilitas di sekitar UMY bersama mahasiswa terverifikasi. Pilih cara berangkat yang pas untukmu.",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Dua Kotak Fitur (Perjalanan Sendiri & Nebeng Bareng)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Kotak 1
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = primaryGreen, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Nebeng", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("Langsung ke tujuan", fontSize = 11.sp, color = Color.Gray)
                        }
                    }
                    // Kotak 2
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = lightGreenBg),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Icon(Icons.Default.Group, contentDescription = null, tint = primaryGreen, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Nebeng Bareng", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("Searah, berbagi biaya", fontSize = 11.sp, color = Color.DarkGray)
                        }
                    }
                }
            }

            // Bagian Bawah: Tombol Mulai Perjalanan & Teks Tersembunyi Portal Admin
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = onStartJourneyClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryRed)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Mulai perjalanan", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 🛡️ Teks Tersembunyi (Disamarkan sebagai Versi Aplikasi untuk Akses Satpam)
                TextButton(
                    onClick = { showAdminDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "v1.0.0 · Portal Satpam UMY",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }

    // --- POPUP DIALOG PIN RAHASIA ADMIN ---
    if (showAdminDialog) {
        AlertDialog(
            onDismissRequest = {
                showAdminDialog = false
                pinInput = ""
            },
            title = { Text("Otorisasi Khusus Satpam", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            text = {
                Column {
                    Text("Masukkan PIN akses pusat darurat kampus:", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = pinInput,
                        onValueChange = { pinInput = it },
                        placeholder = { Text("PIN (Contoh: 2026)") },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (pinInput == "2026") { // PIN Rahasia untuk demo penjurian
                            showAdminDialog = false
                            pinInput = ""
                            onNavigateToAdmin() // Memanggil navigasi admin
                        } else {
                            Toast.makeText(context, "PIN Salah!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = primaryGreen)
                ) {
                    Text("Masuk")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showAdminDialog = false
                    pinInput = ""
                }) {
                    Text("Batal", color = Color.Gray)
                }
            }
        )
    }
}