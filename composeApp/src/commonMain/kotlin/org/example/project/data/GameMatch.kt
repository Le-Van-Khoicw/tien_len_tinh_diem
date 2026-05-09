package org.example.project.data

import kotlinx.serialization.Serializable

@Serializable
data class GameMatch(
    val id: String,
    val players: List<Player>,
    val history: List<GameRound>,
    val limit: Int?,
    val date: Long // Thời gian tạo trận đấu
)
