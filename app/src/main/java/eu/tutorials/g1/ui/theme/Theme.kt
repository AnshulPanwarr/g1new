package eu.tutorials.g1.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Modern App Colors
private val ModernBlue = Color(0xFF2196F3)
private val ModernPurple = Color(0xFF6200EE)
private val ModernTeal = Color(0xFF03DAC6)
private val ModernPink = Color(0xFFFF4081)
private val ModernOrange = Color(0xFFFF5722)
private val ModernGreen = Color(0xFF4CAF50)

private val LightColorScheme = lightColorScheme(
    primary = ModernBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE3F2FD),
    onPrimaryContainer = Color(0xFF1565C0),
    
    secondary = ModernTeal,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE0F7FA),
    onSecondaryContainer = Color(0xFF00838F),
    
    tertiary = ModernPurple,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFEDE7F6),
    onTertiaryContainer = Color(0xFF4527A0),
    
    background = Color(0xFFF8F9FA),
    onBackground = Color(0xFF1A237E),
    
    surface = Color.White,
    onSurface = Color(0xFF1A237E),
    
    surfaceVariant = Color(0xFFF5F5F5),
    onSurfaceVariant = Color(0xFF455A64),
    
    error = Color(0xFFE53935),
    onError = Color.White,

    // Additional modern colors
    scrim = Color(0x52000000),
    inverseSurface = Color(0xFF1A237E),
    inverseOnSurface = Color.White,
    inversePrimary = Color(0xFF90CAF9),
    surfaceTint = ModernBlue.copy(alpha = 0.05f),
    outline = Color(0xFFBDBDBD),
    outlineVariant = Color(0xFFE0E0E0)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF90CAF9),
    onPrimary = Color(0xFF0D47A1),
    primaryContainer = Color(0xFF1565C0),
    onPrimaryContainer = Color(0xFFE3F2FD),
    
    secondary = Color(0xFF4DD0E1),
    onSecondary = Color(0xFF004D40),
    secondaryContainer = Color(0xFF00838F),
    onSecondaryContainer = Color(0xFFE0F7FA),
    
    tertiary = Color(0xFFB39DDB),
    onTertiary = Color(0xFF311B92),
    tertiaryContainer = Color(0xFF4527A0),
    onTertiaryContainer = Color(0xFFEDE7F6),
    
    background = Color(0xFF121212),
    onBackground = Color(0xFFE8EAF6),
    
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFE8EAF6),
    
    surfaceVariant = Color(0xFF2D2D2D),
    onSurfaceVariant = Color(0xFFB0BEC5),
    
    error = Color(0xFFEF5350),
    onError = Color(0xFFB71C1C),

    // Additional modern colors
    scrim = Color(0x52000000),
    inverseSurface = Color(0xFFE8EAF6),
    inverseOnSurface = Color(0xFF1A237E),
    inversePrimary = Color(0xFF1565C0),
    surfaceTint = Color(0xFF90CAF9).copy(alpha = 0.05f),
    outline = Color(0xFF757575),
    outlineVariant = Color(0xFF424242)
)

@Composable
fun G1Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
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

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}