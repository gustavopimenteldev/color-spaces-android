package com.example.colorspaces.ui.palettes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.colorspaces.databinding.FragmentPalettesBinding

class PalettesFragment : Fragment() {

    private var _binding: FragmentPalettesBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val palettesViewModel =
            ViewModelProvider(this).get(PalettesViewModel::class.java)

        _binding = FragmentPalettesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textPalettes
        palettesViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}