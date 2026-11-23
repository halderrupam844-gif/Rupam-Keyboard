package com.rupam.keyboard

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

class CustomKeyboardView(context: Context) : View(context) {
    var onKeyPressed: ((String) -> Unit)? = null
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var radius = 18f
    private var keyColor = 0xFFFFFFFF.toInt()
    private var textColor = 0xFF000000.toInt()
    private var backgroundColor = 0xFFCCCCCC.toInt()
    private var bengali = false
    private var numbers = false

    private val englishRows get() = listOf(
        "qwertyuiop".map { it.toString() },
        "asdfghjkl".map { it.toString() },
        listOf("⇧") + "zxcvbnm".map { it.toString() } + listOf("⌫"),
        listOf("?123", "বাংলা", "space", "↵")
    )
    private val bengaliRows = listOf(
        "ৌৈাীূবহগদ".map { it.toString() },
        "োে্িুপটচজ".map { it.toString() },
        listOf("⇧") + "কনমতরলসয".map { it.toString() } + listOf("⌫"),
        listOf("?123", "English", "space", "↵")
    )
    private val numberRows = listOf(
        "1234567890".map { it.toString() },
        "!@#$%&*()-".map { it.toString() },
        listOf("⇧") + "+=/:;?'\"".map { it.toString() } + listOf("⌫"),
        listOf("ABC", "বাংলা", "space", "↵")
    )

    init { refreshTheme() }

    fun setTheme(r: Float, key: Int, text: Int, bg: Int) {
        radius = r
        keyColor = key
        textColor = text
        backgroundColor = bg
        invalidate()
    }

    fun refreshTheme() = setTheme(
        ThemeManager.radius(context),
        ThemeManager.keyColor(context),
        ThemeManager.textColor(context),
        ThemeManager.backgroundColor(context)
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(
            widthMeasureSpec,
            MeasureSpec.makeMeasureSpec(
                (260 * resources.displayMetrics.density).toInt(),
                MeasureSpec.EXACTLY
            )
        )
    }

    private fun rows() = when {
        numbers -> numberRows
        bengali -> bengaliRows
        else -> englishRows
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(backgroundColor)
        val rows = rows()
        val rowHeight = height.toFloat() / rows.size

        rows.forEachIndexed { ri, row ->
            val keyWidth = width.toFloat() / row.size
            row.forEachIndexed { ci, key ->
                val rect = RectF(
                    ci * keyWidth + 3f,
                    ri * rowHeight + 4f,
                    (ci + 1) * keyWidth - 3f,
                    (ri + 1) * rowHeight - 4f
                )
                paint.color = if (key in listOf("⌫", "↵", "?123", "বাংলা", "English", "ABC", "⇧")) {
                    0xFFB0BEC5.toInt()
                } else keyColor
                val r = radius.coerceAtMost(minOf(rect.width(), rect.height()) / 2f) *
                    resources.displayMetrics.density
                canvas.drawRoundRect(rect, r, r, paint)
                paint.color = textColor
                paint.textAlign = Paint.Align.CENTER
                paint.textSize = 17f * resources.displayMetrics.density
                val label = if (key == "space") " " else key
                val baseline = rect.centerY() - (paint.ascent() + paint.descent()) / 2
                canvas.drawText(label, rect.centerX(), baseline, paint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val current = rows()
            val rowHeight = height.toFloat() / current.size
            val ri = (event.y / rowHeight).toInt().coerceIn(0, current.lastIndex)
            val row = current[ri]
            val keyWidth = width.toFloat() / row.size
            val ci = (event.x / keyWidth).toInt().coerceIn(0, row.lastIndex)
            when (val key = row[ci]) {
                "বাংলা" -> { bengali = true; numbers = false; invalidate() }
                "English", "ABC" -> { bengali = false; numbers = false; invalidate() }
                "?123" -> { numbers = true; invalidate() }
                "⇧" -> { /* Shift layout can be added in a later version. */ }
                else -> onKeyPressed?.invoke(key)
            }
        }
        return true
    }
}
