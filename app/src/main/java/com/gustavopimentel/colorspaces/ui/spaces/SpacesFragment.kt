package com.gustavopimentel.colorspaces.ui.spaces

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.gustavopimentel.colorspaces.databinding.FragmentSpacesBinding
import com.gustavopimentel.colorspaces.util.ColorUtils.getContrastColor

class SpacesFragment : Fragment(), StateChangeListener {

    private var _binding: FragmentSpacesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SpacesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpacesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val hexGridView = binding.hexGridView
        hexGridView.stateChangeListener = this

        val colorMixingSwitch = binding.colorMixingSwitch
        val savedStatesContainer = binding.savedStatesContainer

        setupObservers(hexGridView, colorMixingSwitch, savedStatesContainer)

        return root
    }

    private fun setupObservers(
        hexGridView: HexGridView,
        colorMixingSwitch: SwitchCompat,
        savedStatesContainer: GridLayout
    ) {
        viewModel.savedStates.observe(viewLifecycleOwner) { states ->
            savedStatesContainer.removeAllViews()

            states.forEachIndexed { index, state ->
                val button = Button(requireContext()).apply {
                    updateButtonAppearance(this, state)
                    setOnClickListener { viewModel.switchToState(index) }

                    layoutParams = LinearLayout.LayoutParams(
                        120,
                        120
                    ).apply {
                        marginStart = 25
                        marginEnd = 25
                        bottomMargin = 40
                    }
                }
                savedStatesContainer.addView(button)
            }
        }

        viewModel.activeStateIndex.observe(viewLifecycleOwner, Observer { index ->
            val states = viewModel.savedStates.value ?: return@Observer
            val newState = states[index]

            hexGridView.cornerColors.clear()
            hexGridView.cornerColors.addAll(newState.cornerColors)
            hexGridView.isSubtractiveMixing = newState.isSubtractiveMixing
            colorMixingSwitch.isChecked = newState.isSubtractiveMixing

            hexGridView.invalidate()
        })

        colorMixingSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateActiveStateMixing(isChecked)
            hexGridView.isSubtractiveMixing = isChecked
            hexGridView.invalidate()
        }
    }

    override fun onStateChanged() {
        viewModel.updateActiveStateColors(
            binding.hexGridView.cornerColors,
            binding.hexGridView.isSubtractiveMixing
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateButtonAppearance(button: Button, state: SpacesViewModel.SavedState) {
        button.text = if (state.isSubtractiveMixing) "-" else "+"
        button.background = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            state.cornerColors.toIntArray()
        ).apply {
            cornerRadius = 23f
        }
        button.setTextColor(getContrastColor(state.cornerColors[1]))
    }
}
