package com.example.sleepmonitoring.Visual.interfaces.Vistas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.sleepmonitoring.R
import com.example.sleepmonitoring.ui.theme.ConfiguracionTema


enum class EstadoSueno { BIEN_DESCANSADO, ALGO_CANSADO, MUY_CANSADO, MUERTO }

fun estadoPorDuracion(minutos: Long): EstadoSueno {
    val horas = minutos / 60.0
    return when {
        horas >= 8 -> EstadoSueno.BIEN_DESCANSADO
        horas >= 6 -> EstadoSueno.ALGO_CANSADO
        horas >= 3 -> EstadoSueno.MUY_CANSADO
        else -> EstadoSueno.MUERTO
    }
}

object CatalogoAvatares {


    fun familia(tema: String): String = if (tema == "1") "2" else "1"

    fun obtener(tema: String, genero: String, estado: EstadoSueno): Int {
        val clave = familia(tema) + genero.uppercase()
        return when (clave) {
            "1H" -> when (estado) {
                EstadoSueno.BIEN_DESCANSADO -> R.drawable.iconopersona1h_bien_descansado
                EstadoSueno.ALGO_CANSADO -> R.drawable.iconopersona1h_algo_cansado
                EstadoSueno.MUY_CANSADO -> R.drawable.iconopersona1h_muy_cansada
                EstadoSueno.MUERTO -> R.drawable.iconopersona1h_muerto
            }
            "1M" -> when (estado) {
                EstadoSueno.BIEN_DESCANSADO -> R.drawable.iconopersona1m_bien_descansado
                EstadoSueno.ALGO_CANSADO -> R.drawable.iconopersona1m_algo_cansado
                EstadoSueno.MUY_CANSADO -> R.drawable.iconopersona1m_muy_cansada
                EstadoSueno.MUERTO -> R.drawable.iconopersona1m_muerto
            }
            "2H" -> when (estado) {
                EstadoSueno.BIEN_DESCANSADO -> R.drawable.iconopersona2h_bien_descansado
                EstadoSueno.ALGO_CANSADO -> R.drawable.iconopersona2h_algo_cansado
                EstadoSueno.MUY_CANSADO -> R.drawable.iconopersona2h_muy_cansada
                EstadoSueno.MUERTO -> R.drawable.iconopersona2h_muerto
            }
            else -> when (estado) {
                EstadoSueno.BIEN_DESCANSADO -> R.drawable.iconopersona2m_bien_descansado
                EstadoSueno.ALGO_CANSADO -> R.drawable.iconopersona2m_algo_cansado
                EstadoSueno.MUY_CANSADO -> R.drawable.iconopersona2m_muy_cansada
                EstadoSueno.MUERTO -> R.drawable.iconopersona2m_muerto
            }
        }
    }
}

@Composable
fun ImagenAvatar(
    recurso: Int,
    tamano: Dp = 120.dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(tamano),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = recurso),
            contentDescription = "Avatar",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(tamano)
        )
    }
}

@Composable
fun AvatarSueno(minutosDormidos: Long, tamano: Dp = 180.dp) {
    val estado = estadoPorDuracion(minutosDormidos)
    val recurso = CatalogoAvatares.obtener(
        tema = ConfiguracionTema.tema,
        genero = ConfiguracionAvatar.genero,
        estado = estado
    )
    ImagenAvatar(recurso, tamano)
}

object ConfiguracionAvatar {
    var genero by mutableStateOf("H")
        private set
    var cargado = false
        private set
    
    fun cargar(valor: String) {
        genero = if (valor == "M") "M" else "H"
        cargado = true
    }

    fun cambiar(valor: String) {
        genero = valor
    }

    fun reiniciar() {
        genero = "H"
        cargado = false
    }
}