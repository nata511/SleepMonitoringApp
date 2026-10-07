package com.example.sleepmonitoring.Visual.interfaces.ViesModelyDatas

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.sleepmonitoring.Controladores.ControladorAlmacenaje
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

data class DatosRecuperacion(
    val correo: String = "",
    val errorCorreo: String? = null,
    val cargando: Boolean = false
)

class ViewModelRecuperacion : ViewModel() {

    var datos by mutableStateOf(DatosRecuperacion())

    sealed interface RecuperacionUi {
        data class Error(val mensaje: String) : RecuperacionUi
        object EnvioExitoso : RecuperacionUi
    }

    private val eventos = Channel<RecuperacionUi>()
    val eventosPublic = eventos.receiveAsFlow()

    fun cambiarCorreo(corr: String) {
        datos = datos.copy(correo = corr, errorCorreo = null)
    }

    fun enviarRecuperacion(navController: NavController) {
        if (datos.cargando) return

        if (datos.correo.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(datos.correo).matches()) {
            datos = datos.copy(errorCorreo = "El correo esta vacio o formato erroneo")
            return
        }

        viewModelScope.launch {
            datos = datos.copy(cargando = true)
            try {
                val result = ControladorAlmacenaje.puenteRecuperacion(datos.correo.trim())
                if (result != null) {
                    eventos.send(RecuperacionUi.Error(result))
                } else {
                    navController.navigate("Inicio")
                }
            } catch (e: Exception) {
                println("ERROR: ${e.message}")
                eventos.send(RecuperacionUi.Error("No se pudo enviar el mensaje, intenta de nuevo"))
            } finally {
                datos = datos.copy(cargando = false)
            }
        }
    }
}