package com.gamebd.rpsduel

import androidx.lifecycle.SavedStateHandle
import com.gamebd.rpsduel.model.GamePhase
import com.gamebd.rpsduel.model.Move
import com.gamebd.rpsduel.viewmodel.GameViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GameViewModelTest {

    private lateinit var viewModel: GameViewModel

    @Before
    fun setUp() {
        viewModel = GameViewModel(SavedStateHandle())
    }

    @Test
    fun testMoveBeatsLogic() {
        // Rock beats Scissors
        assertEquals(true, Move.ROCK.beats(Move.SCISSORS))
        // Scissors beats Paper
        assertEquals(true, Move.SCISSORS.beats(Move.PAPER))
        // Paper beats Rock
        assertEquals(true, Move.PAPER.beats(Move.ROCK))

        // Reverse checks
        assertEquals(false, Move.SCISSORS.beats(Move.ROCK))
        assertEquals(false, Move.PAPER.beats(Move.SCISSORS))
        assertEquals(false, Move.ROCK.beats(Move.PAPER))

        // Same move is draw
        assertNull(Move.ROCK.beats(Move.ROCK))
        assertNull(Move.PAPER.beats(Move.PAPER))
        assertNull(Move.SCISSORS.beats(Move.SCISSORS))
    }

    @Test
    fun testMatchWinConditionBestOfThree() {
        viewModel.startGame("Alice", "Bob", totalRounds = 3)
        val p1Name = "Alice"

        // Round 1: Alice picks ROCK, Bob picks SCISSORS -> Alice wins round 1
        viewModel.selectP1Move(Move.ROCK)
        viewModel.readyP2()
        viewModel.selectP2Move(Move.SCISSORS)

        assertEquals(1, viewModel.state.value.p1Score)
        assertEquals(0, viewModel.state.value.p2Score)
        assertNull(viewModel.state.value.matchWinnerName)

        viewModel.nextRound()

        // Round 2: Alice picks PAPER, Bob picks ROCK -> Alice wins round 2 and match (2 wins required)
        viewModel.selectP1Move(Move.PAPER)
        viewModel.readyP2()
        viewModel.selectP2Move(Move.ROCK)

        assertEquals(2, viewModel.state.value.p1Score)
        assertEquals(p1Name, viewModel.state.value.matchWinnerName)

        viewModel.nextRound()
        assertEquals(GamePhase.MATCH_OVER, viewModel.state.value.phase)
    }

    @Test
    fun testDrawDoesNotCountAsRoundWin() {
        viewModel.startGame("Alice", "Bob", totalRounds = 3)

        // Round 1 Draw
        viewModel.selectP1Move(Move.ROCK)
        viewModel.readyP2()
        viewModel.selectP2Move(Move.ROCK)

        assertTrue(viewModel.state.value.isDraw)
        assertEquals(0, viewModel.state.value.p1Score)
        assertEquals(0, viewModel.state.value.p2Score)

        viewModel.nextRound()
        // Round should stay as Round 1 to replay
        assertEquals(1, viewModel.state.value.currentRound)
        assertEquals(GamePhase.P1_TURN, viewModel.state.value.phase)
    }

    @Test
    fun testGameReset() {
        viewModel.startGame("Alice", "Bob", totalRounds = 3)
        viewModel.selectP1Move(Move.ROCK)
        viewModel.readyP2()
        viewModel.selectP2Move(Move.SCISSORS)

        assertEquals(1, viewModel.state.value.p1Score)

        viewModel.resetMatch()

        val state = viewModel.state.value
        assertEquals(0, state.p1Score)
        assertEquals(0, state.p2Score)
        assertEquals(1, state.currentRound)
        assertNull(state.p1Move)
        assertNull(state.p2Move)
        assertNull(state.roundWinnerName)
        assertNull(state.matchWinnerName)
        assertEquals(GamePhase.P1_TURN, state.phase)
    }
}
