// PROMPT: "Create MainActivity that:
// - Handles keyboard button clicks
// - Observes game state updates
// - Shows win/lose dialogs (non-transparent)
// - Has reset functionality"
package com.example.wordle

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.wordle.databinding.ActivityMainBinding
import com.example.wordle.data.GameRepository
import com.example.wordle.data.WordDataSource
import com.example.wordle.model.GameStatus
import com.example.wordle.ui.viewmodel.GameViewModel
import com.example.wordle.ui.viewmodel.GameViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = GameRepository(WordDataSource(this.applicationContext))
        viewModel = ViewModelProvider(this, GameViewModelFactory(repository))
            .get(GameViewModel::class.java)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        binding.keyboard.setOnLetterClickListener { letter ->
            viewModel.onLetterClicked(letter)
        }

        binding.deleteButton.setOnClickListener {
            viewModel.onDeleteClicked()
        }

        binding.submitButton.setOnClickListener {
            viewModel.onSubmitClicked()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.gameState.collect { state ->
                binding.wordGrid.updateGameState(state)
                binding.keyboard.updateKeyboardState(state.keyboardState)

                when (state.gameStatus) {
                    GameStatus.WON -> showResultDialog(true, state.targetWord)
                    GameStatus.LOST -> showResultDialog(false, state.targetWord)
                    GameStatus.ONGOING -> Unit
                }
            }
        }
    }

    private fun showResultDialog(isWin: Boolean, targetWord: String) {
        val dialog = AlertDialog.Builder(this).apply {
            setTitle(if (isWin) "You Won!" else "Game Over")
            setMessage("The word was: $targetWord")
            setPositiveButton("Play Again") { _, _ ->
                lifecycleScope.launch {
                    viewModel.resetGame()
                }
            }
            setCancelable(false)
        }.create()

        // Make dialog non-transparent with white background
        dialog.window?.apply {
            // Set solid white background
            setBackgroundDrawable(ColorDrawable(Color.WHITE))
            // Add some padding
            setLayout((resources.displayMetrics.widthPixels * 0.9).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
            // Dim the background
            setDimAmount(0.7f)
        }

        // Customize positive button text color
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(ContextCompat.getColor(this, R.color.correct))
        }

        dialog.show()
    }
}