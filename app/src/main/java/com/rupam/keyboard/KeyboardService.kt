package com.rupam.keyboard

import android.inputmethodservice.InputMethodService
import android.view.View

class KeyboardService : InputMethodService() {
    private lateinit var keyboardView: CustomKeyboardView

    override fun onCreateInputView(): View {
        keyboardView = CustomKeyboardView(this)
        keyboardView.onKeyPressed = { key ->
            val ic = currentInputConnection
            when (key) {
                "⌫" -> ic?.deleteSurroundingText(1, 0)
                "↵" -> ic?.commitText("\n", 1)
                "space" -> ic?.commitText(" ", 1)
                else -> ic?.commitText(key, 1)
            }
        }
        return keyboardView
    }

    override fun onStartInputView(info: android.view.inputmethod.EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        if (::keyboardView.isInitialized) keyboardView.refreshTheme()
    }
}
