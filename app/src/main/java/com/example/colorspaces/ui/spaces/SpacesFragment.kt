package com.example.colorspaces.ui.spaces

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.colorspaces.databinding.FragmentSpacesBinding

class SpacesFragment : Fragment() {

    private var _binding: FragmentSpacesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val guessViewModel =
            ViewModelProvider(this).get(SpacesViewModel::class.java)

        _binding = FragmentSpacesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSpaces
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