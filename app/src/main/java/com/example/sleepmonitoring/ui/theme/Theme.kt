package com.example.sleepmonitoring.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


private val DarkColorScheme = darkColorScheme(

    primary = grafica,
    primaryContainer = onprimary,

    onPrimaryContainer = iconGlow,
    onPrimary = letra1,

    secondary = glow,
    onSecondary = letra2,

    background = panel,

    onSecondaryContainer = cardGlow,
    secondaryContainer = boton,

    outline = borde,

    tertiary = hover,
    onTertiary = grid,
    tertiaryContainer = shadow
)



private val BlueColorScheme = darkColorScheme(

    primary = grafica2,
    primaryContainer = boton2,

    onPrimaryContainer = iconGlow2,
    onPrimary = letra1_2,

    secondary = glow2,
    onSecondary = letra2_2,

    background = panel2,

    onSecondaryContainer = cardGlow2,
    secondaryContainer = boton2,

    outline = borde2,

    tertiary = hover2,
    onTertiary = grid2,
    tertiaryContainer = shadow2
)



object ConfiguracionTema {

    var tema by mutableStateOf("1")
        private set

    fun cambiarTema(nuevoTema: String) {
        tema = nuevoTema
    }
}

@Composable
fun SleepMonitoringTheme(
    content: @Composable () -> Unit
) {

    val esquemaActual = when (ConfiguracionTema.tema) {

        "1" -> DarkColorScheme

        "2" -> BlueColorScheme

        else -> DarkColorScheme
    }

    MaterialTheme(
        colorScheme = esquemaActual,
        typography = Typography,
        content = content
    )
}