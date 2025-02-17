package com.gustavopimentel.colorspaces.ui.colorpicker

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.gustavopimentel.colorspaces.R
import com.gustavopimentel.colorspaces.util.ColorUtils.calculateLuminosity
import com.gustavopimentel.colorspaces.util.ColorUtils.getContrastColor
import kotlin.math.max
import kotlin.math.min

class ColorPicker(context: Context, attributeSet: AttributeSet?) :
    ColorSlider(context, attributeSet) {

    constructor(context: Context) : this(context, null)

    private lateinit var colorShader: LinearGradient
    private lateinit var darknessShader: LinearGradient

    private var dialogView: View? = null
    private var hexCodeTextView: TextView? = null
    private var hueTextView: TextView? = null
    private var saturationTextView: TextView? = null
    private var brightnessTextView: TextView? = null
    private var luminosityTextView: TextView? = null
    private var buttonView: TextView? = null

    private val hsvArray = FloatArray(3)

    fun setDialogView(view: View) {
        this.dialogView = view
        this.hexCodeTextView = view.findViewById(R.id.tv_hex_code)
        this.hueTextView = view.findViewById(R.id.tv_hue)
        this.saturationTextView = view.findViewById(R.id.tv_saturation)
        this.brightnessTextView = view.findViewById(R.id.tv_brightness)
        this.luminosityTextView = view.findViewById(R.id.tv_luminosity)
        this.buttonView = view.findViewById(R.id.btn_select_color)
    }

    private var hue = 30
        set(value) {
            if (value < 0f || value > 360f) {
                throw IllegalStateException("hue value should be between 0 and 360")
            }

            field = value

            initializeSliderPaint()
            calculateColor(circleX, circleY)
            invalidate()
        }

    private var circleIndicatorRadius = dp(12)
        set(value) {
            field = value
            invalidate()
        }

    var hueSliderView: HueSlider? = null
        set(value) {
            if (value != null) {
                field = value
                this.hue = value.hue.toInt()

                value.setOnHueChangedListener { hue, _ ->
                    this.hue = hue.toInt()
                }
            }
        }

    var color = Color.RED

    private var onColorChanged: ((color: Int) -> Unit)? = null
    private var onColorChangedListener: OnColorChangedListener? = null

    private var defaultSize = dp(320).toInt()

    init {
        linePaint.style = Paint.Style.FILL
    }

    override fun onCirclePositionChanged(circlePositionX: Float, circlePositionY: Float) {
        calculateColor(circlePositionX, circlePositionY)
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measureWidth = MeasureSpec.getSize(widthMeasureSpec)
        val measureHeight = MeasureSpec.getSize(heightMeasureSpec)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val finalWidth = when (widthMode) {
            MeasureSpec.EXACTLY -> measureWidth
            MeasureSpec.AT_MOST -> min(defaultSize, measureWidth)
            MeasureSpec.UNSPECIFIED -> defaultSize
            else -> suggestedMinimumWidth
        }

        val finalHeight = when (heightMode) {
            MeasureSpec.EXACTLY -> measureHeight
            MeasureSpec.AT_MOST -> min(defaultSize, measureHeight)
            MeasureSpec.UNSPECIFIED -> defaultSize
            else -> suggestedMinimumWidth
        }

        setMeasuredDimension(
            max(finalWidth, suggestedMinimumWidth),
            max(finalHeight, suggestedMinimumHeight)
        )
    }

    override fun calculateBounds(targetWidth: Float, targetHeight: Float) {
        val fx = (circleX - drawingStart) / (widthF - drawingStart)
        val fy = (circleY - drawingTop) / (heightF - drawingTop)

        widthF = targetWidth - paddingEnd - circleIndicatorRadius
        heightF = targetHeight - paddingBottom - circleIndicatorRadius

        drawingStart = paddingStart + circleIndicatorRadius
        drawingTop = paddingTop + circleIndicatorRadius

        if (isFirstTimeLaying) {
            isFirstTimeLaying = false
            circleX = widthF
            circleY = drawingTop
        } else if (isRestoredState) {
            circleX = ((widthF - drawingStart) * fx) + drawingStart
            circleY = ((heightF - drawingTop) * fy) + drawingTop
        } else {
            circleX = ((widthF - drawingStart) * fx) + drawingStart
            circleY = ((heightF - drawingTop) * fy) + drawingTop
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.run {
            drawRect(drawingStart, drawingTop, widthF, heightF, linePaint.apply {
                shader = colorShader
            })

            drawRect(drawingStart, drawingTop, widthF, heightF, linePaint.apply {
                shader = darknessShader
            })

            drawCircle(
                circleX,
                circleY,
                circleIndicatorRadius,
                circlePaint.apply {
                    color = strokeColor
                })

            drawCircle(
                circleX,
                circleY,
                circleIndicatorRadius - strokeSize,
                circlePaint.apply {
                    color = color
                })
        }
    }

    override fun initializeSliderPaint() {
        hsvArray[0] = hue.toFloat()
        hsvArray[1] = 1f
        hsvArray[2] = 1f

        colorShader =
            LinearGradient(
                drawingStart,
                0f,
                widthF,
                0f,
                Color.WHITE,
                Color.HSVToColor(hsvArray),
                Shader.TileMode.MIRROR
            )

        darknessShader =
            LinearGradient(
                0f,
                drawingTop,
                0f,
                heightF,
                Color.TRANSPARENT,
                Color.BLACK,
                Shader.TileMode.MIRROR
            )

        calculateColor(circleX, circleY)
    }

    override fun onSaveInstanceState(): Parcelable {
        return (super.onSaveInstanceState() as Bundle).apply {
            putInt(HUE_KEY, hue)
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        (state as Bundle).let { bundle ->
            isFirstTimeLaying = false
            hue = bundle.getInt(HUE_KEY)
        }
        super.onRestoreInstanceState(state)
    }

    private fun calculateColor(ex: Float, ey: Float) {
        hsvArray[0] = hue.toFloat()
        hsvArray[1] = (ex - drawingStart) / (widthF - drawingStart)
        hsvArray[2] = 1f - ((ey - drawingTop) / (heightF - drawingTop))

        color = Color.HSVToColor(hsvArray)
        callListeners()
    }

    private fun callListeners() {
        onColorChanged?.invoke(color)
        onColorChangedListener?.onColorChanged(color)

        buttonView?.setBackgroundColor(color)
        buttonView?.setTextColor(getContrastColor(color))

        hexCodeTextView?.text = hexCodeTextView?.context?.getString(R.string.hex_code, String.format("#%06X", 0xFFFFFF and color))
        hueTextView?.text = hueTextView?.context?.getString(R.string.hue, hsvArray[0].toInt())
        saturationTextView?.text = saturationTextView?.context?.getString(R.string.saturation, (hsvArray[1] * 100.0f).toInt())
        brightnessTextView?.text = brightnessTextView?.context?.getString(R.string.brightness, (hsvArray[2] * 100.0f).toInt())
        luminosityTextView?.text = luminosityTextView?.context?.getString(R.string.luminosity, calculateLuminosity(color))
    }

    interface OnColorChangedListener {
        fun onColorChanged(color: Int)
    }

    companion object {
        private const val HUE_KEY = "hue"
    }

}