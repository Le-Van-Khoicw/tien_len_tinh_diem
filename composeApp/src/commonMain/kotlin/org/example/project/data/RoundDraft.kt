package org.example.project.data

data class RoundDraft(
    val rankMap: Map<Int, Int> = emptyMap(),
    val anTrangIds: Set<Int> = emptySet(),
    val denIds: Set<Int> = emptySet(),
    val biChayIds: Set<Int> = emptySet(),
    val manualScores: Map<Int, String> = emptyMap() // Điểm nhập tay dưới dạng chuỗi
)
