package com.pixelcraftin.calcplus

import android.os.Bundle
import android.os.Build
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.pixelcraftin.calcplus.ui.navigation.NavGraph
import com.pixelcraftin.calcplus.ui.theme.CalcPlusTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.pixelcraftin.calcplus.data.util.PreferencesManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNativeEdgeToEdge()

        val preferencesManager = PreferencesManager(this)

        setContent {
            var themeState by remember { mutableStateOf(preferencesManager.theme) }
            var shapeState by remember { mutableStateOf(preferencesManager.buttonShape) }
            var fontState by remember { mutableStateOf(preferencesManager.fontStyle) }

            val darkTheme = when (themeState) {
                PreferencesManager.THEME_LIGHT -> false
                PreferencesManager.THEME_DARK -> true
                else -> isSystemInDarkTheme()
            }

            val useDotMatrix = fontState == PreferencesManager.FONT_DOT_MATRIX

            CalcPlusTheme(
                darkTheme = darkTheme,
                useDotMatrix = useDotMatrix,
                fontStyle = fontState
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavGraph(
                        navController = navController,
                        preferencesManager = preferencesManager,
                        onThemeChange = { themeState = preferencesManager.theme },
                        onShapeChange = { shapeState = preferencesManager.buttonShape },
                        onFontChange = { fontState = preferencesManager.fontStyle }
                    )
                }
            }
        }
    }

    private fun setupNativeEdgeToEdge() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            @Suppress("DEPRECATION")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isStatusBarContrastEnforced = false
                window.isNavigationBarContrastEnforced = false
            }
            @Suppress("DEPRECATION")
            window.statusBarColor = android.graphics.Color.TRANSPARENT
            @Suppress("DEPRECATION")
            window.navigationBarColor = android.graphics.Color.TRANSPARENT
        }
    }
}
