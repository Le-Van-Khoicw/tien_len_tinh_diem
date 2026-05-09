package org.example.project.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.data.GameMatch
import org.example.project.ui.theme.WarmCream
import org.example.project.ui.theme.WarmOrange
import org.example.project.viewmodel.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchHistoryScreen(
    viewModel: GameViewModel,
    onAddNewMatch: () -> Unit
) {
    // Chuyển sang dùng SnapshotStateList trực tiếp để Compose quan sát tốt nhất
    val matchHistory = viewModel.matchHistory

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Lịch sử trận đấu (${matchHistory.size})", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WarmOrange)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddNewMatch,
                containerColor = Color(0xFFFFCC80),
                contentColor = Color.Black,
                icon = { Text("+", fontSize = 24.sp, fontWeight = FontWeight.Bold) },
                text = { Text("Trận mới", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(WarmCream)
        ) {
            if (matchHistory.isEmpty()) {
                EmptyHistoryContent()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(matchHistory) { match ->
                        MatchItem(match)
                    }
                }
            }
        }
    }
}

@Composable
fun MatchItem(match: GameMatch) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = Color(0xFFF57C00),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "ID: ${match.id}",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text("${match.history.size} ván đấu", fontSize = 14.sp, color = Color.Gray)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                match.players.forEach { player ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(player.name, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                        Text(
                            text = if (player.score >= 0) "+${player.score}" else "${player.score}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (player.score >= 0) Color(0xFF388E3C) else Color(0xFFD32F2F)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyHistoryContent() {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("👋", fontSize = 60.sp)
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Chào mừng bạn!\nBạn chưa có trận đấu nào được lưu.",
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            fontSize = 16.sp,
            color = Color.Gray
        )
        Spacer(Modifier.height(32.dp))
        Text(
            text = "Bấm nút \"Trận mới\" để bắt đầu ghi điểm ván bài đầu tiên nhé!",
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            fontSize = 14.sp,
            color = Color.LightGray
        )
    }
}
