package com.gustavopimentel.colorspaces.ui.lights.components

import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.gustavopimentel.colorspaces.MainActivity
import com.gustavopimentel.colorspaces.R
import com.gustavopimentel.colorspaces.ui.colorpicker.ColorPicker
import com.gustavopimentel.colorspaces.ui.colorpicker.HueSlider
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun SphereLightingScreen() {
    var sphereColor by remember { mutableStateOf(Color.White) }
    var leftLightColor by remember { mutableStateOf(Color.White) }
    var rightLightColor by remember { mutableStateOf(Color.White) }
    var isRightLightOn by remember { mutableStateOf(true) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .background(colorResource(id = R.color.dark))
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_lights_24),
                contentDescription = "Left Light",
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(leftLightColor),
                modifier = Modifier
                    .size(60.dp)
                    .padding(12.dp)
                    .align(Alignment.TopStart)
                    .clickable { showColorPickerDialog(context) { newColor -> leftLightColor = Color(newColor) } }
            )

            if (isRightLightOn) {
                Image(
                    painter = painterResource(id = R.drawable.ic_lights_24),
                    contentDescription = "Right Light",
                    colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(rightLightColor),
                    modifier = Modifier
                        .size(60.dp)
                        .padding(12.dp)
                        .align(Alignment.TopEnd)
                        .clickable { showColorPickerDialog(context) { newColor -> rightLightColor = Color(newColor) } }
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .pointerInput(Unit) {
                    detectTapGestures { touchPosition ->
                        val sphereCenter = Offset(size.width / 2f, size.height / 2f)
                        val sphereRadius = minOf(size.width, size.height) / 2.5f

                        val leftGradientCenter = Offset((sphereCenter.x - sphereRadius / 1.7f), sphereCenter.y - sphereRadius / 1.7f)
                        val rightGradientCenter = Offset((sphereCenter.x + sphereRadius / 1.7f), sphereCenter.y - sphereRadius / 1.7f)

                        val pickedColor = estimateColorAtPoint(
                            touchPosition = touchPosition,
                            sphereRadius = sphereRadius,
                            leftGradientCenter = leftGradientCenter,
                            leftLightColor = leftLightColor,
                            rightGradientCenter = if (isRightLightOn) rightGradientCenter else null,
                            rightLightColor = if (isRightLightOn) rightLightColor else null,
                            sphereColor = sphereColor,
                            isRightLightOn = isRightLightOn
                        ).toArgb()

                        (context as MainActivity).updateInfoColor(pickedColor)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            SphereCanvas(
                sphereColor = sphereColor,
                leftLightColor = leftLightColor,
                rightLightColor = if (isRightLightOn) rightLightColor else Color.Transparent
            )
        }

        Button(
            onClick = { showColorPickerDialog(context) { newColor -> sphereColor = Color(newColor) } },
            colors = ButtonDefaults.buttonColors(
                containerColor = sphereColor,
                contentColor = if (sphereColor.luminance() > 0.5f) Color.Black else Color.White
            ),
            elevation = ButtonDefaults.elevatedButtonElevation(8.dp),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Sphere Color",
                style = MaterialTheme.typography.bodyLarge,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Right Light",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(end = 8.dp)
            )

            Switch(
                checked = isRightLightOn,
                onCheckedChange = { isRightLightOn = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = rightLightColor,
                    uncheckedThumbColor = Color.Gray,
                    checkedTrackColor = rightLightColor.copy(alpha = 0.7F),
                    uncheckedTrackColor = Color(0xFF505050),
                )
            )
        }
    }
}

fun estimateGradientEffect(
    touchPosition: Offset,
    gradientCenter: Offset,
    sphereRadius: Float
): Float {
    val distance = sqrt(
        (touchPosition.x - gradientCenter.x).pow(2) +
                (touchPosition.y - gradientCenter.y).pow(2)
    )

    val maxGradientRadius = sphereRadius * 1.7f
    return (1 - (distance / maxGradientRadius)).coerceIn(0f, 1f)
}

fun estimateColorAtPoint(
    touchPosition: Offset,
    sphereRadius: Float,
    leftGradientCenter: Offset,
    leftLightColor: Color,
    sphereColor: Color,
    rightGradientCenter: Offset?,
    rightLightColor: Color?,
    isRightLightOn: Boolean
): Color {
    val leftEffect = estimateGradientEffect(touchPosition, leftGradientCenter, sphereRadius)

    val rightEffect = if (isRightLightOn && rightGradientCenter != null && rightLightColor != null) {
        estimateGradientEffect(touchPosition, rightGradientCenter, sphereRadius)
    } else {
        0f
    }

    val leftActualColor = multiplyColors(sphereColor, leftLightColor)
    val rightActualColor = if (isRightLightOn && rightLightColor != null) {
        multiplyColors(sphereColor, rightLightColor)
    } else {
        Color.Transparent
    }

    return Color(
        red = (leftActualColor.red * leftEffect + rightActualColor.red * rightEffect).coerceIn(0f, 1f),
        green = (leftActualColor.green * leftEffect + rightActualColor.green * rightEffect).coerceIn(0f, 1f),
        blue = (leftActualColor.blue * leftEffect + rightActualColor.blue * rightEffect).coerceIn(0f, 1f)
    )
}

private fun showColorPickerDialog(context: Context, onColorSelected: (Int) -> Unit) {
    val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_color_picker, null)
    val colorPicker = dialogView.findViewById<ColorPicker>(R.id.color_picker)
    val hueSlider = dialogView.findViewById<HueSlider>(R.id.hue_slider)
    val selectButton = dialogView.findViewById<Button>(R.id.btn_select_color)

    colorPicker.setDialogView(dialogView)
    colorPicker.hueSliderView = hueSlider

    val dialog = AlertDialog.Builder(context)
        .setView(dialogView)
        .create()

    selectButton.setOnClickListener {
        onColorSelected(colorPicker.color)
        dialog.dismiss()
    }

    dialog.show()
}
