package com.gamebd.rpsduel.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.gamebd.rpsduel.model.GamePhase
import com.gamebd.rpsduel.model.GameState
import com.gamebd.rpsduel.model.Move
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel(
    private val savedStateHandle: SavedStateHandle = SavedStateHandle()
) : ViewModel() {

    companion object {
        private const val KEY_P1_NAME = "p1_name"
        private const val KEY_P2_NAME = "p2_name"
        private const val KEY_P1_SCORE = "p1_score"
        private const val KEY_P2_SCORE = "p2_score"
        private const val KEY_CURRENT_ROUND = "current_round"
        private const val KEY_TOTAL_ROUNDS = "total_rounds"
        private const val KEY_PHASE = "phase"
        private const val KEY_P1_MOVE = "p1_move"
        private const val KEY_P2_MOVE = "p2_move"
        private const val KEY_ROUND_WINNER = "round_winner"
        private const val KEY_IS_DRAW = "is_draw"
        private const val KEY_MATCH_WINNER = "match_winner"
    }

    private val _state = MutableStateFlow(
        GameState(
            p1Name = savedStateHandle[KEY_P1_NAME] ?: "Player 1",
            p2Name = savedStateHandle[KEY_P2_NAME] ?: "Player 2",
            p1Score = savedStateHandle[KEY_P1_SCORE] ?: 0,
            p2Score = savedStateHandle[KEY_P2_SCORE] ?: 0,
            currentRound = savedStateHandle[KEY_CURRENT_ROUND] ?: 1,
            totalRounds = savedStateHandle[KEY_TOTAL_ROUNDS] ?: 3,
            phase = try { GamePhase.valueOf(savedStateHandle[KEY_PHASE] ?: "") } catch (e: Exception) { GamePhase.P1_TURN },
            p1Move = try { Move.valueOf(savedStateHandle[KEY_P1_MOVE] ?: "") } catch (e: Exception) { null },
            p2Move = try { Move.valueOf(savedStateHandle[KEY_P2_MOVE] ?: "") } catch (e: Exception) { null },
            roundWinnerName = savedStateHandle[KEY_ROUND_WINNER],
            isDraw = savedStateHandle[KEY_IS_DRAW] ?: false,
            matchWinnerName = savedStateHandle[KEY_MATCH_WINNER]
        )
    )
    val state: StateFlow<GameState> = _state.asStateFlow()

    private fun updateState(newState: GameState) {
        _state.value = newState
        savedStateHandle[KEY_P1_NAME] = newState.p1Name
        savedStateHandle[KEY_P2_NAME] = newState.p2Name
        savedStateHandle[KEY_P1_SCORE] = newState.p1Score
        savedStateHandle[KEY_P2_SCORE] = newState.p2Score
        savedStateHandle[KEY_CURRENT_ROUND] = newState.currentRound
        savedStateHandle[KEY_TOTAL_ROUNDS] = newState.totalRounds
        savedStateHandle[KEY_PHASE] = newState.phase.name
        savedStateHandle[KEY_P1_MOVE] = newState.p1Move?.name ?: ""
        savedStateHandle[KEY_P2_MOVE] = newState.p2Move?.name ?: ""
        savedStateHandle[KEY_ROUND_WINNER] = newState.roundWinnerName ?: ""
        savedStateHandle[KEY_IS_DRAW] = newState.isDraw
        savedStateHandle[KEY_MATCH_WINNER] = newState.matchWinnerName ?: ""
    }

    fun startGame(p1Name: String, p2Name: String, totalRounds: Int) {
        val formattedP1 = p1Name.trim().ifEmpty { "Player 1" }.take(12)
        val formattedP2 = p2Name.trim().ifEmpty { "Player 2" }.take(12)

        updateState(
            GameState(
                p1Name = formattedP1,
                p2Name = formattedP2,
                p1Score = 0,
                p2Score = 0,
                currentRound = 1,
                totalRounds = totalRounds,
                phase = GamePhase.P1_TURN,
                p1Move = null,
                p2Move = null,
                roundWinnerName = null,
                isDraw = false,
                matchWinnerName = null
            )
        )
    }

    fun selectP1Move(move: Move) {
        if (state.value.phase != GamePhase.P1_TURN) return
        updateState(
            state.value.copy(
                p1Move = move,
                phase = GamePhase.PASS_TO_P2
            )
        )
    }

    fun readyP2() {
        if (state.value.phase != GamePhase.PASS_TO_P2) return
        updateState(
            state.value.copy(
                phase = GamePhase.P2_TURN
            )
        )
    }

    fun selectP2Move(move: Move) {
        if (state.value.phase != GamePhase.P2_TURN) return
        val p1Move = state.value.p1Move ?: return
        val outcome = p1Move.beats(move)

        var newP1Score = state.value.p1Score
        var newP2Score = state.value.p2Score
        var roundWinner: String? = null
        var isDraw = false

        when (outcome) {
            true -> {
                newP1Score += 1
                roundWinner = state.value.p1Name
            }
            false -> {
                newP2Score += 1
                roundWinner = state.value.p2Name
            }
            null -> {
                isDraw = true
            }
        }

        val required = state.value.requiredWins
        val matchWinner = when {
            newP1Score >= required -> state.value.p1Name
            newP2Score >= required -> state.value.p2Name
            else -> null
        }

        updateState(
            state.value.copy(
                p2Move = move,
                p1Score = newP1Score,
                p2Score = newP2Score,
                roundWinnerName = roundWinner,
                isDraw = isDraw,
                matchWinnerName = matchWinner,
                phase = GamePhase.REVEAL
            )
        )
    }

    fun nextRound() {
        if (state.value.phase != GamePhase.REVEAL) return
        if (state.value.matchWinnerName != null) {
            updateState(state.value.copy(phase = GamePhase.MATCH_OVER))
        } else {
            val nextRoundNum = if (state.value.isDraw) {
                state.value.currentRound // Draw replays round
            } else {
                state.value.currentRound + 1
            }
            updateState(
                state.value.copy(
                    currentRound = nextRoundNum,
                    p1Move = null,
                    p2Move = null,
                    roundWinnerName = null,
                    isDraw = false,
                    phase = GamePhase.P1_TURN
                )
            )
        }
    }

    fun resetMatch() {
        updateState(
            state.value.copy(
                p1Score = 0,
                p2Score = 0,
                currentRound = 1,
                p1Move = null,
                p2Move = null,
                roundWinnerName = null,
                isDraw = false,
                matchWinnerName = null,
                phase = GamePhase.P1_TURN
            )
        )
    }

    fun resetToHome() {
        updateState(GameState())
    }
}
