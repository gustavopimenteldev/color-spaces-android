package com.gustavopimentel.colorspaces.ui.lights.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope

@Composable
fun SphereCanvas(
    sphereColor: Color,
    leftLightColor: Color,
    rightLightColor: Color
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2.5f

        val leftActualColor = multiplyColors(leftLightColor, sphereColor)
        val rightActualColor = multiplyColors(rightLightColor, sphereColor)

        drawCircle(
            color = Color.Black,
            center = center,
            radius = radius
        )

        drawRadialGradientLight(center, radius, leftActualColor, Offset((center.x - radius / 1.7F), center.y - radius / 1.7F))

        if (rightLightColor != Color.Transparent) {
            drawRadialGradientLight(center, radius, rightActualColor, Offset((center.x + radius / 1.7F), center.y - radius / 1.7F))
        }
    }
}

fun DrawScope.drawRadialGradientLight(center: Offset, radius: Float, lightColor: Color, lightSource: Offset) {
    val shader = Brush.radialGradient(
        colors = listOf(lightColor, Color.Transparent),
        center = lightSource,
        radius = radius * 1.7f
    )
    drawCircle(brush = shader, center = center, radius = radius, blendMode = BlendMode.Plus)
}

fun multiplyColors(color1: Color, color2: Color): Color {
    val red = ((color1.red * color2.red) * 255).toInt()
    val green = ((color1.green * color2.green) * 255).toInt()
    val blue = ((color1.blue * color2.blue) * 255).toInt()

    return Color(red / 255f, green / 255f, blue / 255f, 1f)
}

