package org.example.project.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    val history = viewModel.matchHistory

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Đã chơi (${history.size})", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { /* Cài đặt */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Menu */ }) {
                        Text("...", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WarmOrange)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddNewMatch,
                containerColor = Color(0xFFFFE0B2),
                contentColor = Color.Black,
                icon = { Text("+", fontSize = 24.sp) },
                text = { Text("Trận mới") }
            )
        }
    ) { padding ->
        if (history.isEmpty()) {
            EmptyHistoryContent(padding)
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(WarmCream)
            ) {
                items(history) { match ->
                    MatchItem(match)
                }
            }
        }
    }
}

@Composable
fun MatchItem(match: GameMatch) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Trận đấu #${match.id}", fontWeight = FontWeight.Bold, color = Color.DarkGray)
                Spacer(Modifier.weight(1f))
                Text("${match.history.size} ván", fontSize = 12.sp, color = Color.Gray)
            }
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                match.players.forEach { player ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(player.name, fontSize = 11.sp, color = Color.Gray)
                        Text("${player.score}", fontWeight = FontWeight.Bold, color = if(player.score >= 0) Color(0xFF2E7D32) else Color.Red)
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyHistoryContent(padding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .background(WarmCream)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("👋", fontSize = 40.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Chào mừng bạn đến với\nỨng dụng ghi điểm bài tiến lên!",
            textAlign = TextAlign.Center,
            color = Color.Gray,
            lineHeight = 24.sp
        )
        Spacer(modifier = Modifier.height(32.dp))
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
            InfoRow(text = "👉 Ấn vào \"Trận mới\" để tạo bàn chơi và nhập kết quả cho các ván.")
            Spacer(modifier = Modifier.height(12.dp))
            InfoRow(text = "👉 Vào mục cài đặt ⚙️ để thay đổi cách tính điểm.")
        }
    }
}

@Composable
fun InfoRow(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        color = Color.Gray,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}
