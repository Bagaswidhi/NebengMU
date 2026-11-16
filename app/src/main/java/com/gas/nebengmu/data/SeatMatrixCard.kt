package com.gas.nebengmu.data

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventSeat
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SeatMatrixCard(
    gender: String = "L", // "L" untuk Laki-laki, "P" untuk Perempuan
    totalSeats: Int = 3,
    bookedPassengers: List<String> = listOf("Nabila", "Dinda"), // Nama penumpang yang sudah bergabung
    modifier: Modifier = Modifier
) {
    val primaryGreen = Color(0xFF006D44)
    val lightGreenBg = Color(0xFFE8F5E9)
    val isFemale = gender.equals("P", ignoreCase = true)

    // Nama penumpang simulasi menyesuaikan gender akun yang login
    val seat1Name = if (isFemale) "Nabila" else "Dimas"
    val seat2Name = if (isFemale) "Dinda" else "Farhan"
    val seat3Name = "Kosong"

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        border = BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header Info Kursi
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.EventSeat,
                        contentDescription = null,
                        tint = primaryGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Formasi Kursi Belakang",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                val availableSeats = totalSeats - bookedPassengers.size
                Surface(
                    color = if (availableSeats > 0) lightGreenBg else Color(0xFFFFEBEE),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (availableSeats > 0) "$availableSeats Kursi Kosong" else "Penuh",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (availableSeats > 0) primaryGreen else Color(0xFFB71C1C)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Visualisasi Slot Kursi (3 Kursi)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                for (i in 0 until totalSeats) {
                    val isBooked = i < bookedPassengers.size
                    val passengerName = if (isBooked) bookedPassengers[i] else "Kosong"

                    SeatSlotItem(
                        seatNumber = "Kursi ${i + 1}",
                        occupantName = passengerName,
                        isBooked = isBooked,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "💡 Maksimal 3 penumpang searah dalam satu kali perjalanan untuk efisiensi bahan bakar.",
                fontSize = 10.sp,
                color = Color.Gray,
                lineHeight = 14.sp
            )
        }
    }
}

@Composable
fun SeatSlotItem(
    seatNumber: String,
    occupantName: String,
    isBooked: Boolean,
    modifier: Modifier = Modifier
) {
    val primaryGreen = Color(0xFF006D44)
    val lightGreenBg = Color(0xFFE8F5E9)

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = if (isBooked) lightGreenBg else Color(0xFFFAFAFA),
        border = BorderStroke(
            width = 1.dp,
            color = if (isBooked) primaryGreen.copy(alpha = 0.3f) else Color(0xFFE0E0E0)
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(if (isBooked) primaryGreen else Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = if (isBooked) Color.White else Color.Gray,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = seatNumber,
                fontSize = 10.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = occupantName,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isBooked) primaryGreen else Color(0xFF757575),
                maxLines = 1
            )
        }
    }
}