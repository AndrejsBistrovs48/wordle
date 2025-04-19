package com.example.wordle.model

data class GameState(
    val targetWord: String = "",
    val guesses: List<Guess> = emptyList(),
    val currentGuess: String = "",
    val gameStatus: GameStatus = GameStatus.ONGOING,
    val keyboardState: Map<Char, LetterState> = emptyMap()
)

data class Guess(
    val word: String,
    val letterStates: List<LetterState>
)

enum class LetterState {
    CORRECT, WRONG_POSITION, INCORRECT, UNKNOWN
}

enum class GameStatus {
    ONGOING, WON, LOST
}