package org.example.project.data

import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val id: Int,
    val name: String,
    val score: Int = 0,
    val chuoithua: Int = 0
)
