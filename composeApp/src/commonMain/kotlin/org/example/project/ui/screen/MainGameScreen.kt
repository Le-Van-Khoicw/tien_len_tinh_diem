package org.example.project.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.ui.components.PlayerHeaderItem
import org.example.project.ui.components.RoundHistoryRow
import org.example.project.ui.components.ScoreEntrySheet
import org.example.project.ui.theme.WarmCream
import org.example.project.ui.theme.WarmOrange
import org.example.project.viewmodel.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainGameScreen(
    viewModel: GameViewModel,
    onExitGame: () -> Unit
) {
    var showSheet by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }

    // --- DIALOG CHIẾN THẮNG ---
    val winner = viewModel.nguoiThangCuoc.value
    if (winner != null) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("🏆 CHIẾN THẮNG!") },
            text = { Text("Chúc mừng ${winner.name} đã giành chiến thắng chung cuộc!") },
            confirmButton = {
                Button(onClick = { 
                    viewModel.nguoiThangCuoc.value = null 
                    viewModel.ketThucVaLuuTran()
                    onExitGame()
                }) {
                    Text("Kết thúc & Lưu")
                }
            }
        )
    }

    // --- DIALOG XÁC NHẬN THOÁT ---
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Kết thúc trận?") },
            text = { Text("Toàn bộ lịch sử ván đấu của trận này sẽ được lưu lại.") },
            confirmButton = {
                Button(
                    onClick = { 
                        showExitDialog = false
                        viewModel.ketThucVaLuuTran()
                        onExitGame() 
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C))
                ) {
                    Text("Kết thúc & Lưu", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Tiếp tục chơi")
                }
            }
        )
    }

    if (showSheet) {
        ModalBottomSheet(onDismissRequest = { showSheet = false }) {
            ScoreEntrySheet(viewModel = viewModel, onDismiss = { showSheet = false })
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bàn chơi", fontWeight = FontWeight.Bold) },
                actions = {
                    // NÚT THOÁT TRẬN (Prominent)
                    Button(
                        onClick = { showExitDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black.copy(alpha = 0.1f)),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("KẾT THÚC", fontSize = 12.sp, color = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WarmOrange)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showSheet = true },
                containerColor = WarmOrange
            ) {
                Text("+", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(WarmCream)
        ) {
            // Header: Avatar & Score
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                Spacer(modifier = Modifier.width(40.dp))
                viewModel.players.forEach { player ->
                    PlayerHeaderItem(player = player, modifier = Modifier.weight(1f))
                }
            }

            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

            // Danh sách các ván
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(viewModel.danhSachVanDau) { round ->
                    RoundHistoryRow(round = round, players = viewModel.players)
                }
            }
        }
    }
}
