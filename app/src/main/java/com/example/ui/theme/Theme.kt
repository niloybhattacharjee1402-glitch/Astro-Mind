package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AstroMindDarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = SpaceDarkBg,
    primaryContainer = SpacePanelLight,
    onPrimaryContainer = NeonCyan,
    secondary = BioGreen,
    onSecondary = SpaceDarkBg,
    secondaryContainer = SpacePanelLight,
    onSecondaryContainer = BioGreen,
    tertiary = SolarYellow,
    onTertiary = SpaceDarkBg,
    error = AlertRed,
    onError = TextWhite,
    background = SpaceDarkBg,
    onBackground = TextWhite,
    surface = SpacePanelBg,
    onSurface = TextWhite,
    surfaceVariant = SpacePanelLight,
    onSurfaceVariant = TextMuted,
    outline = SpacePanelBorder
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    // NASA Mission Control interface is strictly deep space dark themed
    MaterialTheme(
        colorScheme = AstroMindDarkColorScheme,
        typography = Typography,
        content = content
    )
}
