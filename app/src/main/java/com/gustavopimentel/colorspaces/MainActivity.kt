package com.gustavopimentel.colorspaces

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.gustavopimentel.colorspaces.databinding.ActivityMainBinding
import com.gustavopimentel.colorspaces.util.ColorUtils

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
                R.id.navigation_spaces, R.id.navigation_lights,
                R.id.navigation_guess, R.id.navigation_about
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun updateInfoColor(color: Int) {
        val context = binding.colorInfoView.context

        binding.colorInfoView.findViewById<View>(R.id.color_square).setBackgroundColor(color)

        binding.colorInfoView.findViewById<TextView>(R.id.tv_hex_code).text =
            ColorUtils.getHexCode(color)

        binding.colorInfoView.findViewById<TextView>(R.id.tv_hue).text =
            context.getString(R.string.hue, ColorUtils.getHue(color))

        binding.colorInfoView.findViewById<TextView>(R.id.tv_saturation).text =
            context.getString(R.string.saturation, ColorUtils.getSaturation(color))

        binding.colorInfoView.findViewById<TextView>(R.id.tv_brightness).text =
            context.getString(R.string.brightness, ColorUtils.calculateBrightness(color))

        binding.colorInfoView.findViewById<TextView>(R.id.tv_luminosity).text =
            context.getString(R.string.luminosity, ColorUtils.calculateLuminosity(color))
    }

}
