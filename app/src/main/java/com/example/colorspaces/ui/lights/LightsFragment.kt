package com.example.colorspaces.ui.lights

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.colorspaces.databinding.FragmentLightsBinding

class LightsFragment : Fragment() {

    private var _binding: FragmentLightsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val guessViewModel =
            ViewModelProvider(this).get(LightsViewModel::class.java)

        _binding = FragmentLightsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textLights
        guessViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}