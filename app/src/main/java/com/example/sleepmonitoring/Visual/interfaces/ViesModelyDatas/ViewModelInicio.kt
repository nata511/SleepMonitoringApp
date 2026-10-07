package com.example.sleepmonitoring.Visual.interfaces.ViesModelyDatas

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.sleepmonitoring.Controladores.ControladorAlmacenaje
import com.example.sleepmonitoring.Visual.interfaces.ViesModelyDatas.ViewModelRegis.RegistroUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sleepmonitoring.Controladores.ControladorHora
import kotlinx.coroutines.launch
import okio.utf8Size

class ViewModelInicio: ViewModel() {
    val scope = CoroutineScope(Dispatchers.IO)
    var state by mutableStateOf(false)
    var datos by mutableStateOf(DatosInicio())
    var err by mutableStateOf(false)
    sealed interface SesionUi{
        data class Error(val mensaje: String): SesionUi
        object RegistroExitoso: SesionUi
    }
    private val eventos= Channel<SesionUi>()
    val eventosPublic= eventos.receiveAsFlow()

    fun cambiarCorreo(corr: String){ datos= datos.copy(correo = corr, errorCorreo = null) }
    fun cambiarContraseña(contra: String) { datos = datos.copy(contraseña = contra, errorContraseña =null) }

    fun IniciarSesion(navController: NavController){
        println("ENTRÓ A INICIAR SESIÓN")

        if (datos.correo.isNullOrBlank() or !datos.correo.contains("@")) {
            datos= datos.copy(errorCorreo = "El correo esta vacio o formato erroneo")
            return
        }
        if (datos.contraseña.isNullOrBlank() || datos.contraseña.length<10 ) {
            datos= datos.copy( errorContraseña = "La contraseña debe tener minimo 10 caracteres ")
            return
        }
        if (!ControladorHora.esAlfanumericoMixto(datos.contraseña)) {
            datos= datos.copy( errorContraseña = "La contraseña no cumple con los requisitos")
            return
        }

        viewModelScope.launch {
            try {
                datos=datos.copy(cargando = true)


                var result = ControladorAlmacenaje.puenteInicio(datos.correo, datos.contraseña)
                if (result != null) {
                    eventos.send(SesionUi.Error(result))
                } else {
                    val validar = ControladorAlmacenaje.validarDatosUsuraio()
                    if (validar==null){
                        navController.navigate("Principal")
                    }else{
                    navController.navigate("CuestionarioInicial")

                }

                }
                datos=datos.copy(cargando = false)


            }catch (e: Exception) {
                    println("ERROR: ${e.message}")
            }        }


}}