package com.gustavopimentel.colorspaces.ui.lights

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.gustavopimentel.colorspaces.ui.lights.components.SphereLightingScreen

class LightsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                SphereLightingScreen()
            }
        }
    }
}
