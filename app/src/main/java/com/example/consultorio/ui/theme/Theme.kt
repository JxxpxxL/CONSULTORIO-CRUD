package com.example.consultorio.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = BluePrimary,
    secondary = GreenAccent,
    tertiary = BlueLight,
    background = SoftBackground,
    surface = CardWhite,
    primaryContainer = BlueLight,
    secondaryContainer = BlueLight,
    tertiaryContainer = BlueLight
)

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    secondary = GreenAccent,
    tertiary = BlueLight,
    background = SoftBackground,
    surface = CardWhite,
    primaryContainer = BlueLight,
    secondaryContainer = BlueLight,
    tertiaryContainer = BlueLight
)

@Composable
fun ConsultorioTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}