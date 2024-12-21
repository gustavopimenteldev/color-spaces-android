package com.example.colorspaces.ui.guess

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.colorspaces.databinding.FragmentGuessBinding

class GuessFragment : Fragment() {

    private var _binding: FragmentGuessBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val guessViewModel =
            ViewModelProvider(this).get(GuessViewModel::class.java)

        _binding = FragmentGuessBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textGuess
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