package com.example.sleepmonitoring.Visual.interfaces.ViesModelyDatas

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.sleepmonitoring.Controladores.ControladorAlmacenaje
import com.example.sleepmonitoring.Controladores.ControladorHora
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ViewModelRegis: ViewModel() {

    var datos by mutableStateOf(DatosRegis())
    var state by mutableStateOf(false)

    var err by mutableStateOf(false)

    fun cambiarCorreo(corr: String){ datos= datos.copy(correo = corr, errorCorreo = null) }
    fun cambiarContraseña(contra: String) { datos = datos.copy(contraseña = contra, errorContraseña = null) }
    fun cambiarConfiContraseña(confir: String) { datos = datos.copy(confirContra = confir, errorCoincidencia = null) }

    sealed interface RegistroUi{
        data class Error(val mensaje: String): RegistroUi
        object RegistroExitoso: RegistroUi
    }
    private val eventos= Channel<RegistroUi>()
    val eventosPublic= eventos.receiveAsFlow()

    fun RegistraSesion(navController: NavController): Unit{
        println("ENTRÓ A INICIAR SESIÓN")

        if (datos.correo.isNullOrBlank() or !datos.correo.contains("@")) {
            datos= datos.copy(errorCorreo = "El correo esta vacio o formato erroneo")
            return
        }
        println("ENTRÓ A } SESIÓN")
        if (datos.contraseña.isNullOrBlank()  || datos.contraseña.length<10) {
            datos= datos.copy( errorContraseña = "La contraseña esta vacia o es muy insegura")
            return
        }
        println("ENTRÓ A ñ SESIÓN")
        if (!ControladorHora.esAlfanumericoMixto(datos.contraseña)) {
            datos= datos.copy( errorContraseña = "La contraseña es insegura")
            return
        }
        println("ENTRÓ A { SESIÓN")
        if (datos.contraseña!=datos.confirContra  ) {
            datos= datos.copy( errorCoincidencia = "La contraseña no coincide")
            return
        }



        println("Pasa variables")
        viewModelScope.launch {
            try {
                if (state==false){
                    err=true
                    eventos.send(RegistroUi.Error("Falta aceptar terminos y condiciones"))
                    println(state)
                    return@launch
                }


                datos=datos.copy(cargando = true)


            var result= ControladorAlmacenaje.puenteRegistro(datos.correo,datos.contraseña)

                if (result != null) {
                    println("🔥 ENVIANDO ERROR AL CHANNEL")
                    eventos.send(RegistroUi.Error(result))
                } else {
                    navController.navigate("CuestionarioInicial")
                }
            datos=datos.copy(cargando = false)
        }catch (e: Exception){
                println("ERROR: ${e.message}")
        }
        }

}}