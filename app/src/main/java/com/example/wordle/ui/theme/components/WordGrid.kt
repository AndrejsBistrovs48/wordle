package com.example.wordle.ui.components

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.wordle.R
import com.example.wordle.model.GameState
import com.example.wordle.model.LetterState

class WordGrid @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val letterCells = Array(6) { row ->
        Array(5) { col ->
            LetterCell(context).apply {
                id = android.view.View.generateViewId()
            }
        }
    }

    init {
        val gridLayout = GridLayout(context).apply {
            columnCount = 5
            rowCount = 6
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            // Add padding to ensure all 6 rows fit
            setPadding(
                resources.getDimensionPixelSize(R.dimen.grid_padding),
                resources.getDimensionPixelSize(R.dimen.grid_padding),
                resources.getDimensionPixelSize(R.dimen.grid_padding),
                resources.getDimensionPixelSize(R.dimen.grid_padding)
            )
            // Remove red background
            setBackgroundColor(Color.TRANSPARENT)
        }

        for (row in 0 until 6) {
            for (col in 0 until 5) {
                val cell = letterCells[row][col]
                val params = GridLayout.LayoutParams().apply {
                    width = resources.getDimensionPixelSize(R.dimen.cell_size)
                    height = resources.getDimensionPixelSize(R.dimen.cell_size)
                    setGravity(Gravity.CENTER)

                    columnSpec = GridLayout.spec(col)
                    rowSpec = GridLayout.spec(row)
                    setMargins(
                        resources.getDimensionPixelSize(R.dimen.cell_margin),
                        resources.getDimensionPixelSize(R.dimen.cell_margin),
                        resources.getDimensionPixelSize(R.dimen.cell_margin),
                        resources.getDimensionPixelSize(R.dimen.cell_margin)
                    )
                }
                gridLayout.addView(cell, params)
            }
        }

        // Add red border around the entire grid
        background = ContextCompat.getDrawable(context, R.drawable.grid_border)
        addView(gridLayout)
    }

    fun updateGameState(state: GameState) {
        for (row in 0 until 6) {
            for (col in 0 until 5) {
                val cell = letterCells[row][col]
                when {
                    row < state.guesses.size -> {
                        val guess = state.guesses[row]
                        cell.setLetter(
                            guess.word.getOrNull(col) ?: ' ',
                            guess.letterStates.getOrNull(col) ?: LetterState.UNKNOWN
                        )
                    }
                    row == state.guesses.size -> {
                        cell.setLetter(
                            state.currentGuess.getOrNull(col) ?: ' ',
                            LetterState.UNKNOWN
                        )
                    }
                    else -> {
                        cell.setLetter(' ', LetterState.UNKNOWN)
                    }
                }
            }
        }
    }

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()
}