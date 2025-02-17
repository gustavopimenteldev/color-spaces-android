package com.gustavopimentel.colorspaces.ui.colorpicker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.gustavopimentel.colorspaces.R
import kotlin.math.floor
import kotlin.math.max

abstract class ColorSlider(context: Context, attributeSet: AttributeSet?) :
    View(context, attributeSet) {

    constructor(context: Context) : this(context, null)

    protected val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    protected val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.RED
    }

    protected var widthF = 0f
    protected var heightF = 0f

    private var heightHalf = 0f

    protected var drawingStart = 0f
    protected var drawingTop = 0f

    private var strokeWidthHalf = 0f

    protected var circleX = 0f
    protected var circleY = 0f

    private var circleXFactor = 0f
    private var circleYFactor = 0f

    protected var circleColor: Int = Color.TRANSPARENT

    protected var isFirstTimeLaying = true
    protected var isRestoredState = false

    private var isWrapContent = false

    private var lineStrokeCap = Paint.Cap.ROUND
        set(value) {
            field = value
            linePaint.strokeCap = field
            requestLayout()
        }

    protected var strokeColor = Color.WHITE
        set(value) {
            field = value
            invalidate()
        }

    protected var strokeSize = dp(2)
        set(value) {
            field = value
            invalidate()
        }

    private var defaultPaddingVertical = dp(12)
    private var wrapContentSize = dp(48).toInt()

    init {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.MananSlider, 0, 0).apply {
            try {
                lineStrokeCap = Paint.Cap.entries.toTypedArray()[getInt(
                    R.styleable.MananSlider_sliderBarStrokeCap,
                    Paint.Cap.ROUND.ordinal
                )]

                strokeSize = getDimension(R.styleable.MananSlider_sliderStrokeSize, strokeSize)

                strokeColor = getColor(R.styleable.MananSlider_sliderStrokeColor, strokeColor)

            } finally {
                recycle()
            }
        }
    }

    protected open fun changePositionOfCircle(ex: Float, ey: Float) {
        circleX = floor(ex).coerceIn(drawingStart, widthF)
        circleY = floor(ey).coerceIn(drawingTop, heightF)

        onCirclePositionChanged(circleX, circleY)
    }

    /**
     * Called when position of indicator changes in slider or picker.
     * @param circlePositionX Position of indicator in x axis.
     * @param circlePositionY Position of indicator in y axis.
     */
    protected open fun onCirclePositionChanged(circlePositionX: Float, circlePositionY: Float) {

    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let { e ->
            return when (e.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    changePositionOfCircle(e.x, e.y)
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    changePositionOfCircle(e.x, e.y)
                    true
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    changePositionOfCircle(e.x, e.y)
                    false
                }

                else -> {
                    false
                }
            }
        }
        return super.onTouchEvent(null)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (w != oldw || h != oldh) {

            calculateBounds(w.toFloat(), h.toFloat())

            initializeSliderPaint()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measureWidth = MeasureSpec.getSize(widthMeasureSpec)
        val measureHeight = MeasureSpec.getSize(heightMeasureSpec)

        isWrapContent = false

        val finalHeight = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.EXACTLY -> {
                measureHeight
            }

            MeasureSpec.AT_MOST, MeasureSpec.UNSPECIFIED -> {
                isWrapContent = true
                wrapContentSize
            }

            else -> {
                suggestedMinimumHeight
            }
        }

        setMeasuredDimension(
            max(measureWidth, suggestedMinimumWidth),
            max(finalHeight, suggestedMinimumHeight)
        )

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (changed) {
            calculateBounds(width.toFloat(), height.toFloat())
            initializeSliderPaint()
        }
    }

    /**
     * Calculates bounds of drawing elements.
     * @param targetWidth limiting width of drawings, often width of the view is supplied.
     * @param targetHeight limiting height of drawings, often height of the view is supplied.
     */
    protected open fun calculateBounds(targetWidth: Float, targetHeight: Float) {
        val fx = (circleX - drawingStart) / (widthF - drawingStart)
        val fy = (circleY - drawingTop) / (heightF - drawingTop)

        heightF = targetHeight - paddingBottom - paddingTop

        if (isWrapContent) {
            heightF -= (defaultPaddingVertical * 2f)
        }

        heightHalf = heightF * 0.5f
        linePaint.strokeWidth = heightHalf

        widthF = targetWidth - paddingEnd - heightHalf

        drawingStart = heightHalf + paddingStart
        drawingTop = heightHalf + paddingTop

        if (isWrapContent) {
            drawingTop += defaultPaddingVertical
        }

        if (isFirstTimeLaying) {
            isFirstTimeLaying = false
            circleX = widthF
            circleY = drawingTop
        } else if (isRestoredState) {
            circleX = ((widthF - drawingStart) * circleXFactor) + drawingStart
            circleY = ((heightF - drawingTop) * circleYFactor) + drawingTop

            circleXFactor = 0f
            circleYFactor = 0f

            isRestoredState = false
        } else {
            circleX = ((widthF - drawingStart) * fx) + drawingStart
            circleY = ((heightF - drawingTop) * fy) + drawingTop
        }

        strokeWidthHalf = if (linePaint.strokeCap != Paint.Cap.BUTT) {
            linePaint.strokeWidth * 0.5f
        } else {
            0f
        }

    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawLine(
            drawingStart,
            drawingTop,
            widthF,
            drawingTop,
            linePaint
        )

        canvas.drawCircle(
            circleX,
            drawingTop,
            heightHalf,
            circlePaint.apply {
                color = strokeColor
            })

        canvas.drawCircle(
            circleX,
            drawingTop,
            heightHalf - strokeSize,
            circlePaint.apply {
                color = circleColor
            })

    }

    override fun onSaveInstanceState(): Parcelable? {
        return Bundle().apply {
            putFloat(CIRCLE_X_KEY, (circleX - drawingStart) / (widthF - drawingStart))
            putFloat(CIRCLE_Y_KEY, (circleY - drawingTop) / (heightF - drawingTop))
            putParcelable(STATE_KEY, super.onSaveInstanceState())
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        (state as? Bundle)?.apply {
            circleXFactor = getFloat(CIRCLE_X_KEY)
            circleYFactor = getFloat(CIRCLE_Y_KEY)
            isFirstTimeLaying = false
            isRestoredState = true

            val restoredState: Parcelable? =
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    getParcelable(STATE_KEY, Parcelable::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    getParcelable(STATE_KEY)
                }

            super.onRestoreInstanceState(restoredState)
            return
        }

        super.onRestoreInstanceState(state)
    }

    open fun initializeSliderPaint() {
    }

    companion object {
        private const val CIRCLE_X_KEY = "circleX"
        private const val CIRCLE_Y_KEY = "circleY"
        private const val STATE_KEY = "p"
    }

}