package com.gamebd.rpsduel.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val ArcadeColorScheme = darkColorScheme(
    primary = Player1Cyan,
    onPrimary = ArcadeBackground,
    secondary = Player2Pink,
    onSecondary = TextWhite,
    tertiary = AccentGold,
    background = ArcadeBackground,
    onBackground = TextWhite,
    surface = ArcadeSurface,
    onSurface = TextWhite,
    surfaceVariant = ArcadeCard,
    onSurfaceVariant = TextMuted
)

@Composable
fun RPSDuelTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = ArcadeBackground.toArgb()
            window.navigationBarColor = ArcadeBackground.toArgb()
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = false
            insetsController.isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = ArcadeColorScheme,
        typography = Typography,
        content = content
    )
}
