package com.gas.nebengmu.ui.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
fun RegisterPassengerScreen(
    // Callback diperbarui dengan menambahkan parameter kelima untuk gender (String)
    onRegisterSubmit: (String, String, String, String, String) -> Unit,
    onNavigateBack: () -> Unit
) {
    var namaLengkap by remember { mutableStateOf("") }
    var nim by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("L") } // Default "L" (Laki-laki) atau "P" (Perempuan)
    var passwordVisible by remember { mutableStateOf(false) }
    var agreedToTerms by remember { mutableStateOf(false) }

    val primaryGreen = Color(0xFF006D44)
    val primaryRed = Color(0xFFB71C1C)
    val lightGreenBg = Color(0xFFE8F5E9)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buat akun kampus", fontSize = 18.sp, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
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
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Badge Langkah 1 dari 2
            Surface(
                color = lightGreenBg,
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = " LANGKAH 1 DARI 2 ",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryGreen
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Judul & Subtitle
            Text(
                text = "Kenalan dulu, yuk.",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp,
                    color = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Gunakan NIM dan email kampus aktif agar komunitas tetap aman.",
                fontSize = 13.sp,
                color = Color.Gray,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Input Nama Lengkap
            Text("Nama lengkap", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = namaLengkap,
                onValueChange = { namaLengkap = it },
                placeholder = { Text("Nabila Putri", color = Color.LightGray) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray) },
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

            // Input NIM
            Text("NIM", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = nim,
                onValueChange = { nim = it },
                placeholder = { Text("20230140012", color = Color.LightGray) },
                leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null, tint = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF9F9F9),
                    focusedContainerColor = Color(0xFFF9F9F9),
                    unfocusedBorderColor = Color(0xFFE0E0E0)
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "NIM akan dicocokkan dengan identitas mahasiswa UMY.",
                fontSize = 11.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input Email Kampus
            Text("Email kampus", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("nabila@students.umy.ac.id", color = Color.LightGray) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Color.Gray) },
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

            // 🌟 TAMBAHAN: Pilihan Jenis Kelamin (Gender Selection)
            Text("Jenis Kelamin", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { selectedGender = "L" },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (selectedGender == "L") lightGreenBg else Color(0xFFF9F9F9),
                        contentColor = if (selectedGender == "L") primaryGreen else Color.Gray
                    ),
                    border = BorderStroke(
                        width = if (selectedGender == "L") 2.dp else 1.dp,
                        color = if (selectedGender == "L") primaryGreen else Color(0xFFE0E0E0)
                    )
                ) {
                    Text("Laki-laki", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                OutlinedButton(
                    onClick = { selectedGender = "P" },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (selectedGender == "P") lightGreenBg else Color(0xFFF9F9F9),
                        contentColor = if (selectedGender == "P") primaryGreen else Color.Gray
                    ),
                    border = BorderStroke(
                        width = if (selectedGender == "P") 2.dp else 1.dp,
                        color = if (selectedGender == "P") primaryGreen else Color(0xFFE0E0E0)
                    )
                ) {
                    Text("Perempuan", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Digunakan untuk validasi pemisahan rute perjalanan yang aman.",
                fontSize = 11.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input Kata Sandi
            Text("Kata sandi", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("••••••••••••", color = Color.LightGray) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Gray) },
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
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Minimal 8 karakter, kombinasi huruf dan angka.",
                fontSize = 11.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Kartu Informasi Verifikasi Data
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
                        contentDescription = null,
                        tint = primaryGreen,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Identitas diverifikasi, data dijaga", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.Black)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            "NIM digunakan untuk validasi mahasiswa. Kode OTP dikirim ke email kampus. Lokasi digunakan saat layanan perjalanan.",
                            fontSize = 11.sp,
                            color = Color.DarkGray,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Checkbox Persetujuan
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = agreedToTerms,
                    onCheckedChange = { agreedToTerms = it },
                    colors = CheckboxDefaults.colors(checkedColor = primaryGreen)
                )
                Text(
                    text = "I setujui Syarat Layanan dan Kebijakan Privasi NebengMU.",
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tombol Daftar & Kirim OTP (Mengirimkan selectedGender ke parameter kelima)
            Button(
                onClick = { onRegisterSubmit(namaLengkap, nim, email, password, selectedGender) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryRed),
                enabled = agreedToTerms
            ) {
                Text("Daftar & kirim kode OTP", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}