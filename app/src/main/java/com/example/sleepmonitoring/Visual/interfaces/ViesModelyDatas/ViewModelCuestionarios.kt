package com.example.sleepmonitoring.Visual.interfaces.ViesModelyDatas

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.sleepmonitoring.BasesDatosFirebase.DatosCuestionario
import com.example.sleepmonitoring.BasesDatosFirebase.DatosSomnolencia
import com.example.sleepmonitoring.Controladores.ControladorTests
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher


class ViewModelCuestionarios(): ViewModel() {
    var datos by mutableStateOf(pregunta())
    var final by mutableStateOf(false)
    var opcion by mutableStateOf(-1)
    var ind by mutableStateOf(0)

    val esUltimaPregunta: Boolean
        get() = ind == valores.size - 1
    var valores: MutableMap<String, MutableList<String>> = mutableMapOf()
    var Lista = mutableListOf<String>()
    var clase = 0
    var progresp by mutableStateOf(0.0F)
    var respuestas = mutableListOf<Int>()
    fun Deteccion(tipo: Int) {
        clase = tipo
        ind = 0
        progresp = 0f
        opcion = -1
        when (tipo) {
            0 -> { valores = Preguntas.preguntasDiarias; valor = 1f / 5 }
            1 -> { valores = Preguntas.preguntasEpworth; valor = 1f / 8 }
        }
        respuestas = MutableList(valores.size) { -1 }
        val claves = valores.keys.toList()
        val opciones = valores.values.toList()
        datos = pregunta(claves[0], opciones[0][0], opciones[0][1], opciones[0][2], opciones[0][3])
    }

    var valor = 0F


    fun Iniciar(navController: NavController) {
        val claves = valores.keys.toList()
        val opciones = valores.values.toList()

        if (opcion < 0) return
        respuestas[ind] = opcion

        if (ind == claves.lastIndex) {
            val textos = respuestas.mapIndexed { i, o -> opciones[i][o] }
            val claseActual = clase

            viewModelScope.launch {                       // Main por defecto
                try {
                    withContext(Dispatchers.IO) {         // trabajo pesado
                        if (claseActual == 0) {
                            ControladorTests.objetosEncuestas(
                                DatosCuestionario(
                                    calidad = textos[0],
                                    malestar = textos[1],
                                    rendimiento = textos[2],
                                    Somnolencia = textos[3],
                                    medi = textos[4]
                                )
                            )
                        } else {
                            ControladorTests.ConexionSomno(
                                RespuestasEpworth(
                                    textos[0], textos[1], textos[2], textos[3],
                                    textos[4], textos[5], textos[6], textos[7]
                                )
                            )
                        }
                    }
                    navController.navigate("MenuTests")   // de vuelta en Main
                } catch (e: Exception) {
                    Log.e("CUESTIONARIO", "Error al finalizar", e)
                }
            }
            return
        }

        ind++
        progresp += valor
        cargarPregunta(claves, opciones)
    }
    fun retornarDatos(): DatosCuestionario {

        return DatosCuestionario(
            calidad = Lista[0], Somnolencia = Lista[2], Lista[1],
            rendimiento = Lista[3], medi = Lista[4]
        )
    }

    fun anterior() {
        if (ind == 0) return
        ind--
        progresp -= valor
        cargarPregunta(valores.keys.toList(), valores.values.toList())
    }

    private fun cargarPregunta(claves: List<String>, opciones: List<List<String>>) {
        datos = datos.copy(
            pregunta = claves[ind],
            opcion1 = opciones[ind][0], opcion2 = opciones[ind][1],
            opcion3 = opciones[ind][2], opcion4 = opciones[ind][3]
        )
        opcion = respuestas[ind]
    }
}

data class pregunta(
    val pregunta: String="",
    val opcion1:String="",
    val opcion2:String="",
    val opcion3: String="",
    val opcion4:String=""
)