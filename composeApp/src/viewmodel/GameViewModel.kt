package org.example.project.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import com.russhwolf.settings.serialization.decodeValueOrNull
import com.russhwolf.settings.serialization.encodeValue
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import org.example.project.data.GameMatch
import org.example.project.data.GameRound
import org.example.project.data.Player
import org.example.project.data.RoundDraft
import kotlin.random.Random

@Serializable
data class GameData(
    val matchHistory: List<GameMatch>, // Danh sách tất cả các trận đã kết thúc
    val currentMatch: GameMatch? = null // Trận đấu đang diễn ra (để nạp lại nếu văng app)
)

@OptIn(ExperimentalSerializationApi::class, ExperimentalSettingsApi::class)
class GameViewModel : ViewModel() {
    private val settings: Settings = Settings()
    private val STORAGE_KEY = "game_data_v2"

    // 1. KHO DỮ LIỆU TẤT CẢ CÁC TRẬN ĐÃ CHƠI
    val matchHistory = mutableStateListOf<GameMatch>()

    // 2. DỮ LIỆU TRẬN ĐANG DIỄN RA
    val players = mutableStateListOf<Player>(
        Player(id = 1, name = "Người chơi 1"),
        Player(id = 2, name = "Người chơi 2"),
        Player(id = 3, name = "Người chơi 3"),
        Player(id = 4, name = "Người chơi 4")
    )
    val danhSachVanDau = mutableStateListOf<GameRound>()
    var diemChamDich by mutableStateOf<Int?>(null)
    
    // 3. TỜ GIẤY NHÁP & TRẠNG THÁI UI
    var toNhap = mutableStateOf(RoundDraft())
    var isSimpleInput by mutableStateOf(false)
    var nguoiThangCuoc = mutableStateOf<Player?>(null)

    val luatDiem = mapOf(1 to 3, 2 to 2, 3 to 1, 4 to 0)

    init {
        loadData()
    }

    private fun saveData() {
        try {
            val current = GameMatch(
                id = "current",
                players = players.toList(),
                history = danhSachVanDau.toList(),
                limit = diemChamDich,
                date = 0 // Không quan trọng cho trận hiện tại
            )
            val data = GameData(
                matchHistory = matchHistory.toList(),
                currentMatch = current
            )
            settings.encodeValue(GameData.serializer(), STORAGE_KEY, data)
        } catch (e: Exception) {
            println("Lỗi lưu dữ liệu: ${e.message}")
        }
    }

    private fun loadData() {
        try {
            val data: GameData? = settings.decodeValueOrNull(GameData.serializer(), STORAGE_KEY)
            if (data != null) {
                matchHistory.clear()
                matchHistory.addAll(data.matchHistory)
                
                // Nạp lại trận đang chơi dở
                data.currentMatch?.let { match ->
                    players.clear()
                    players.addAll(match.players)
                    danhSachVanDau.clear()
                    danhSachVanDau.addAll(match.history)
                    diemChamDich = match.limit
                }
            }
        } catch (e: Exception) {
            println("Lỗi tải dữ liệu: ${e.message}")
        }
    }

    fun khoiTaoVanDau(ten1: String, ten2: String, ten3: String, ten4: String, gioiHan: Int?) {
        danhSachVanDau.clear()
        toNhap.value = RoundDraft()
        diemChamDich = gioiHan
        nguoiThangCuoc.value = null

        players.clear()
        players.addAll(listOf(
            Player(id = 1, name = ten1, score = 0),
            Player(id = 2, name = ten2, score = 0),
            Player(id = 3, name = ten3, score = 0),
            Player(id = 4, name = ten4, score = 0)
        ))
        saveData()
    }

    // --- HÀM QUAN TRỌNG: KẾT THÚC VÀ LƯU TRẬN VÀO LỊCH SỬ ---
    fun ketThucVaLuuTran() {
        if (danhSachVanDau.isNotEmpty()) {
            val completedMatch = GameMatch(
                id = Random.nextInt(1000, 9999).toString(),
                players = players.toList(),
                history = danhSachVanDau.toList(),
                limit = diemChamDich,
                date = 0 // Tạm thời để 0 vì KMP date hơi phức tạp
            )
            matchHistory.add(0, completedMatch)
        }
        
        // Reset dữ liệu trận hiện tại sau khi đã lưu vào lịch sử
        danhSachVanDau.clear()
        diemChamDich = null
        nguoiThangCuoc.value = null
        players.forEachIndexed { i, p -> players[i] = p.copy(score = 0) }
        
        saveData()
    }

    // --- LOGIC NHẬP ĐIỂM (Giữ nguyên) ---
    fun autoAssignRank(playerId: Int) {
        val current = toNhap.value
        val ranks = current.rankMap.toMutableMap()
        val currentRank = ranks[playerId]
        val otherTakenRanks = ranks.filter { it.key != playerId }.values.toSet()
        val availableRanks = (1..4).filter { it !in otherTakenRanks }
        if (availableRanks.isEmpty()) return
        val nextRank = if (currentRank == null) {
            availableRanks.minOrNull()
        } else {
            val higherRanks = availableRanks.filter { it > currentRank }
            higherRanks.minOrNull() ?: availableRanks.minOrNull()
        }
        if (nextRank != null) ranks[playerId] = nextRank
        toNhap.value = current.copy(rankMap = ranks)
    }

    fun toggleAnTrang(playerId: Int) {
        val current = toNhap.value
        val isSelected = current.anTrangIds.contains(playerId)
        toNhap.value = current.copy(
            anTrangIds = if (isSelected) emptySet() else setOf(playerId),
            rankMap = emptyMap(),
            denIds = emptySet()
        )
    }

    fun toggleDen(playerId: Int) {
        val current = toNhap.value
        val isSelected = current.denIds.contains(playerId)
        toNhap.value = current.copy(
            denIds = if (isSelected) emptySet() else setOf(playerId),
            rankMap = emptyMap(),
            anTrangIds = emptySet()
        )
    }

    fun updateManualScore(playerId: Int, scoreStr: String) {
        val current = toNhap.value
        val newManualScores = current.manualScores.toMutableMap()
        newManualScores[playerId] = scoreStr
        toNhap.value = current.copy(manualScores = newManualScores)
    }

    fun onKeypadPress(playerId: Int, key: String) {
        val current = toNhap.value
        val currentScore = current.manualScores[playerId] ?: ""
        val newScore = when (key) {
            "DEL" -> if (currentScore.isNotEmpty()) currentScore.dropLast(1) else ""
            "+/-" -> {
                if (currentScore.startsWith("-")) currentScore.drop(1)
                else if (currentScore.isNotEmpty()) "-$currentScore"
                else "-"
            }
            else -> if (currentScore == "0") key else currentScore + key
        }
        updateManualScore(playerId, newScore)
    }

    fun getRankForPlayer(playerId: Int): Int? = toNhap.value.rankMap[playerId]

    fun tinhDiemDuKien(playerId: Int): Int {
        val nhap = toNhap.value
        if (isSimpleInput) return nhap.manualScores[playerId]?.toIntOrNull() ?: 0
        if (nhap.anTrangIds.isNotEmpty()) return if (nhap.anTrangIds.contains(playerId)) 3 else 0
        if (nhap.denIds.isNotEmpty()) {
            if (nhap.denIds.contains(playerId)) return -6
            val winnerId = nhap.rankMap.entries.find { it.value == 1 }?.key
            return if (playerId == winnerId) 6 else 0
        }
        val rank = nhap.rankMap[playerId] ?: 4
        return luatDiem[rank] ?: 0
    }

    fun chotSoLuuVan() {
        val nhap = toNhap.value
        val hopLe = if (isSimpleInput) nhap.manualScores.values.any { (it.toIntOrNull() ?: 0) != 0 }
        else nhap.anTrangIds.isNotEmpty() || nhap.denIds.isNotEmpty() || nhap.rankMap.size == 4
        if (!hopLe) return
        val bienDongDiem = mutableMapOf<Int, Int>()
        players.forEach { p -> bienDongDiem[p.id] = tinhDiemDuKien(p.id) }
        luuVanVaoSo(bienDongDiem)
        toNhap.value = RoundDraft()
        saveData()
    }

    private fun luuVanVaoSo(diemMoi: Map<Int, Int>) {
        val vanMoi = GameRound(roundNumber = danhSachVanDau.size + 1, scoreChanges = diemMoi)
        danhSachVanDau.add(0, vanMoi)
        diemMoi.forEach { (id, diem) ->
            val index = players.indexOfFirst { it.id == id }
            if (index != -1) players[index] = players[index].copy(score = players[index].score + diem)
        }
        val limit = diemChamDich
        if (limit != null) {
            val winner = players.find { it.score >= limit }
            if (winner != null) nguoiThangCuoc.value = winner
        }
    }
}
