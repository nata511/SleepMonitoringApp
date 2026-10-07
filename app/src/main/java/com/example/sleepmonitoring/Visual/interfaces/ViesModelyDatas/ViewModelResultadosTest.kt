package com.example.sleepmonitoring.Visual.interfaces.ViesModelyDatas

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.sleepmonitoring.BasesDatosFirebase.DatosCuestionario
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.sleepmonitoring.BasesDatosFirebase.ConsulFirebase
import com.example.sleepmonitoring.BasesDatosFirebase.DatosResultados
import com.example.sleepmonitoring.BasesDatosFirebase.Dimesion
import com.example.sleepmonitoring.BasesDatosFirebase.tempo
import com.example.sleepmonitoring.BasesDatosFirebase.textosTest
import com.example.sleepmonitoring.Controladores.ControladorTests
import kotlinx.coroutines.launch
class ViewModelResultadosTest : ViewModel() {
    var datos by mutableStateOf(DatosResultados())
    var variables by mutableStateOf(tempo())
    var dimen by mutableStateOf(Dimesion())
    var sobre by mutableStateOf("")
    var cargando by mutableStateOf(true)
    var sinDatos by mutableStateOf(false)
    val textos = textosTest()
    private var tipoCargado = -1

    fun Deteccion(tipo: Int) {
        // en Deteccion(), envolviendo todo el cuerpo de la corrutina
        try { /* cuerpo actual */
        if (tipo !in 0..3 || tipo == tipoCargado) return
        tipoCargado = tipo

        viewModelScope.launch {
            cargando = true
            sinDatos = false
            variables = tempo()

            val resultado = when (tipo) {
                0 -> ControladorTests.ConsultasResultadoPSQI()
                1 -> ControladorTests.ConsultasResultadoAtenea()
                2 -> ControladorTests.ConsultasResultadoSomnolencia()
                else -> ControladorTests.ConsultasResultadoMCTQ()
            }
            dimen = resultado

            val (iPuntaje, iRiesgo) = when (tipo) {
                0 -> 8 to 7
                1 -> 9 to 8
                2 -> 9 to 8
                else -> 8 to 9
            }
            val r = resultado.resultados
            val p= resultado.puntajes

            if (r.size <= maxOf(iPuntaje, iRiesgo)) {
                sinDatos = true
                cargando = false
                tipoCargado = -1
                return@launch
            }

            val semanal = when (tipo) {
                0 -> ConsulFirebase.datosSemanalPSQI()
                1 -> ConsulFirebase.datosSemanalAtenea()
                2 -> ConsulFirebase.datosSemanalSomnolencia()
                else -> mutableMapOf()
            }

            val descripciones = when (tipo) {
                0 -> resultado.descripcionesPSQI
                1 -> resultado.descripcionesAtenea
                2 -> resultado.descripcionesSomnolencia
                else -> resultado.descripcionesMCTQ
            }

            sobre = when (tipo) {
                0 -> "/21"
                1, 2 -> "/24"
                else -> ""
            }

            datos = datos.copy(
                nombreTest = textos.nombres[tipo],
                DescripcionTest = textos.descripciones[tipo],
                puntaje = r[iPuntaje],
                riesgo = r[iRiesgo],
                clasifiaciones = textos.listaDeRangos[tipo],
                valoresClasi = textos.tamañosRangos[tipo],
                mutableList = semanal
            )
            variables = variables.copy(descripciones = descripciones, resultados = r, puntajes = p)
            cargando = false
        }
    }catch (e: Exception) {
            Log.e("RESULTADOS", "Fallo en tipo $tipo", e)
        cargando = false; sinDatos = true; tipoCargado = -1
    }
}}