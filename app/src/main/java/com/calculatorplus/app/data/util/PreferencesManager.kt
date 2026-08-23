package com.calculatorplus.app.data.util

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("calc_plus_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_THEME = "theme_selection"
        private const val KEY_SHAPE = "keypad_button_shape"
        private const val KEY_FONT = "font_style"
        private const val KEY_HISTORY = "calc_history"
        
        const val THEME_SYSTEM = "System"
        const val THEME_LIGHT = "Light"
        const val THEME_DARK = "Dark"
        
        const val SHAPE_ROUNDED_RECT = "Rounded Rectangle"
        const val SHAPE_CIRCLE = "Circle"
        
        const val FONT_DOT_MATRIX = "Dot Matrix"
        const val FONT_SYSTEM = "System"
        const val FONT_ROBOTO_SLAB = "Roboto Slab"
        const val FONT_OPEN_SANS_CONDENSED = "Open Sans Condensed"
    }

    var theme: String
        get() = prefs.getString(KEY_THEME, THEME_SYSTEM) ?: THEME_SYSTEM
        set(value) = prefs.edit().putString(KEY_THEME, value).apply()

    var buttonShape: String
        get() = prefs.getString(KEY_SHAPE, SHAPE_ROUNDED_RECT) ?: SHAPE_ROUNDED_RECT
        set(value) = prefs.edit().putString(KEY_SHAPE, value).apply()

    var fontStyle: String
        get() = prefs.getString(KEY_FONT, FONT_DOT_MATRIX) ?: FONT_DOT_MATRIX
        set(value) = prefs.edit().putString(KEY_FONT, value).apply()

    fun getHistory(): List<String> {
        val historySet = prefs.getStringSet(KEY_HISTORY, emptySet()) ?: emptySet()
        return historySet.sortedDescending()
    }

    fun addHistoryEntry(entry: String) {
        val currentHistory = getHistory().toMutableList()
        val timestamp = System.currentTimeMillis()
        val formattedEntry = "$timestamp||$entry"
        currentHistory.add(0, formattedEntry)
        if (currentHistory.size > 50) {
            currentHistory.removeAt(currentHistory.size - 1)
        }
        prefs.edit().putStringSet(KEY_HISTORY, currentHistory.toSet()).apply()
    }

    fun clearHistory() {
        prefs.edit().remove(KEY_HISTORY).apply()
    }
}
