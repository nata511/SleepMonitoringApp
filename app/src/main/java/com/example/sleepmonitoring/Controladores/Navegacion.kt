package com.example.sleepmonitoring.Controladores

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.sleepmonitoring.Visual.interfaces.Vistas.InicioSesion
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import androidx.navigation.compose.composable
import com.example.sleepmonitoring.Visual.interfaces.Vistas.PrincipalInterfaz
import com.example.sleepmonitoring.Visual.interfaces.Vistas.Registro
import android.content.Context
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import okio.blackholeSink
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.sleepmonitoring.Visual.interfaces.Vistas.CuestionarioInicial
import com.example.sleepmonitoring.Visual.interfaces.Vistas.MenuTests
import com.example.sleepmonitoring.Visual.interfaces.Vistas.RestablecerContrasena
import com.example.sleepmonitoring.Visual.interfaces.Vistas.ResultadosTests
import com.example.sleepmonitoring.Visual.interfaces.Vistas.Terminos
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.dreamclue.ui.configuracion.ConfiguracionVisual
import com.example.sleepmonitoring.Visual.interfaces.Vistas.Cuestionarios
import com.example.sleepmonitoring.Visual.interfaces.Vistas.Inicial

@Composable
fun Navegacion(start:String){
    val navController= rememberNavController()
    val id= Firebase.auth
    var selectedItem by rememberSaveable {
        mutableIntStateOf(0)
    }
    NavHost(
       navController =  navController, startDestination = start
    ){
        composable("Inicio"){
            InicioSesion(navController = navController)
        }
        composable("Principal"){
            PrincipalInterfaz( navController = navController,selectedItem = selectedItem,
                onTabSelected = {
                    selectedItem = it
                })
        }
        composable("Registro") { entry ->
            Registro(
                navaController = navController,
                savedStateHandle = entry.savedStateHandle
            )
        }
        composable(
            route = "Cuestionario/{tipo}",
            arguments = listOf(
                navArgument("tipo"){
                    type = NavType.IntType
                }
            )
        ){datos->
            val valor=datos.arguments?.getInt("tipo")?:0
            Cuestionarios(valor, navController = navController,selectedItem = selectedItem,
                onTabSelected = {
                    selectedItem = it
                })
        }
        composable("Terminos") { Terminos(navController) }

        composable ("CuestionarioInicial"){
            CuestionarioInicial(navController=navController)
        }
        composable ("MenuTests"){
            MenuTests(navController = navController,selectedItem = selectedItem,
                onTabSelected = {
                    selectedItem = it
                })
        }

        composable(
             route = "Resultados/{tipo}", arguments = listOf(
                 navArgument("tipo"){
                     type = NavType.IntType
                 }
             )
         ){datos->
             val valor=datos.arguments?.getInt("tipo")?:0
             ResultadosTests(navController, tipo = valor,selectedItem = selectedItem,
                 onTabSelected = {
                     selectedItem = it
                 })
         }
        composable("Restablecer") {
            RestablecerContrasena(navController=navController)
        }
        composable("Configuracion") {
            ConfiguracionVisual(navController=navController,selectedItem = selectedItem,
                onTabSelected = {
                    selectedItem = it
                })
        }
        composable("Validacion") {
            Inicial(navController)
        }

    }
}