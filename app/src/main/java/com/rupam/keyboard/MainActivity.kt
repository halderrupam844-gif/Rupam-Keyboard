package com.rupam.keyboard

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.provider.Settings
import android.graphics.Color
import android.view.inputmethod.InputMethodManager
import android.widget.*

class MainActivity : Activity() {
    private var radius = 18f
    private var keyColor = Color.WHITE
    private var textColor = Color.BLACK
    private var backgroundColor = Color.LTGRAY
    private lateinit var preview: CustomKeyboardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        radius = ThemeManager.radius(this)
        keyColor = ThemeManager.keyColor(this)
        textColor = ThemeManager.textColor(this)
        backgroundColor = ThemeManager.backgroundColor(this)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 28, 24, 24)
            setBackgroundColor(Color.rgb(245, 245, 250))
        }

        fun heading(text: String, size: Float) {
            layout.addView(TextView(this).apply {
                this.text = text
                textSize = size
                setTextColor(Color.BLACK)
                setPadding(0, 12, 0, 8)
            })
        }

        heading("Rupam Keyboard", 26f)
        heading("কিবোর্ড কাস্টমাইজ করুন", 18f)
        preview = CustomKeyboardView(this)
        layout.addView(preview, LinearLayout.LayoutParams(-1, dp(190)))

        heading("Button Roundness", 16f)
        val radiusLabel = TextView(this).apply { text = "Radius: ${radius.toInt()}" }
        layout.addView(radiusLabel)
        layout.addView(SeekBar(this).apply {
            max = 50
            progress = radius.toInt()
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, value: Int, fromUser: Boolean) {
                    radius = value.toFloat()
                    radiusLabel.text = "Radius: $value"
                    updatePreview()
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        })

        heading("Keyboard Themes", 18f)
        addThemeButton(layout, "Light Theme") {
            keyColor = Color.WHITE; textColor = Color.BLACK; backgroundColor = Color.LTGRAY
        }
        addThemeButton(layout, "Dark Theme") {
            keyColor = Color.DKGRAY; textColor = Color.WHITE; backgroundColor = Color.BLACK
        }
        addThemeButton(layout, "Blue Theme") {
            keyColor = Color.rgb(80, 130, 230); textColor = Color.WHITE
            backgroundColor = Color.rgb(25, 35, 65)
        }
        addThemeButton(layout, "Pink Theme") {
            keyColor = Color.rgb(255, 180, 210); textColor = Color.BLACK
            backgroundColor = Color.rgb(255, 230, 240)
        }

        layout.addView(Button(this).apply {
            text = "SAVE THEME"
            setOnClickListener {
                ThemeManager.save(this@MainActivity, radius, keyColor, textColor, backgroundColor)
                Toast.makeText(this@MainActivity, "Theme saved", Toast.LENGTH_SHORT).show()
                updatePreview()
            }
        })
        layout.addView(Button(this).apply {
            text = "ENABLE KEYBOARD"
            setOnClickListener { startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)) }
        })
        layout.addView(Button(this).apply {
            text = "SELECT KEYBOARD"
            setOnClickListener {
                (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).showInputMethodPicker()
            }
        })

        setContentView(ScrollView(this).apply { addView(layout) })
        updatePreview()
    }

    private fun addThemeButton(parent: LinearLayout, title: String, change: () -> Unit) {
        parent.addView(Button(this).apply {
            text = title
            setOnClickListener { change(); updatePreview() }
        })
    }

    private fun updatePreview() {
        if (::preview.isInitialized) preview.setTheme(radius, keyColor, textColor, backgroundColor)
    }

    private fun dp(value: Int) = (value * resources.displayMetrics.density).toInt()
}
