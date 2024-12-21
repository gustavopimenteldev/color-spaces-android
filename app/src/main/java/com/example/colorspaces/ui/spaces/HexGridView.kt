package com.example.colorspaces.ui.spaces

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.colorspaces.MainActivity
import com.example.colorspaces.R
import com.example.colorspaces.ui.colorpicker.ColorPicker
import com.example.colorspaces.ui.colorpicker.HueSlider
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

//TODO: add slider for brightness, in the selector box add a text input that also changes dynamically, in the selected box, add hue, brightness, saturation and luminosity information
class HexGridView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val hexPath = Path()
    private val hexRadius = 70f
    private val hexGrid = mutableListOf<MutableList<HexCell>>()
    private val side = 4

    private var gridWidth = 0f
    private var gridHeight = 0f
    private var xOffset = 0f
    private var yOffset = 0f
    private var minX = Float.MAX_VALUE
    private var maxX = Float.MIN_VALUE

    private val holdPressHandler = android.os.Handler()
    private var holdPressRunnable: Runnable? = null
    private var isHoldPress = false

    private val cornerColors = mutableListOf(Color.RED, Color.GREEN, Color.BLUE)

    init {
        paint.style = Paint.Style.FILL
        setupHexGrid()
    }

    data class HexCell(
        val x: Float,
        val y: Float,
        val q: Int,
        val r: Int,
        var color: Int = Color.LTGRAY,
        val isCorner: Boolean = false
    )

    private fun setupHexGrid() {
        val rows = 2 * side - 1
        val dx = 2f * hexRadius
        val dy = 1.7f * hexRadius

        for (row in 0 until rows) {
            val rowList = mutableListOf<HexCell>()

            val cols = if (row < side) side + row - 1 else 3 * side - row - 3
            val xOffset = (3 * side - 3 - cols) * dx / 2f

            for (col in 0..cols) {
                val x = col * dx + xOffset
                val y = row * dy

                val q = col - (row.coerceAtMost(side - 1))
                val r = row - (side - 1)

                minX = minOf(minX, x)
                maxX = maxOf(maxX, x)

                val isCorner = (row == 0 && col == 0) || (row == side - 1 && col == cols) || (row == rows - 1 && col == 0)

                rowList.add(HexCell(x, y, q, r, isCorner = isCorner))
            }
            hexGrid.add(rowList)
        }

        gridWidth = maxX - minX
        gridHeight = rows * dy

        recalculateHexColors()
    }

    private fun recalculateHexColors() {
        for (row in hexGrid.indices) {
            for (col in hexGrid[row].indices) {
                val hex = hexGrid[row][col]

                val (weight1, weight2, weight3) = calculateColorValue(hex.q, hex.r)

                val color1 = scaleColor(cornerColors[0], weight1)
                val color2 = scaleColor(cornerColors[1], weight2)
                val color3 = scaleColor(cornerColors[2], weight3)
                val finalColor = addColors(color1, color2, color3)

                hex.color = finalColor
            }
        }
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        xOffset = (width - gridWidth) / 2f - minX

        yOffset = (height - gridHeight) / 2f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        hexGrid.flatten().forEach { hex ->
            drawHexagon(canvas, hex.x + xOffset, hex.y + yOffset, hex.color)
        }
    }

    private fun drawHexagon(canvas: Canvas, x: Float, y: Float, color: Int) {
        hexPath.reset()
        for (i in 0 until 6) {
            val angle = Math.toRadians((60 * i).toDouble())
            val px = x + hexRadius * sin(angle).toFloat()
            val py = y + hexRadius * cos(angle).toFloat()
            if (i == 0) hexPath.moveTo(px, py) else hexPath.lineTo(px, py)
        }
        hexPath.close()

        paint.color = color
        canvas.drawPath(hexPath, paint)
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        canvas.drawPath(hexPath, paint)
        paint.style = Paint.Style.FILL
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isHoldPress = false
                handleHoldPress(event.x, event.y)
            }
            MotionEvent.ACTION_UP -> {
                if (!isHoldPress) {
                    handleSimplePress(event.x, event.y)
                }
                cancelHoldPress()
            }
        }
        return true
    }

    private  fun handleSimplePress(touchX: Float, touchY: Float) {
        val clickedHex = hexGrid.flatten().find { isInsideHex(touchX, touchY, it.x + xOffset, it.y + yOffset) }
        clickedHex?.let {
            (context as MainActivity).updateInfoColor(it.color)
            invalidate()
        }
    }

    private fun handleHoldPress(touchX: Float, touchY: Float) {
        holdPressRunnable = Runnable {
            val holdPressedHex = hexGrid.flatten().find { isInsideHex(touchX, touchY, it.x + xOffset, it.y + yOffset) }

            if (holdPressedHex?.isCorner == true) {
                isHoldPress = true

                val cornerIndex = when (holdPressedHex) {
                    hexGrid[0][0] -> 0
                    hexGrid.last()[0] -> 1
                    hexGrid[side - 1].last() -> 2
                    else -> return@Runnable
                }

                (context as? AppCompatActivity)?.let { activity ->
                    showColorPickerDialog(activity) { selectedColor ->
                        cornerColors[cornerIndex] = selectedColor
                        recalculateHexColors()
                    }
                }
            }
        }
        holdPressHandler.postDelayed(holdPressRunnable!!, 500)
    }

    private fun cancelHoldPress() {
        holdPressRunnable?.let { holdPressHandler.removeCallbacks(it) }
    }

    private fun isInsideHex(touchX: Float, touchY: Float, hexX: Float, hexY: Float): Boolean {
        val dx = abs(touchX - hexX)
        val dy = abs(touchY - hexY)
        return dx < hexRadius && dy < sqrt(3.0) / 2 * hexRadius
    }

    private fun showColorPickerDialog(context: Context, onColorSelected: (Int) -> Unit) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_color_picker, null)
        val colorPicker = dialogView.findViewById<ColorPicker>(R.id.color_picker)
        val hueSlider = dialogView.findViewById<HueSlider>(R.id.hue_slider)
        val selectButton = dialogView.findViewById<Button>(R.id.btn_select_color)

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

    private fun calculateColorValue(q: Int, r: Int): Triple<Double, Double, Double> {
        fun hexDistance(q1: Int, r1: Int, q2: Int, r2: Int): Int {
            val dq = q2 - q1
            val dr = r2 - r1
            val ds = -(dq + dr)
            return maxOf(dq.absoluteValue, dr.absoluteValue, ds.absoluteValue)
        }

        val corner1 = Pair(0, -side + 1)
        val corner2 = Pair(-side + 1, side -1)
        val corner3 = Pair(side - 1, 0)

        val distanceToCorner1 = hexDistance(q, r, corner1.first, corner1.second)
        val distanceToCorner2 = hexDistance(q, r, corner2.first, corner2.second)
        val distanceToCorner3 = hexDistance(q, r, corner3.first, corner3.second)

        val halfMaxDistance = side - 1

        val scaledCorner1 = if (distanceToCorner1 <= halfMaxDistance) {
            1.0
        } else {
            1.0 - ((distanceToCorner1 - halfMaxDistance).toDouble() / halfMaxDistance)
        }

        val scaledCorner2 = if (distanceToCorner2 <= halfMaxDistance) {
            1.0
        } else {
            1.0 - ((distanceToCorner2 - halfMaxDistance).toDouble() / halfMaxDistance)
        }

        val scaledCorner3 = if (distanceToCorner3 <= halfMaxDistance) {
            1.0
        } else {
            1.0 - ((distanceToCorner3 - halfMaxDistance).toDouble() / halfMaxDistance)
        }

        return Triple(scaledCorner1, scaledCorner2, scaledCorner3)
    }

    private fun scaleColor(baseColor: Int, proportion: Double): Int {
        require(proportion in 0.0..1.0) { "Proportion must be between 0 and 1" }

        val red = (Color.red(baseColor) * proportion).toInt().coerceIn(0, 255)
        val green = (Color.green(baseColor) * proportion).toInt().coerceIn(0, 255)
        val blue = (Color.blue(baseColor) * proportion).toInt().coerceIn(0, 255)

        return Color.rgb(red, green, blue)
    }

    private fun addColors(color1: Int, color2: Int, color3: Int): Int {
        val red = (Color.red(color1) + Color.red(color2) + Color.red(color3)).coerceAtMost(255)
        val green = (Color.green(color1) + Color.green(color2) + Color.green(color3)).coerceAtMost(255)
        val blue = (Color.blue(color1) + Color.blue(color2) + Color.blue(color3)).coerceAtMost(255)

        return Color.rgb(red, green, blue)
    }
}