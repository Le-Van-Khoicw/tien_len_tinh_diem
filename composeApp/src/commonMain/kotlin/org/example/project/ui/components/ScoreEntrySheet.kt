package org.example.project.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.viewmodel.GameViewModel

@Composable
fun ScoreEntrySheet(
    viewModel: GameViewModel,
    onDismiss: () -> Unit
) {
    val nhap = viewModel.toNhap.value
    val players = viewModel.players
    var focusedPlayerId by remember { mutableStateOf<Int?>(players.firstOrNull()?.id) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp) // Giảm padding dọc
            .background(Color(0xFFFFF9F2), RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
    ) {
        // 1. Header (Thu gọn khoảng cách)
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "KẾT QUẢ VÁN ${viewModel.danhSachVanDau.size + 1}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp // Giảm cỡ chữ
            )
            Spacer(Modifier.weight(1f))
            Text("Nhập đơn giản", fontSize = 11.sp, color = Color.Gray)
            Checkbox(
                checked = viewModel.isSimpleInput, 
                onCheckedChange = { viewModel.isSimpleInput = it }
            )
        }

        // 2. BẢNG NHẬP ĐIỂM
        Row(modifier = Modifier.fillMaxWidth().padding(start = 70.dp)) {
            players.forEach { p ->
                Text(
                    text = p.name,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    maxLines = 1
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        if (viewModel.isSimpleInput) {
            // --- CHẾ ĐỘ NHẬP ĐƠN GIẢN ---
            ScoreEntryRow(label = "Nhập điểm") {
                players.forEach { p ->
                    val manualScore = nhap.manualScores[p.id] ?: ""
                    val isFocused = focusedPlayerId == p.id
                    
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 3.dp)
                            .height(38.dp) // Giảm chiều cao ô nhập
                            .clickable { focusedPlayerId = p.id },
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White,
                        border = if (isFocused) BorderStroke(2.dp, Color(0xFF9575CD)) else BorderStroke(1.dp, Color.LightGray)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = manualScore.ifEmpty { "0" },
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = if (manualScore.isEmpty()) Color.LightGray else Color.Black
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // BÀN PHÍM SỐ (Thu nhỏ nút bấm)
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp)) {
                val keys = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("+/-", "0", "DEL")
                )
                keys.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        row.forEach { key ->
                            Button(
                                onClick = { focusedPlayerId?.let { viewModel.onKeypadPress(it, key) } },
                                modifier = Modifier.weight(1f).height(40.dp).padding(vertical = 2.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (key == "DEL" || key == "+/-") Color(0xFFE0E0E0) else Color.White,
                                    contentColor = Color.Black
                                ),
                                shape = RoundedCornerShape(6.dp),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(key, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }

        } else {
            // --- CHẾ ĐỘ CHI TIẾT (Cũng thu gọn chiều cao) ---
            ScoreEntryRow(label = "Tới") {
                players.forEach { p ->
                    val rank = viewModel.getRankForPlayer(p.id)
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Surface(
                            modifier = Modifier.size(32.dp),
                            shape = CircleShape,
                            color = if (rank != null) Color(0xFFFFD54F) else Color(0xFFEEEEEE),
                            onClick = { viewModel.autoAssignRank(p.id) }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = rank?.toString() ?: "-", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }

            ScoreEntryRow(label = "Ăn trắng") {
                players.forEach { p ->
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Checkbox(checked = nhap.anTrangIds.contains(p.id), onCheckedChange = { viewModel.toggleAnTrang(p.id) })
                    }
                }
            }

            ScoreEntryRow(label = "Đền") {
                players.forEach { p ->
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Checkbox(checked = nhap.denIds.contains(p.id), onCheckedChange = { viewModel.toggleDen(p.id) })
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 3. TỔNG KẾT (Thu gọn)
        ScoreEntryRow(label = "Tổng kết") {
            players.forEach { p ->
                val diemDuKien = viewModel.tinhDiemDuKien(p.id)
                Surface(
                    modifier = Modifier.weight(1f).padding(horizontal = 3.dp),
                    color = if (diemDuKien >= 0) Color(0xFFC8E6C9) else Color(0xFFFFCDD2),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = if (diemDuKien >= 0) "+$diemDuKien" else "$diemDuKien",
                        modifier = Modifier.padding(vertical = 4.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = if (diemDuKien >= 0) Color(0xFF2E7D32) else Color(0xFFC62828)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 4. Nút thao tác (Đảm bảo hiện hết)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                Text("Bỏ qua", color = Color.Gray, fontSize = 14.sp)
            }
            Button(
                onClick = {
                    viewModel.chotSoLuuVan()
                    onDismiss()
                },
                modifier = Modifier.weight(1f).height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81C784)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("LƯU", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun ScoreEntryRow(
    label: String,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp), // Giảm padding dọc dòng
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.width(70.dp),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray
        )
        content()
    }
}
