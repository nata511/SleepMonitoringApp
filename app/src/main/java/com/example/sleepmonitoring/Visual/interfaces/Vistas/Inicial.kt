package com.example.sleepmonitoring.Visual.interfaces.Vistas

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.example.sleepmonitoring.BasesDatosFirebase.ConsulFirebase
import com.example.sleepmonitoring.Controladores.ControladorAlmacenaje
import com.example.sleepmonitoring.ui.theme.ConfiguracionTema

@Composable
fun Inicial(navController: NavController) {

    LaunchedEffect(Unit) {

        val validar = ControladorAlmacenaje.validarDatosUsuraio()
        cargarConfiguracionUsuario()

        navController.navigate(
            if (validar == null)
                "Principal"


            else
                "CuestionarioInicial"
        ) {
            popUpTo("Validacion") {
                inclusive = true
            }
        }
    }
}
suspend fun cargarConfiguracionUsuario() {
    val usuario = ConsulFirebase.ObtenerUsuario() ?: return

    ConfiguracionTema.cambiarTema(usuario.tema)

    if (!ConfiguracionAvatar.cargado) {
        ConfiguracionAvatar.cargar(usuario.imagen)
    }
}