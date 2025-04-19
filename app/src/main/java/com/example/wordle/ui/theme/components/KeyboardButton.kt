package com.example.wordle.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.wordle.R
import com.example.wordle.model.LetterState

class KeyboardButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var onLetterClicked: ((Char) -> Unit)? = null

    init {
        val keyboardLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
        }

        val rows = listOf("QWERTYUIOP", "ASDFGHJKL", "ZXCVBNM")

        rows.forEach { rowLetters ->
            val rowLayout = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER
            }

            rowLetters.forEach { letter ->
                val button = Button(context).apply {
                    text = letter.toString()
                    setOnClickListener { onLetterClicked?.invoke(letter) }
                    layoutParams = LinearLayout.LayoutParams(
                        resources.getDimensionPixelSize(R.dimen.key_width),
                        resources.getDimensionPixelSize(R.dimen.key_height)
                    ).apply { setMargins(2, 2, 2, 2) }
                }
                rowLayout.addView(button)
            }
            keyboardLayout.addView(rowLayout)
        }
        addView(keyboardLayout)
    }

    fun setOnLetterClickListener(listener: (Char) -> Unit) {
        onLetterClicked = listener
    }

    fun updateKeyboardState(keyboardState: Map<Char, LetterState>) {
        // Implement color updates based on LetterState
    }
}