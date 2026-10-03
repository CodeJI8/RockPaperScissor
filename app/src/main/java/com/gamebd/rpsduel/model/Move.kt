package com.gamebd.rpsduel.model

enum class Move {
    ROCK,
    PAPER,
    SCISSORS;

    /**
     * Returns true if this move beats [other],
     * false if [other] beats this move,
     * or null if it's a draw.
     */
    fun beats(other: Move): Boolean? {
        if (this == other) return null
        return when (this) {
            ROCK -> other == SCISSORS
            PAPER -> other == ROCK
            SCISSORS -> other == PAPER
        }
    }
}
