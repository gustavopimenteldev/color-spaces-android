package com.gustavopimentel.colorspaces.ui.guess

import android.graphics.Color
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.gustavopimentel.colorspaces.R
import com.gustavopimentel.colorspaces.databinding.FragmentGuessBinding

class GuessFragment : Fragment() {

    private var _binding: FragmentGuessBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GuessViewModel by viewModels()
    private lateinit var guessGrid: List<List<EditText>>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGuessBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupGrid()
        setupObservers()

        binding.retryButton.setOnClickListener { viewModel.resetGame() }

        return root
    }

    private fun setupObservers() {
        viewModel.targetColor.observe(viewLifecycleOwner) { color ->
            binding.colorDisplay.setBackgroundColor(color)
        }

        viewModel.feedbackKey.observe(viewLifecycleOwner) { key ->
            val message = when (key) {
                "game_win" -> getString(R.string.game_win)
                "game_lose" -> getString(R.string.game_lose, viewModel.hexColor.value)
                "game_feedback_start" -> getString(R.string.game_feedback_start)
                else -> {""}
            }
            binding.feedbackText.text = message
        }

        viewModel.currentRow.observe(viewLifecycleOwner) { newRow ->
            if (newRow == 0) resetGridUI()
            updateRowColors(newRow)
        }

        viewModel.guessEvaluations.observe(viewLifecycleOwner) { evaluations ->
            updateCellBackgrounds(evaluations)
        }

        viewModel.gameOver.observe(viewLifecycleOwner) { isGameOver ->
            if (isGameOver) {
                binding.darkOverlay.visibility = View.VISIBLE
                binding.retryButton.visibility = View.VISIBLE
            } else {
                binding.darkOverlay.visibility = View.GONE
                binding.retryButton.visibility = View.GONE
            }
        }
    }

    private fun setupGrid() {
        val gridLayout = binding.guessGrid
        val editTextList = mutableListOf<List<EditText>>()

        for (row in 0 until 6) {
            val rowList = mutableListOf<EditText>()
            for (col in 0 until 3) {
                val cell = EditText(requireContext()).apply {
                    layoutParams = ViewGroup.MarginLayoutParams(120, 120).apply {
                        setMargins(8, 8, 8, 8)
                    }
                    setTextColor(Color.BLACK)
                    textSize = 20f
                    filters = arrayOf(
                        InputFilter.LengthFilter(2),
                        InputFilter { source, _, _, _, _, _ ->
                            source.toString()
                                .filter { it.isDigit() || it in 'A'..'F' || it in 'a'..'f' }
                                .uppercase()
                        }
                    )
                    inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                    setPadding(14, 14, 14, 14)
                    gravity = android.view.Gravity.CENTER
                    isEnabled = row == 0
                    background = ContextCompat.getDrawable(
                        requireContext(),
                        if (row == 0) R.drawable.cell_active else R.drawable.cell_future
                    )
                }
                rowList.add(cell)
                gridLayout.addView(cell)
            }
            editTextList.add(rowList)
        }
        guessGrid = editTextList
        setupAutoMove()
    }

    private fun setupAutoMove() {
        guessGrid.forEach { row ->
            row.forEachIndexed { index, cell ->
                cell.setOnKeyListener { _, _, _ ->
                    if (cell.text.length == 2 && index < 2) {
                        row[index + 1].requestFocus()
                    } else if (cell.text.length == 2 && index == 2) {
                        submitGuess()
                    }
                    false
                }
            }
        }
    }

    private fun submitGuess() {
        val (r, g, b) = guessGrid[viewModel.currentRow.value ?: 0].map {
            it.text.toString().toIntOrNull(16) ?: return
        }
        viewModel.processGuess(Color.rgb(r, g, b))
    }

    private fun updateRowColors(newRow: Int) {
        if (newRow > 0) {
            guessGrid[newRow - 1].forEach { it.isEnabled = false }
        }
        guessGrid.getOrNull(newRow)?.forEach {
            it.background = ContextCompat.getDrawable(requireContext(), R.drawable.cell_active)
            it.isEnabled = true
        }
    }

    private fun updateCellBackgrounds(evaluations: List<List<String>>) {
        evaluations.forEachIndexed { rowIndex, rowEvaluation ->
            rowEvaluation.forEachIndexed { colIndex, evaluation ->
                val cell = guessGrid[rowIndex][colIndex]
                cell.background = ContextCompat.getDrawable(
                    requireContext(),
                    when (evaluation) {
                        "✔" -> R.drawable.cell_correct
                        "⬆" -> R.drawable.cell_lower
                        "⬇" -> R.drawable.cell_higher
                        else -> R.drawable.cell_future
                    }
                )
            }
        }
    }

    private fun resetGridUI() {
        guessGrid.forEachIndexed { rowIndex, row ->
            row.forEach { cell ->
                cell.text.clear()
                cell.isEnabled = rowIndex == 0
                cell.background = ContextCompat.getDrawable(
                    requireContext(),
                    if (rowIndex == 0) R.drawable.cell_active else R.drawable.cell_future
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
