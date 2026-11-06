package com.example.sleepmonitoring.Visual.interfaces.ViesModelyDatas

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import com.example.sleepmonitoring.BasesDatosFirebase.ConsulFirebase
import com.example.sleepmonitoring.Controladores.ControladorAlmacenaje
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import com.example.sleepmonitoring.Analisis.DatosCrudos.SensorService
import com.example.sleepmonitoring.BasesDatosFirebase.DatosConstantes
import kotlinx.coroutines.launch

class ViewModelPrincipal: ViewModel(){
    var datos by mutableStateOf(DatosPrincipal())
    var explicacionRacha by mutableStateOf("")
    var estadoTest by mutableStateOf(false)
    val botonReloj: Boolean get() = DatosConstantes.Encendido
    var corr by mutableStateOf(false)
    init {
        CargarBase()

        viewModelScope.launch { ConsulFirebase.actualizar.collect { CargarBase() } }
        viewModelScope.launch { ConsulFirebase.estadoTest.collect { estadoTest = true } }
    }


    fun CargarBase() {
        viewModelScope.launch {
            val nuevo = ControladorAlmacenaje.ObtenerDatosPrincipal()
            println("holoi"+nuevo)
            if(nuevo?.riesgo=="Sin datos"){

                DatosConstantes.sinDatos=false
                return@launch
            }
            DatosConstantes.sinDatos=true
            datos = nuevo?: DatosPrincipal()
        }
    }

    fun iniciarSensor(context: Context) {
        val intent = Intent(context, SensorService::class.java)
        ContextCompat.startForegroundService(context, intent)
    }

    fun finalizarSensor(context: Context) {
        val intent = Intent(context, SensorService::class.java)
            .setAction(SensorService.ACCION_STOP)
        ContextCompat.startForegroundService(context, intent)
    }

}