package com.example.sleepmonitoring.Visual.interfaces.ViesModelyDatas

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.sleepmonitoring.BasesDatosFirebase.Usuario
import com.example.sleepmonitoring.Controladores.ControladorAlmacenaje
import com.example.sleepmonitoring.Visual.interfaces.ViesModelyDatas.valoresCuestionario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

class ViewModelCuestionarioInicial : ViewModel() {
    var estado by mutableStateOf(false)
    var preguntas by mutableStateOf(preguntasInciales())
    var datosH by mutableStateOf(Usuario())
    var nuevaLista by mutableStateOf(setOf<Int>())
        private set
    var error by mutableStateOf("")
    var datosI by mutableStateOf(
        valoresCuestionario(
            pregunta = preguntas.preguntas[0],
            descripcion = preguntas.descripciones[0],
            labeli = preguntas.labels[0],
            valor = 0
        )
    )

    private val totalPasos get() = preguntas.preguntas.size
    private var ind = 0

    val progreso: Float
        get() = ind.toFloat() / (totalPasos - 1).coerceAtLeast(1)

    fun agregarLista(valor: Int) {
        nuevaLista = nuevaLista + valor
        datosH = datosH.copy(diasLibres = nuevaLista.toMutableList())
    }

    fun quitarLista(valor: Int) {
        nuevaLista = nuevaLista - valor
        datosH = datosH.copy(diasLibres = nuevaLista.toMutableList())
    }

    fun CambiarFecha(fech: String) {
        datosH = datosH.copy(fechaNcimiento = fech)
    }

    fun CambiarNombre(nombre: String) {
        datosI = datosI.copy(labeli = nombre)
        datosH = datosH.copy(nombre = nombre)
    }

    fun CambiarHora(hor: String) {
        datosH = if (datosI.valor == 2) datosH.copy(horaAcostar = hor)
        else datosH.copy(horaLevantar = hor)
    }

    private fun validarPasoActual(): String? = when (datosI.valor) {
        0 -> if (datosH.nombre.length < 3)
            "El nombre está vacío o tiene menos de 3 caracteres" else null
        1 -> if (datosH.fechaNcimiento.isNullOrEmpty())
            "Debes seleccionar tu fecha de nacimiento" else null
        2 -> if (datosH.horaAcostar.isNullOrEmpty())
            "Debes seleccionar la hora de acostarte" else null
        3 -> if (datosH.horaLevantar.isNullOrEmpty())
            "Debes seleccionar la hora de levantarte" else null
        4 -> if (nuevaLista.isEmpty())
            "Debes seleccionar al menos un día libre" else null
        else -> null
    }

    private fun actualizarPaso() {
        datosI = datosI.copy(
            pregunta = preguntas.preguntas[ind],
            descripcion = preguntas.descripciones[ind],
            labeli = if (ind == 0) datosH.nombre else preguntas.labels[ind],
            valor = ind
        )
        estado = ind == totalPasos - 1
    }

    fun Siguiente() {
        val mensaje = validarPasoActual()
        if (mensaje != null) {
            error = mensaje
            return
        }
        error = ""
        if (ind < totalPasos - 1) {
            ind++
            actualizarPaso()
        }
    }

    fun anterior() {
        if (ind > 0) {
            error = ""
            ind--
            actualizarPaso()
        }
    }
}