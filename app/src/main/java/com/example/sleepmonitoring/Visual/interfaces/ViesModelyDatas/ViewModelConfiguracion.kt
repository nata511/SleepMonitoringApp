package com.example.sleepmonitoring.Visual.interfaces.ViesModelyDatas


import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import com.example.sleepmonitoring.Analisis.DatosCrudos.ManejoSensores.ProgramadorSueno
import com.example.sleepmonitoring.BaseDatos.SQLITE.AppDataBase
import com.example.sleepmonitoring.BasesDatosFirebase.ConsulFirebase
import com.example.sleepmonitoring.BasesDatosFirebase.Usuario
import com.example.sleepmonitoring.Visual.interfaces.Vistas.ConfiguracionAvatar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch

class ViewModelConfiguracion : ViewModel() {
    var nombre by mutableStateOf("")
    var nombreCambio by mutableStateOf("")
    var error by mutableStateOf<Boolean?>(null)
    var nombreFisrt = ""
    var FotoFisrt = ""
    var foto by mutableStateOf("")
    val context= LocalContext
    var generoAvatar by mutableStateOf(ConfiguracionAvatar.genero)

    init {
        viewModelScope.launch {
            val usuario = ConsulFirebase.ObtenerUsuario()
            if (usuario != null) {
                nombre = usuario.nombre
                nombreCambio = usuario.nombre
                nombreFisrt = usuario.nombre
                foto = usuario.avatar
                FotoFisrt = usuario.avatar
            }
        }
    }

    var cargando by mutableStateOf(false)

    fun CambiarNombre(nombre: String) {
        nombreCambio = nombre
    }

    fun Guardar() {
        val datos = mutableMapOf<String, Any>()

        if (nombreFisrt != nombreCambio) {
            if (nombreCambio.length < 3) {
                error = true
                return
            }
            error = false
            nombreFisrt = nombreCambio
            nombre = nombreCambio
            datos["nombre"] = nombreCambio
        }

        if (FotoFisrt != foto) {
            FotoFisrt = foto
            datos["avatar"] = foto
        }

        if (generoAvatar != ConfiguracionAvatar.genero) {
            datos["imagen"] = generoAvatar
            ConfiguracionAvatar.cambiar(generoAvatar)
        }

        if (datos.isEmpty()) return

        cargando = true
        viewModelScope.launch {
            ConsulFirebase.ModificarUsuario(datos)
            cargando = false
        }
    }

    fun cerrar(context: Context) {
        ConfiguracionAvatar.reiniciar()
        CoroutineScope(Dispatchers.IO).launch {
            limpiarHoras(context = context)
        }
        ConsulFirebase.cerrarSesion()
    }
    suspend fun limpiarHoras(context: Context) {
        AppDataBase.getDataBase(context).ConsultasDao().borrarHoras()
        ProgramadorSueno.programar(context)
    }
}