package org.example.project.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.ui.theme.WarmOrange
import org.example.project.viewmodel.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupScreen(
    viewModel: GameViewModel,
    onStartGame: () -> Unit,
    onDismiss: () -> Unit
) {
    var t1 by remember { mutableStateOf("Người chơi 1") }
    var t2 by remember { mutableStateOf("Người chơi 2") }
    var t3 by remember { mutableStateOf("Người chơi 3") }
    var t4 by remember { mutableStateOf("Người chơi 4") }
    
    var isLimitEnabled by remember { mutableStateOf(false) }
    var limitValue by remember { mutableStateOf("50") }
    var errorMessage by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black.copy(alpha = 0.5f)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Card(
                modifier = Modifier.fillMaxWidth(0.9f).padding(16.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("Bàn chơi mới", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    
                    Spacer(modifier = Modifier.height(20.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        PlayerInputItem(t1, { t1 = it }, "Người chơi 1", Modifier.weight(1f))
                        Spacer(modifier = Modifier.width(16.dp))
                        PlayerInputItem(t2, { t2 = it }, "Người chơi 2", Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        PlayerInputItem(t3, { t3 = it }, "Người chơi 3", Modifier.weight(1f))
                        Spacer(modifier = Modifier.width(16.dp))
                        PlayerInputItem(t4, { t4 = it }, "Người chơi 4", Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = isLimitEnabled, onCheckedChange = { isLimitEnabled = it })
                        Text("Giới hạn điểm", fontSize = 14.sp)
                        if (isLimitEnabled) {
                            Spacer(modifier = Modifier.width(12.dp))
                            TextField(
                                value = limitValue,
                                onValueChange = { limitValue = it },
                                modifier = Modifier.width(80.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent,
                                    errorContainerColor = Color.Transparent
                                )
                            )
                        }
                    }

                    if (errorMessage.isNotEmpty()) {
                        Text(errorMessage, color = Color.Red, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = onDismiss) {
                            Text("Bỏ qua", color = Color(0xFF8D6E63))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = {
                                if (t1.isBlank() || t2.isBlank() || t3.isBlank() || t4.isBlank()) {
                                    errorMessage = "Nhập đủ tên đi ba!"
                                } else {
                                    val limit = if (isLimitEnabled) limitValue.toIntOrNull() else null
                                    viewModel.khoiTaoVanDau(t1, t2, t3, t4, limit)
                                    onStartGame()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFF3E0)),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                        ) {
                            Text("OK", color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlayerInputItem(value: String, onValueChange: (String) -> Unit, label: String, modifier: Modifier) {
    var hasBeenFocused by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(32.dp).background(Color(0xFFEEEEEE), androidx.compose.foundation.shape.CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("👤", fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            TextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text(label, fontSize = 12.sp) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused && !hasBeenFocused && value.startsWith("Người chơi")) {
                            onValueChange("")
                            hasBeenFocused = true
                        }
                    }
            )
        }
    }
}
