package org.example.project.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.data.Player
import org.example.project.ui.theme.LossRed
import org.example.project.ui.theme.TextDark
import org.example.project.ui.theme.WinGreen

@Composable
fun PlayerHeaderItem(
    player: Player,
    modifier: Modifier = Modifier
){
    Column (
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally // căn giữa tất cả các món đồ bên trong
    ) {
    // Tên người chơi
        Text(
            text = player.name,
            color = TextDark,
            fontSize = 14.sp
        )
        //Diem Tong logic lon hon 0 màu xanh < màu đỏ
        val scoreColor = if(player.score >=0) WinGreen else LossRed
        Text(
            text = player.score.toString(),
            color = scoreColor,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}