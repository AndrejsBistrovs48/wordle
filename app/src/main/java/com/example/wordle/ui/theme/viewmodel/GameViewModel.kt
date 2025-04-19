package com.example.wordle.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordle.data.GameRepository
import com.example.wordle.model.GameState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GameViewModel(private val repository: GameRepository) : ViewModel() {
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState

    init {
        startNewGame()
    }

    // In GameViewModel.kt
    fun onLetterClicked(letter: Char) {
        if (_gameState.value.currentGuess.length < 5) {
            _gameState.value = _gameState.value.copy(
                currentGuess = _gameState.value.currentGuess + letter
            )
        }
    }

    fun onDeleteClicked() {
        _gameState.value = _gameState.value.copy(
            currentGuess = _gameState.value.currentGuess.dropLast(1)
        )
    }

    fun onSubmitClicked() {
        viewModelScope.launch {
            repository.submitGuess(_gameState.value.currentGuess)
            _gameState.value = repository.getGameState()
        }
    }

    // Add this function to reset the game
    fun resetGame() {
        viewModelScope.launch {
            repository.initializeGame()
            _gameState.value = repository.getGameState()
        }
    }

    // Helper function for initial game setup
    private fun startNewGame() {
        viewModelScope.launch {
            repository.initializeGame()
            _gameState.value = repository.getGameState()
        }
    }
}