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

    // --- CỜ BÁO THẮNG CUỘC ---
    val winner = viewModel.nguoiThangCuoc.value
    if (winner != null) {
        AlertDialog(
            onDismissRequest = { /* Không cho tắt ngang */ },
            title = { Text("🏆 CHIẾN THẮNG!") },
            text = { Text("Chúc mừng ${winner.name} đã chạm mốc và giành chiến thắng chung cuộc!") },
            confirmButton = {
                Button(onClick = { 
                    viewModel.nguoiThangCuoc.value = null 
                    viewModel.ketThucVaLuuTran() // LƯU TRẬN KHI THẮNG
                    onExitGame()
                }) {
                    Text("Kết thúc trận")
                }
            }
        )
    }

    // --- DIALOG XÁC NHẬN THOÁT SỚM ---
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Kết thúc trận đấu?") },
            text = { Text("Bạn có chắc muốn kết thúc trận đấu này và lưu lại lịch sử không?") },
            confirmButton = {
                Button(
                    onClick = { 
                        showExitDialog = false
                        viewModel.ketThucVaLuuTran() // LƯU TRẬN KHI THOÁT SỚM
                        onExitGame() 
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
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
                    IconButton(onClick = { showExitDialog = true }) {
                        Text("X", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = WarmOrange,
                    titleContentColor = Color.Black
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showSheet = true },
                containerColor = WarmOrange
            ) {
                Text(
                    text = "+",
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(WarmCream)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Spacer(modifier = Modifier.width(40.dp))
                viewModel.players.forEach { player ->
                    PlayerHeaderItem(
                        player = player,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(viewModel.danhSachVanDau) { round ->
                    RoundHistoryRow(
                        round = round,
                        players = viewModel.players
                    )
                }
            }
        }
    }
}
