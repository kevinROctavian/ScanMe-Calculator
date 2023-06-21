package com.kevinroctavian.scanme.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.kevinroctavian.scanme.BuildConfig

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

private val RedColorPalette = darkColors(
    primary = Color.Red,
    primaryVariant = Color.Red,
    secondary = Teal200,
    onBackground = Color.White,
    surface = Color.White
)
private val GreenColorPalette = darkColors(
    primary = Color.Green,
    primaryVariant = Color.Green,
    secondary = Teal200,
    onBackground = Color.White,
    surface = Color.White
)

@Composable
fun ScanMeTheme(content: @Composable () -> Unit) {
    val colors = if (BuildConfig.FLAVOR.lowercase().contains("red")) {
        RedColorPalette
    } else {
        GreenColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}