package core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.Color.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.FloatRange
import java.lang.Math.round


@SuppressWarnings("MagicNumber")
class ColorPicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    val colors = intArrayOf(WHITE, RED, MAGENTA, BLUE, CYAN, GREEN, YELLOW)
    val strokeSize = 2 * context.resources.displayMetrics.density
    val rainbowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }
    val rainbowBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = WHITE
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }
    val pickPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    var pick = 0.5f
    var verticalGridSize = 0f
    var rainbowBaseline = 0f
    var showPreview = false
    var listener: OnColorChangedListener? = null

    override fun onDraw(canvas: Canvas) {
        drawPicker(canvas)
        drawColorAim(canvas, rainbowBaseline, verticalGridSize.toInt() / 2, verticalGridSize * 0.5f, color)
        if (showPreview) {
            drawColorAim(canvas, verticalGridSize, (verticalGridSize / 1.4f).toInt(), verticalGridSize * 0.7f, color)
        }
    }

    private fun drawPicker(canvas: Canvas) {
        val lineX = verticalGridSize / 2f
        val lineY = rainbowBaseline.toFloat()
        rainbowPaint.strokeWidth = verticalGridSize / 1.5f + strokeSize
        rainbowBackgroundPaint.strokeWidth = rainbowPaint.strokeWidth + strokeSize
        canvas.drawLine(lineX, lineY, width - lineX, lineY, rainbowBackgroundPaint)
        canvas.drawLine(lineX, lineY, width - lineX, lineY, rainbowPaint)
    }

    private fun drawColorAim(canvas: Canvas, baseLine: Float, offset: Int, size: Float, color: Int) {
        val circleCenterX = offset + pick * (canvas.width - offset * 2)
        canvas.drawCircle(circleCenterX, baseLine, size, pickPaint.apply { this.color = WHITE })
        canvas.drawCircle(circleCenterX, baseLine, size - strokeSize, pickPaint.apply { this.color = color })
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = measuredHeight
        val width = measuredWidth
        val shader = LinearGradient(
            height / 4.0f,
            height / 2.0f,
            width - height / 4.0f,
            height / 2.0f,
            colors,
            null,
            Shader.TileMode.CLAMP
        )
        verticalGridSize = height / 3f
        rainbowPaint.shader = shader
        rainbowBaseline = verticalGridSize / 2f + verticalGridSize * 2
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        if (action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_DOWN) {
            pick = event.x / measuredWidth.toFloat()
            if (pick < 0) {
                pick = 0f
            } else if (pick > 1) {
                pick = 1f
            }

            showPreview = true
        } else if (action == MotionEvent.ACTION_UP) {
            listener?.onColorChanged(color)
            showPreview = false
        }
        postInvalidateOnAnimation()
        return true
    }

    val color: Int
        get() = interpColor(pick, colors)


    fun interpColor(@FloatRange(from = 0.0, to = 1.0) unit: Float, colors: IntArray): Int {
        if (unit <= 0) return colors[0]
        if (unit >= 1) return colors[colors.size - 1]

        var p = unit * (colors.size - 1)
        val i = p.toInt()
        // take fractional part
        p -= i

        val c0 = colors[i]
        val c1 = colors[i + 1]
        // Calculates each channel separately
        val a = avg(alpha(c0), alpha(c1), p)
        val r = avg(red(c0), red(c1), p)
        val g = avg(green(c0), green(c1), p)
        val b = avg(blue(c0), blue(c1), p)

        return Color.argb(a, r, g, b)
    }

    fun avg(s: Int, e: Int, @FloatRange(from = 0.0, to = 1.0) p: Float)
            = s + round(p * (e - s))

    fun setOnColorChangedListener(listener: OnColorChangedListener) {
        this.listener = listener
    }

    interface OnColorChangedListener {
        fun onColorChanged(color: Int)
    }
}

