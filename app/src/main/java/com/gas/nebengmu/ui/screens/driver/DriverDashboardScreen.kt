package com.gas.nebengmu.ui.screens.driver

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Verified
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
import com.gas.nebengmu.data.Ride
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverDashboardScreen(
    driverName: String = "Rizky Aditya",
    driverNim: String = "20220130028",
    onLogoutClick: () -> Unit,
    onOrderClick: (Int) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val primaryGreen = Color(0xFF006D44)
    val darkCardGreen = Color(0xFF004D2C)
    val primaryRed = Color(0xFFB71C1C)
    val lightYellowBg = Color(0xFFFFFDE7)
    val lightGreenBg = Color(0xFFE8F5E9)

    // Penyimpanan lokal untuk mengunci pilihan sif selama 1 hari penuh
    val prefs = remember { context.getSharedPreferences("nebengmu_driver_shift", Context.MODE_PRIVATE) }
    val todayDate = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) }
    val shiftStorageKey = "shift_${driverNim}_$todayDate"

    val validShifts = setOf("Pagi", "Siang", "Sore")
    // State sif yang sudah terkunci hari ini (null jika belum memilih)
    var lockedShift by remember(driverNim, todayDate) {
        val saved = prefs.getString(shiftStorageKey, null)
        mutableStateOf(if (saved in validShifts) saved else null)
    }
    // State sementara untuk menampung sif yang diklik sebelum dikonfirmasi di AlertDialog
    var pendingShiftToConfirm by remember { mutableStateOf<String?>(null) }

    val shiftOptions = listOf(
        "Pagi" to "06.00 - 11.00",
        "Siang" to "10.00 - 15.00",
        "Sore" to "15.00 - 20.00"
    )
    var isOnline by remember { mutableStateOf(false) }

    // State daftar pesanan dari server
    var rideList by remember { mutableStateOf<List<Ride>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    var totalEarnings by remember { mutableStateOf(0.0) }
    var totalTrips by remember { mutableStateOf(0) }

    val initials = remember(driverName) {
        driverName.trim()
            .split(" ")
            .filter { it.isNotEmpty() }
            .take(2)
            .joinToString("") { it.first().uppercase() }
            .ifEmpty { "DR" }
    }

    fun fetchSearchingRides() {
        if (driverNim.isBlank()) {
            Toast.makeText(context, "Sesi NIM pengemudi tidak ditemukan. Silakan login ulang.", Toast.LENGTH_SHORT).show()
            isOnline = false
            return
        }

        coroutineScope.launch {
            isLoading = true
            try {
                // Mengirimkan driverNim beserta sif yang sedang aktif dikunci hari ini
                val response = RetrofitClient.instance.getSearchingRides(
                    driverNim = driverNim,
                    shift = lockedShift
                )
                if (response.isSuccessful) {
                    rideList = response.body() ?: emptyList()
                } else {
                    Toast.makeText(context, "Gagal memuat daftar order (${response.code()})", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Kesalahan Jaringan: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                isLoading = false
            }
        }
    }

    fun fetchEarnings() {
        coroutineScope.launch {
            try {
                val response = RetrofitClient.instance.getDriverEarnings(driverNim)
                if (response.isSuccessful && response.body() != null) {
                    totalEarnings = response.body()!!.total_earnings
                    totalTrips = response.body()!!.total_trips
                }
            } catch (_: Exception) {
            }
        }
    }

    LaunchedEffect(driverNim) {
        fetchEarnings()
    }

    LaunchedEffect(isOnline) {
        if (isOnline) {
            fetchSearchingRides()
        } else {
            rideList = emptyList()
        }
    }

    // --- DIALOG VALIDASI PEMILIHAN SIF HARIAN ---
    if (pendingShiftToConfirm != null) {
        AlertDialog(
            onDismissRequest = { pendingShiftToConfirm = null },
            icon = {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = primaryGreen
                )
            },
            title = {
                Text(
                    text = "Konfirmasi Sif ${pendingShiftToConfirm}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = "Apakah kamu yakin memilih Sif ${pendingShiftToConfirm} untuk operasional hari ini? " +
                            "Pilihan sif hanya bisa ditentukan 1 kali dalam sehari dan tidak dapat diganti hingga besok.",
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = Color.DarkGray
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val chosen = pendingShiftToConfirm!!
                        prefs.edit().putString(shiftStorageKey, chosen).apply()
                        lockedShift = chosen
                        pendingShiftToConfirm = null
                        Toast.makeText(context, "Sif $chosen berhasil dikunci untuk hari ini.", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = primaryGreen),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Ya, Kunci Sif", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingShiftToConfirm = null }) {
                    Text("Batal", color = Color.Gray)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mitra NebengMU", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black) },
                actions = {
                    IconButton(onClick = { if (isOnline) fetchSearchingRides() }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Refresh Order", tint = Color.Black)
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
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Bagian Profil Pengemudi
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(lightGreenBg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(initials, fontWeight = FontWeight.Bold, color = primaryGreen, fontSize = 17.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(driverName, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Color.Black)
                    Text("NIM $driverNim · Mahasiswa UMY", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(color = lightGreenBg, shape = RoundedCornerShape(12.dp)) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Verified, contentDescription = null, tint = primaryGreen, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Pengemudi terverifikasi", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = primaryGreen)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Kartu Pendapatan
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = darkCardGreen,
                shadowElevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("PENDAPATAN HARI INI", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xB3FFFFFF))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Rp${totalEarnings.toInt()}", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("$totalTrips perjalanan selesai", fontSize = 12.sp, color = Color(0xCCFFFFFF))
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // --- BAGIAN PEMILIHAN SIF HARIAN (PAGI / SIANG / MALAM) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Sif Operasional Hari Ini", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                if (lockedShift != null) {
                    Surface(color = lightGreenBg, shape = RoundedCornerShape(8.dp)) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = primaryGreen, modifier = Modifier.size(11.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Terkunci Hari Ini", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = primaryGreen)
                        }
                    }
                } else {
                    Text("Pilih 1x sehari", fontSize = 11.sp, color = primaryRed, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                shiftOptions.forEach { (shiftName, shiftHours) ->
                    val isSelected = lockedShift == shiftName
                    val isLockedOther = lockedShift != null && !isSelected

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable(enabled = lockedShift == null) {
                                pendingShiftToConfirm = shiftName
                            },
                        shape = RoundedCornerShape(12.dp),
                        color = when {
                            isSelected -> lightGreenBg
                            isLockedOther -> Color(0xFFF5F5F5)
                            else -> Color(0xFFF9F9F9)
                        },
                        border = BorderStroke(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) primaryGreen else Color(0xFFE0E0E0)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = shiftName,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when {
                                        isSelected -> primaryGreen
                                        isLockedOther -> Color.LightGray
                                        else -> Color.Black
                                    }
                                )
                                if (isSelected) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Sif Terkunci",
                                        tint = primaryGreen,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = shiftHours,
                                fontSize = 10.sp,
                                color = if (isLockedOther) Color.LightGray else Color.Gray
                            )
                        }
                    }
                }
            }

            if (lockedShift != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Kamu bertugas pada Sif $lockedShift. Sif tidak dapat diubah lagi hingga esok hari.",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tombol Mulai / Berhenti Menerima Order
            Button(
                onClick = {
                    if (!isOnline && lockedShift == null) {
                        Toast.makeText(context, "Pilih dan kunci sif operasional terlebih dahulu!", Toast.LENGTH_SHORT).show()
                    } else {
                        isOnline = !isOnline
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (isOnline) primaryGreen else primaryRed)
            ) {
                Icon(Icons.Default.PowerSettingsNew, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isOnline) "Berhenti Menerima Order (Sif $lockedShift)" else "Mulai Menerima Order",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // DAFTAR PESANAN MASUK
            if (isOnline) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Order Masuk (Filter Sesama Gender)", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                    Surface(color = lightGreenBg, shape = RoundedCornerShape(8.dp)) {
                        Text(
                            text = "Sif $lockedShift",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryGreen
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                if (isLoading) {
                    Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = primaryGreen)
                    }
                } else if (rideList.isEmpty()) {
                    Surface(
                        color = Color(0xFFF9F9F9),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Belum ada pesanan masuk pada Sif $lockedShift hari ini yang sesuai dengan gender dan kendaraanmu. Ketuk ikon lonceng di kanan atas untuk memuat ulang.",
                            modifier = Modifier.padding(16.dp),
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(rideList) { ride ->
                            val genderLabel = when (ride.passenger_gender?.uppercase()) {
                                "L" -> "Sesama Laki-laki"
                                "P" -> "Sesama Perempuan"
                                else -> "Terverifikasi"
                            }

                            val isSharedRide = ride.ride_mode.equals("shared", ignoreCase = true)
                            val modeLabel = if (isSharedRide) "Nebeng Bareng · Mobil" else "Perjalanan Sendiri"
                            val modeBgColor = if (isSharedRide) Color(0xFFFFF3E0) else lightGreenBg
                            val modeTextColor = if (isSharedRide) Color(0xFFE65100) else primaryGreen

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onOrderClick(ride.ride_id) },
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFFF5F5F5),
                                shadowElevation = 1.dp
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = ride.passenger_name ?: "Mahasiswa (${ride.passenger_nim})",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black
                                            )
                                            Text(
                                                text = "NIM: ${ride.passenger_nim} · $genderLabel",
                                                fontSize = 11.sp,
                                                color = primaryGreen,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                        Text(
                                            text = "Rp${ride.fare.toInt()}",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = primaryGreen
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Surface(
                                        color = modeBgColor,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = modeLabel,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = modeTextColor
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Jemput: ${ride.pickup_location}", fontSize = 12.sp, color = Color.DarkGray)
                                    Text("Tujuan: ${ride.destination_location}", fontSize = 12.sp, color = Color.DarkGray)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Ketuk untuk melihat detail order →", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = primaryGreen)
                                }
                            }
                        }
                    }
                }
            } else {
                Surface(
                    color = lightYellowBg,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFFF57F17), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (lockedShift == null) {
                                "Pilih sif operasional hari ini (Pagi/Siang/Sore), lalu aktifkan penerimaan order."
                            } else {
                                "Kamu sedang offline di Sif $lockedShift. Aktifkan penerimaan order untuk mulai menerima pesanan."
                            },
                            fontSize = 12.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}