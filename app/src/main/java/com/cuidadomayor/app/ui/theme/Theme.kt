package com.cuidadomayor.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    onPrimary = SurfaceContainerLowest,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = SecondaryTerracotta,
    onSecondary = SurfaceContainerLowest,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryFixed,
    tertiary = TertiaryGreen,
    tertiaryContainer = TertiaryContainer,
    background = SurfaceBg,
    onBackground = OnSurfaceText,
    surface = SurfaceBg,
    onSurface = OnSurfaceText,
    surfaceVariant = SurfaceContainer,
    onSurfaceVariant = OnSurfaceVariant,
    outline = OutlineGrey,
    error = EmergencyRed,
    errorContainer = EmergencyContainer
)

@Composable
fun CuidadoMayorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
