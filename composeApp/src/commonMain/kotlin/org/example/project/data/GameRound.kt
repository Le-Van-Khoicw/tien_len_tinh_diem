package org.example.project.data

import kotlinx.serialization.Serializable

@Serializable
data class GameRound(
    val roundNumber: Int,
    val scoreChanges: Map<Int, Int>
)
