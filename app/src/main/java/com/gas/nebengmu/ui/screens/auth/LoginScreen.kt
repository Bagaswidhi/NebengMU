package com.gas.nebengmu.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginClick: (String, String, String) -> Unit, // Menerima parameter role
    onNavigateToRegister: (String) -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // State untuk toggle switch (true = Penumpang, false = Pengemudi)
    var isPenumpang by remember { mutableStateOf(true) }

    // Palet warna menyesuaikan mockup
    val primaryGreen = Color(0xFF006D44)
    val primaryRed = Color(0xFFB71C1C)
    val lightGreenBg = Color(0xFFE8F5E9)

    Scaffold(
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Placeholder Logo NebengMU
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(lightGreenBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AltRoute, // Bisa diganti logo aslimu nanti
                    contentDescription = "Logo NebengMU",
                    tint = primaryGreen,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Teks Sambutan
            Text(
                text = "Halo, teman kampus \uD83D\uDC4B",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp,
                    color = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Masuk dengan akun kampusmu. Mau berangkat sebagai apa hari ini?",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Custom Switch / Segmented Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF5F5F5))
            ) {
                // Tombol Penumpang
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isPenumpang) primaryGreen else Color.Transparent)
                        .clickable { isPenumpang = true },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Penumpang",
                        color = if (isPenumpang) Color.White else Color.Gray,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
                // Tombol Pengemudi
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (!isPenumpang) primaryGreen else Color.Transparent)
                        .clickable { isPenumpang = false },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Pengemudi",
                        color = if (!isPenumpang) Color.White else Color.Gray,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Input Email
            Text("Email kampus", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("bagas.satya.ft24@mail.umy.ac.id", color = Color.LightGray) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email", tint = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF9F9F9),
                    focusedContainerColor = Color(0xFFF9F9F9),
                    unfocusedBorderColor = Color(0xFFE0E0E0)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input Kata Sandi
            Text("Kata sandi", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("••••••••••••", color = Color.LightGray) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Sandi", tint = Color.Gray) },
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = "Toggle password", tint = Color.Gray)
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF9F9F9),
                    focusedContainerColor = Color(0xFFF9F9F9),
                    unfocusedBorderColor = Color(0xFFE0E0E0)
                )
            )

            // Lupa Kata Sandi
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { /* TODO: Aksi Lupa sandi */ }, contentPadding = PaddingValues(0.dp)) {
                    Text("Lupa kata sandi?", color = primaryGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Tombol Masuk Dinamis
            Button(
                onClick = {
                    // Tentukan role berdasarkan posisi toggle yang sedang aktif
                    val activeRole = if (isPenumpang) "Penumpang" else "Pengemudi"
                    onLoginClick(email, password, activeRole)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryRed)
            ) {
                Text(
                    text = if (isPenumpang) "Masuk sebagai penumpang" else "Masuk sebagai pengemudi",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Kartu Informasi Khusus Mahasiswa
            Surface(
                color = lightGreenBg,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = "Verified",
                        tint = primaryGreen,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Khusus mahasiswa UMY", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Identitas kampus diverifikasi sebelum perjalanan pertama.",
                            fontSize = 13.sp,
                            color = Color.DarkGray,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Link Daftar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Belum punya akun? ", color = Color.Gray)
                Text(
                    text = "Daftar sekarang",
                    color = primaryGreen,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        // Titik kuncinya di sini: Menyesuaikan rute daftar dengan toggle aktif
                        val targetRegisterRole = if (isPenumpang) "passenger" else "driver"
                        onNavigateToRegister(targetRegisterRole)
                    }
                )
            }
        }
    }
}