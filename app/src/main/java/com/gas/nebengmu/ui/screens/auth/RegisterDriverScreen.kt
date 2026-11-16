package com.gas.nebengmu.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterDriverScreen(
    onRegisterSubmit: (
        namaLengkap: String,
        nim: String,
        email: String,
        password: String,
        jenisKendaraan: String,
        noPlat: String,
        gender: String
    ) -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    var namaLengkap by remember { mutableStateOf("") }
    var nim by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("L") } // "L" atau "P"
    var passwordVisible by remember { mutableStateOf(false) }

    // Informasi Kendaraan & KTM
    var jenisKendaraan by remember { mutableStateOf("Sepeda Motor") }
    var noPlat by remember { mutableStateOf("") }
    var ktmFileName by remember { mutableStateOf("Belum ada file dipilih") }
    var agreedToTerms by remember { mutableStateOf(false) }

    val primaryGreen = Color(0xFF006D44)
    val primaryRed = Color(0xFFB71C1C)
    val lightGreenBg = Color(0xFFE8F5E9)

    val isKtmUploaded = ktmFileName != "Belum ada file dipilih"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pendaftaran Pengemudi", fontSize = 18.sp, fontWeight = FontWeight.SemiBold) },
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

            Surface(
                color = lightGreenBg,
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = " AKUN DRIVER / PENGEMUDI UMY ",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryGreen
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Siap berbagi perjalanan?",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp,
                    color = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Daftar sebagai pengemudi mahasiswa untuk mengatur shift dan mendapatkan penghasilan tambahan.",
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
                placeholder = { Text("Bagas Satya", color = Color.LightGray) },
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
            Text("Nomor Induk Mahasiswa (NIM)", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
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

            Spacer(modifier = Modifier.height(16.dp))

            // Input Email Kampus
            Text("Email kampus", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("bagas.satya.ft24@mail.umy.ac.id", color = Color.LightGray) },
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

            // Pilihan Jenis Kelamin
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
                text = "Pengemudi hanya akan dicocokkan dengan penumpang berjenis kelamin yang sama.",
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

            Spacer(modifier = Modifier.height(16.dp))

            // Pilihan Jenis Kendaraan
            Text("Jenis Kendaraan", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { jenisKendaraan = "Sepeda Motor" },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (jenisKendaraan == "Sepeda Motor") lightGreenBg else Color.Transparent,
                        contentColor = if (jenisKendaraan == "Sepeda Motor") primaryGreen else Color.Gray
                    ),
                    border = BorderStroke(
                        width = if (jenisKendaraan == "Sepeda Motor") 2.dp else 1.dp,
                        color = if (jenisKendaraan == "Sepeda Motor") primaryGreen else Color(0xFFE0E0E0)
                    )
                ) {
                    Icon(Icons.Default.TwoWheeler, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Motor")
                }

                OutlinedButton(
                    onClick = { jenisKendaraan = "Mobil" },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (jenisKendaraan == "Mobil") lightGreenBg else Color.Transparent,
                        contentColor = if (jenisKendaraan == "Mobil") primaryGreen else Color.Gray
                    ),
                    border = BorderStroke(
                        width = if (jenisKendaraan == "Mobil") 2.dp else 1.dp,
                        color = if (jenisKendaraan == "Mobil") primaryGreen else Color(0xFFE0E0E0)
                    )
                ) {
                    Icon(Icons.Default.DirectionsCar, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Mobil")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nomor Plat Kendaraan
            Text("Nomor Plat Kendaraan", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = noPlat,
                onValueChange = { noPlat = it },
                placeholder = { Text("AB 1234 XY", color = Color.LightGray) },
                leadingIcon = { Icon(Icons.Default.ConfirmationNumber, contentDescription = null, tint = Color.Gray) },
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

            // Unggah KTM
            Text("Kartu Tanda Mahasiswa (KTM)", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        width = if (isKtmUploaded) 1.5.dp else 1.dp,
                        color = if (isKtmUploaded) primaryGreen else Color(0xFFE0E0E0),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(if (isKtmUploaded) lightGreenBg else Color(0xFFF9F9F9))
                    .clickable {
                        ktmFileName = if (nim.isNotBlank()) "KTM_${nim}_UMY.jpg" else "KTM_Mahasiswa_UMY.jpg"
                        Toast.makeText(context, "File KTM berhasil dilampirkan", Toast.LENGTH_SHORT).show()
                    },
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isKtmUploaded) Icons.Default.CheckCircle else Icons.Default.UploadFile,
                        contentDescription = null,
                        tint = primaryGreen
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (isKtmUploaded) ktmFileName else "Ketuk untuk melampirkan foto KTM",
                        color = if (isKtmUploaded) primaryGreen else Color.Gray,
                        fontWeight = if (isKtmUploaded) FontWeight.SemiBold else FontWeight.Normal,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Kartu Informasi Verifikasi Admin
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
                        Text("Verifikasi Admin (1x24 Jam)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.Black)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            "Akun pengemudi akan diperiksa oleh staf admin kampus sebelum dapat mengaktifkan shift operasional.",
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
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { agreedToTerms = !agreedToTerms },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = agreedToTerms,
                    onCheckedChange = { agreedToTerms = it },
                    colors = CheckboxDefaults.colors(checkedColor = primaryGreen)
                )
                Text(
                    text = "Saya menyetujui Ketentuan Layanan Pengemudi NebengMU.",
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tombol Submit Pendaftaran Pengemudi
            Button(
                onClick = {
                    when {
                        namaLengkap.isBlank() || nim.isBlank() || email.isBlank() || password.isBlank() -> {
                            Toast.makeText(context, "Lengkapi nama, NIM, email, dan kata sandi!", Toast.LENGTH_SHORT).show()
                        }
                        noPlat.isBlank() -> {
                            Toast.makeText(context, "Masukkan nomor plat kendaraan!", Toast.LENGTH_SHORT).show()
                        }
                        !isKtmUploaded -> {
                            Toast.makeText(context, "Ketuk kotak KTM untuk melampirkan KTM terlebih dahulu!", Toast.LENGTH_SHORT).show()
                        }
                        !agreedToTerms -> {
                            Toast.makeText(context, "Centang persetujuan ketentuan layanan terlebih dahulu!", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            onRegisterSubmit(
                                namaLengkap.trim(),
                                nim.trim(),
                                email.trim(),
                                password,
                                jenisKendaraan,
                                noPlat.trim().uppercase(),
                                selectedGender
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryRed)
            ) {
                Text("Daftar sebagai Pengemudi", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}