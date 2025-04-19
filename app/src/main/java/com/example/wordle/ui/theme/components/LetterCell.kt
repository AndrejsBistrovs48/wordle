package com.example.wordle.ui.components

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.example.wordle.R
import com.example.wordle.model.LetterState

class LetterCell @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()
    private var circleRadius = 0f
    private var centerX = 0f
    private var centerY = 0f
    private var currentState = LetterState.UNKNOWN

    init {
        setWillNotDraw(false)
        gravity = android.view.Gravity.CENTER
        setTextSize(20f)
        setTextColor(Color.WHITE)

        // Configure shadow (depth effect)
        shadowPaint.color = Color.parseColor("#40000000")
        shadowPaint.maskFilter = BlurMaskFilter(6f, BlurMaskFilter.Blur.NORMAL)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        circleRadius = (minOf(width, height) / 2 * 0.9f)
        centerX = width / 2f
        centerY = height / 2f

        path.reset()
        path.addCircle(centerX, centerY, circleRadius, Path.Direction.CW)
    }

    override fun onDraw(canvas: Canvas) {
        // Draw shadow (depth effect)
        canvas.drawCircle(centerX, centerY + 3f, circleRadius, shadowPaint)

        // Draw main circle with state color
        paint.color = getColorForState()
        canvas.drawPath(path, paint)

        // Draw text
        super.onDraw(canvas)
    }

    fun setLetter(letter: Char, state: LetterState) {
        text = letter.toString()
        currentState = state
        invalidate()
    }

    private fun getColorForState(): Int {
        return ContextCompat.getColor(context, when (currentState) {
            LetterState.CORRECT -> R.color.correct
            LetterState.WRONG_POSITION -> R.color.wrong_position
            LetterState.INCORRECT -> R.color.incorrect
            LetterState.UNKNOWN -> R.color.unknown
        })
    }
}