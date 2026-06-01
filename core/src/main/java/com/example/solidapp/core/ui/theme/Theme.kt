package com.example.solidapp.core.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val ColorScheme = lightColorScheme(
    primary          = Ink,
    secondary        = InkLight,
    tertiary         = Accent,
    background       = Snow,
    surface          = Snow,
    surfaceVariant   = Fog,
    onPrimary        = Snow,
    onSecondary      = Snow,
    onTertiary       = Snow,
    onBackground     = Ink,
    onSurface        = Ink,
    onSurfaceVariant = Stone,
    outline          = Pebble,
    error            = ErrorColor
)

@Composable
fun SolidAppTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Snow.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }
    MaterialTheme(
        colorScheme = ColorScheme,
        typography  = Typography,
        content     = content
    )
}
