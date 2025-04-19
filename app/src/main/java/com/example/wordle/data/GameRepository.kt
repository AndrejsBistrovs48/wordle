// PROMPT: "Create a GameRepository that:
// - Uses WordDataSource
// - Manages game state (target word, guesses)
// - Validates word submissions
// - Calculates letter states (correct/position/incorrect)
// - Updates keyboard state
// - Handles game reset
package com.example.wordle.data

import com.example.wordle.model.GameState
import com.example.wordle.model.GameStatus
import com.example.wordle.model.Guess
import com.example.wordle.model.LetterState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GameRepository(private val dataSource: WordDataSource) {
    private var gameState = GameState()

    suspend fun initializeGame() {
        gameState = gameState.copy(
            targetWord = dataSource.getRandomWord().uppercase(),
            guesses = emptyList(),
            currentGuess = "",
            gameStatus = GameStatus.ONGOING,
            keyboardState = emptyMap()
        )
    }

    suspend fun submitGuess(guess: String): Boolean {
        if (guess.length != 5) return false

        val letterStates = guess.mapIndexed { index, c ->
            when {
                gameState.targetWord[index] == c -> LetterState.CORRECT
                gameState.targetWord.contains(c) -> LetterState.WRONG_POSITION
                else -> LetterState.INCORRECT
            }
        }

        val newGuess = Guess(guess, letterStates)
        val updatedGuesses = gameState.guesses + newGuess

        val gameStatus = when {
            guess == gameState.targetWord -> GameStatus.WON
            updatedGuesses.size >= 6 -> GameStatus.LOST
            else -> GameStatus.ONGOING
        }

        val updatedKeyboardState = gameState.keyboardState.toMutableMap()
        guess.forEachIndexed { index, c ->
            val currentState = updatedKeyboardState[c] ?: LetterState.UNKNOWN
            val newState = letterStates[index]
            if (currentState != LetterState.CORRECT) {
                updatedKeyboardState[c] = newState
            }
        }

        gameState = gameState.copy(
            guesses = updatedGuesses,
            currentGuess = "",
            gameStatus = gameStatus,
            keyboardState = updatedKeyboardState
        )

        return true
    }

    fun getGameState(): GameState = gameState
}