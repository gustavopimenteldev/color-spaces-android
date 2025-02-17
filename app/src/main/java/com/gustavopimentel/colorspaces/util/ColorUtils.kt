package com.gustavopimentel.colorspaces.util

import android.graphics.Color

object ColorUtils {

    fun calculateBrightness(color: Int): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        return (hsv[2] * 100).toInt()
    }

    fun getHue(color: Int): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        return hsv[0].toInt()
    }

    fun getSaturation(color: Int): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        return (hsv[1] * 100).toInt()
    }

    fun calculateLuminosity(color: Int): Int {
        val red = Color.red(color) / 255f
        val green = Color.green(color) / 255f
        val blue = Color.blue(color) / 255f
        val luminosity = (0.2126 * red + 0.7152 * green + 0.0722 * blue) * 100
        return luminosity.toInt()
    }

    fun getHexCode(color: Int): String {
        return String.format("#%06X", 0xFFFFFF and color)
    }

    fun getContrastColor(color: Int): Int {
        return if (calculateLuminosity(color) > 50) Color.BLACK else Color.WHITE
    }
}
