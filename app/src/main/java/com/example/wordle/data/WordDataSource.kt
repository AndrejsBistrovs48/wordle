// PROMPT: "Create a Kotlin class WordDataSource that reads 5-letter words from
// res/raw/wordbank.txt, filters for 5-letter words, converts to uppercase,
// and returns a random word. Include error handling with a fallback word list."
package com.example.wordle.data

import android.content.Context
import com.example.wordle.R
import java.io.InputStream

class WordDataSource(private val context: Context) {

    fun getRandomWord(): String {
        return try {
            val inputStream: InputStream = context.resources.openRawResource(R.raw.wordbank)
            val words = inputStream.bufferedReader().useLines { lines ->
                lines.filter { it.length == 5 } // Only 5-letter words
                    .map { it.uppercase() }
                    .toList()
            }
            words.random()
        } catch (e: Exception) {
            // Fallback to hardcoded list if file reading fails
            listOf("APPLE", "BRAIN", "CHAIR", "DANCE", "EARTH").random()
        }
    }
}