package org.example.project.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.data.GameRound
import org.example.project.ui.theme.LossRed
import org.example.project.ui.theme.TextDark
import org.example.project.ui.theme.WinGreen
import org.example.project.data.Player
@Composable
fun RoundHistoryRow(
    round: GameRound,
    players: List<Player>
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = round.roundNumber.toString(),
                modifier = Modifier.width(40.dp),
                textAlign = TextAlign.Center,
                color = Color.Gray,
                fontSize = 14.sp
            )

            // DÙNG VÒNG LẶP FOR ĐỂ GIỮ SCOPE CHO WEIGHT
            for (player in players) {
                val diem = round.scoreChanges[player.id] ?: 0
                val mauChu = if (diem > 0) WinGreen else if (diem < 0) LossRed else TextDark
                val dauCach = if (diem > 0) "+" else ""

                Text(
                    text = "$dauCach$diem",
                    modifier = Modifier.weight(1f), // Hết lỗi ở đây!
                    color = mauChu,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // ĐỔI THÀNH HorizontalDivider CỦA MATERIAL 3
        HorizontalDivider(
            color = Color.LightGray,
            thickness = 0.5.dp
        )
    }
}