package com.gustavopimentel.colorspaces.ui.guess

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class GuessViewModel : ViewModel() {

    private val _targetColor = MutableLiveData(generateRandomColor())
    val targetColor: LiveData<Int> = _targetColor

    private val _attemptsLeft = MutableLiveData(6)

    private val _currentRow = MutableLiveData(0)
    val currentRow: LiveData<Int> = _currentRow

    private val _feedbackKey = MutableLiveData("game_feedback_start")  // Stores string key
    val feedbackKey: LiveData<String> = _feedbackKey

    private val _gameOver = MutableLiveData(false)
    val gameOver: LiveData<Boolean> = _gameOver

    private val _hexColor = MutableLiveData("")
    val hexColor: LiveData<String> = _hexColor

    private val _guessEvaluations = MutableLiveData<MutableList<List<String>>>(mutableListOf())
    val guessEvaluations: LiveData<MutableList<List<String>>> = _guessEvaluations

    fun processGuess(guessColor: Int) {
        val targetRed = Color.red(_targetColor.value!!)
        val targetGreen = Color.green(_targetColor.value!!)
        val targetBlue = Color.blue(_targetColor.value!!)

        val guessRed = Color.red(guessColor)
        val guessGreen = Color.green(guessColor)
        val guessBlue = Color.blue(guessColor)

        val rowEvaluation = listOf(
            getArrowFeedback(guessRed, targetRed),
            getArrowFeedback(guessGreen, targetGreen),
            getArrowFeedback(guessBlue, targetBlue)
        )

        val currentEvaluations = _guessEvaluations.value ?: mutableListOf()
        currentEvaluations.add(rowEvaluation)
        _guessEvaluations.value = currentEvaluations

        _attemptsLeft.value = (_attemptsLeft.value ?: 6) - 1
        _currentRow.value = (_currentRow.value ?: 0) + 1

        if (guessColor == _targetColor.value || _attemptsLeft.value == 0) {
            endGame(guessColor == _targetColor.value)
        }
    }

    private fun getArrowFeedback(guess: Int, target: Int): String {
        return when {
            guess < target -> "⬆"
            guess > target -> "⬇"
            else -> "✔"
        }
    }

    private fun endGame(isWin: Boolean) {
        val hexColor = String.format("#%06X", 0xFFFFFF and _targetColor.value!!)
        _hexColor.value = hexColor

        _feedbackKey.value = if (isWin) "game_win" else "game_lose"
        _gameOver.value = true
    }

    fun resetGame() {
        _targetColor.value = generateRandomColor()
        _attemptsLeft.value = 6
        _currentRow.value = 0
        _feedbackKey.value = "game_feedback_start"
        _gameOver.value = false
        _guessEvaluations.value = mutableListOf()
    }

    private fun generateRandomColor(): Int {
        return Color.rgb(
            Random.nextInt(256),
            Random.nextInt(256),
            Random.nextInt(256)
        )
    }
}
