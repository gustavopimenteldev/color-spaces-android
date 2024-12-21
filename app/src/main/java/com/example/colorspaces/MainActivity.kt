package com.example.colorspaces

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.colorspaces.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_spaces, R.id.navigation_palettes, R.id.navigation_lights,
                R.id.navigation_guess, R.id.navigation_learn
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun updateInfoColor(color: Int) {
        binding.colorInfoView.findViewById<View>(R.id.color_square).setBackgroundColor(color)

        val hexCode = String.format("#%06X", 0xFFFFFF and color)
        binding.colorInfoView.findViewById<TextView>(R.id.hex_code_text).text = hexCode
    }
}
