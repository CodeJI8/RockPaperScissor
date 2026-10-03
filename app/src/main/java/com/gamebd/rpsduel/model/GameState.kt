package com.gamebd.rpsduel.model

data class GameState(
    val p1Name: String = "Player 1",
    val p2Name: String = "Player 2",
    val p1Score: Int = 0,
    val p2Score: Int = 0,
    val currentRound: Int = 1,
    val totalRounds: Int = 3,
    val phase: GamePhase = GamePhase.P1_TURN,
    val p1Move: Move? = null,
    val p2Move: Move? = null,
    val roundWinnerName: String? = null,
    val isDraw: Boolean = false,
    val matchWinnerName: String? = null
) {
    val requiredWins: Int
        get() = (totalRounds / 2) + 1
}
