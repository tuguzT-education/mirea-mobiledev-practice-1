package io.github.tuguzt.mirea.todolist.view.theme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.google.android.material.color.ColorRoles
import com.google.android.material.color.MaterialColors

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun ToDoListTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (useDarkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        shapes = Shapes,
        typography = Typography,
        content = content,
    )
}

data class CustomColor(
    val name: String,
    val color: Color,
    val harmonized: Boolean,
    var roles: ColorRoles,
)

data class ExtendedColors(val colors: Array<CustomColor>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ExtendedColors

        return colors.contentEquals(other.colors)
    }

    override fun hashCode(): Int {
        return colors.contentHashCode()
    }
}

fun setupErrorColors(colorScheme: ColorScheme, isLight: Boolean): ColorScheme {
    val harmonizedError = MaterialColors.harmonize(Error.toArgb(), colorScheme.primary.toArgb())
    val roles = MaterialColors.getColorRoles(harmonizedError, isLight)

    return colorScheme.copy(
        error = Color(roles.accent),
        onError = Color(roles.onAccent),
        errorContainer = Color(roles.accentContainer),
        onErrorContainer = Color(roles.onAccentContainer),
    )
}

private val initializeExtended = ExtendedColors(arrayOf())

fun setupCustomColors(colorScheme: ColorScheme, isLight: Boolean): ExtendedColors {
    initializeExtended.colors.forEach { customColor ->
        // Retrieve record
        val shouldHarmonize = customColor.harmonized
        // Blend or not
        if (shouldHarmonize) {
            val colorToHarmonize = customColor.color.toArgb()
            val colorToHarmonizeWith = colorScheme.primary.toArgb()
            val blendedColor = MaterialColors.harmonize(colorToHarmonize, colorToHarmonizeWith)
            customColor.roles = MaterialColors.getColorRoles(blendedColor, isLight)
        } else {
            customColor.roles = MaterialColors.getColorRoles(customColor.color.toArgb(), isLight)
        }
    }
    return initializeExtended
}

val LocalExtendedColors = staticCompositionLocalOf {
    initializeExtended
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun HarmonizedTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    isDynamic: Boolean = true,
    errorHarmonize: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colors = when {
        isDynamic -> {
            val context = LocalContext.current
            if (useDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> if (useDarkTheme) DarkColorScheme else LightColorScheme
    }
    val colorsWithHarmonizedError = when {
        errorHarmonize -> setupErrorColors(colors, !useDarkTheme)
        else -> colors
    }
    val extendedColors = setupCustomColors(colors, !useDarkTheme)

    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorsWithHarmonizedError,
            shapes = Shapes,
            typography = Typography,
            content = content,
        )
    }
}
