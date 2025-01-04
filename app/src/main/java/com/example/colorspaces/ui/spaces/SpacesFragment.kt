package com.example.colorspaces.ui.spaces

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.colorspaces.databinding.FragmentSpacesBinding

class SpacesFragment : Fragment() {

    private var _binding: FragmentSpacesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpacesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val hexGridView = binding.hexGridView
        val colorMixingSwitch = binding.colorMixingSwitch

        colorMixingSwitch.setOnCheckedChangeListener { _, isChecked ->
            hexGridView.isSubtractiveMixing = isChecked
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
