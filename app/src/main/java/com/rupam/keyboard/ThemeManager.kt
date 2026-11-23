package com.rupam.keyboard

import android.content.Context
import android.graphics.Color

object ThemeManager {
    private const val PREF = "keyboard_theme"

    fun save(context: Context, radius: Float, keyColor: Int, textColor: Int, backgroundColor: Int) {
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE).edit()
            .putFloat("radius", radius)
            .putInt("keyColor", keyColor)
            .putInt("textColor", textColor)
            .putInt("backgroundColor", backgroundColor)
            .apply()
    }

    fun radius(context: Context) = context.getSharedPreferences(PREF, 0).getFloat("radius", 18f)
    fun keyColor(context: Context) = context.getSharedPreferences(PREF, 0).getInt("keyColor", Color.WHITE)
    fun textColor(context: Context) = context.getSharedPreferences(PREF, 0).getInt("textColor", Color.BLACK)
    fun backgroundColor(context: Context) = context.getSharedPreferences(PREF, 0).getInt("backgroundColor", Color.LTGRAY)
}
