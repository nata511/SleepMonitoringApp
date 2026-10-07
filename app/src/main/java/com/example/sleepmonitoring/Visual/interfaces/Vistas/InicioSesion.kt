package com.example.sleepmonitoring.Visual.interfaces.Vistas

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sleepmonitoring.R
import com.example.sleepmonitoring.Visual.interfaces.ViesModelyDatas.ViewModelInicio


@Composable
fun InicioSesion(
    viewModel: ViewModelInicio = viewModel(), navController: NavController
) {

    val snackbarHostState = remember { SnackbarHostState() }

    // Solo para el diseño del snackbar: distingue mensaje de error vs. éxito
    var esError by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.eventosPublic.collect { evento ->
            when (evento) {
                is ViewModelInicio.SesionUi.Error -> {
                    esError = true
                    snackbarHostState.showSnackbar(
                        message = evento.mensaje,
                        duration = SnackbarDuration.Short
                    )
                }

                is ViewModelInicio.SesionUi.RegistroExitoso -> {
                    esError = false
                    snackbarHostState.showSnackbar(
                        message = "¡Registro Exitoso!",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Velo degradado para que el contenido se lea mejor sobre la imagen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background.copy(alpha = 0.25f),
                            MaterialTheme.colorScheme.background.copy(alpha = 0.70f)
                        )
                    )
                )
        )

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 24.dp)
        ) {
            Head()
            Spacer(modifier = Modifier.height(8.dp))
            Card_Info(viewModel, navController)
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(16.dp)
        ) { data ->
            Snackbar(
                shape = RoundedCornerShape(16.dp),
                containerColor = if (esError) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.secondary,
                contentColor = if (esError) MaterialTheme.colorScheme.onError
                else MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.shadow(10.dp, RoundedCornerShape(16.dp))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (esError) Icons.Filled.Warning else Icons.Filled.Check,
                        contentDescription = if (esError) "Error" else "Éxito",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = data.visuals.message,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

    }

}


@Preview
@Composable
fun Head() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {

        // Logo con aro de acento
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(96.dp)
                .shadow(12.dp, CircleShape)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    brush = Brush.linearGradient(
                        listOf(
                            MaterialTheme.colorScheme.secondary,
                            MaterialTheme.colorScheme.tertiary
                        )
                    ),
                    shape = CircleShape
                )
                .padding(4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.icono_app),
                contentDescription = "Descripcióxn de la imagen para accesibilidad",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        }

        Text(
            text = "DREAM CLUE",
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 3.sp,
            modifier = Modifier.padding(top = 14.dp)
        )
        Text(
            text = "MONITOREO SOBRE TU SUEÑO",
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.bodyMedium,
            letterSpacing = 2.sp,
            modifier = Modifier.padding(top = 6.dp, bottom = 20.dp)
        )

    }
}


/** Mensaje de error en forma de etiqueta, bajo el campo correspondiente (solo diseño). */
@Composable
private fun MensajeError(texto: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, top = 6.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.error.copy(alpha = 0.14f))
            .padding(horizontal = 10.dp, vertical = 7.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Warning,
            contentDescription = "Error",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = texto,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
    }
}


@Composable
fun Card_Info(viewModel: ViewModelInicio, navController: NavController) {
    var mostrarContraseña by remember { mutableStateOf(false) }

    val formaCampo = RoundedCornerShape(14.dp)
    val coloresCampo = OutlinedTextFieldDefaults.colors(
        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
        focusedBorderColor = MaterialTheme.colorScheme.secondary,

        unfocusedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
        focusedLabelColor = MaterialTheme.colorScheme.secondary,

        errorBorderColor = MaterialTheme.colorScheme.error,
        errorLabelColor = MaterialTheme.colorScheme.error,

        focusedLeadingIconColor = MaterialTheme.colorScheme.secondary,
        unfocusedLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
        focusedTrailingIconColor = MaterialTheme.colorScheme.secondary,
        unfocusedTrailingIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
        cursorColor = MaterialTheme.colorScheme.secondary
    )

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 14.dp, shape = RoundedCornerShape(24.dp))
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(24.dp)
            )
            .border(
                2.dp, Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.secondary,
                        MaterialTheme.colorScheme.tertiary,
                        MaterialTheme.colorScheme.secondary
                    )

                ), shape = RoundedCornerShape(24.dp)
            )
            .padding(vertical = 14.dp, horizontal = 8.dp)
            .animateContentSize()
    ) {
        Text(
            text = "Iniciar Sesion",
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 10.dp, start = 15.dp)
        )
        Text(
            text = "Monitorea tu sueño y ve tus estadisticas cada dia",
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 4.dp, bottom = 16.dp)
        )

        OutlinedTextField(
            value = viewModel.datos.correo,
            onValueChange = { viewModel.cambiarCorreo(it) },
            label = { Text(" Correo") },
            isError = viewModel.datos.errorCorreo != null,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Email,
                    contentDescription = "Cerradura"
                )
            },
            shape = formaCampo,
            singleLine = true,
            colors = coloresCampo,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            enabled = !viewModel.datos.cargando
        )

        if (viewModel.datos.errorCorreo != null) {
            MensajeError(viewModel.datos.errorCorreo ?: "")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = viewModel.datos.contraseña,
            onValueChange = { viewModel.cambiarContraseña(it) },
            label = { Text(" Contraseña") },

            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = "Cerradura"
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = { mostrarContraseña = !mostrarContraseña }
                ) {
                    Icon(
                        imageVector = if (mostrarContraseña) {
                            Icons.Filled.VisibilityOff
                        } else {
                            Icons.Filled.Visibility

                        }, contentDescription = if (mostrarContraseña) {
                            "Ocultar Contraseña"
                        } else {
                            "Ver Contraseña"
                        }
                    )
                }
            }, visualTransformation =
                if (mostrarContraseña)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
            shape = formaCampo,
            singleLine = true,
            colors = coloresCampo,
            isError = viewModel.datos.errorContraseña != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            enabled = !viewModel.datos.cargando
        )
        if (viewModel.datos.errorContraseña != null) {
            MensajeError(viewModel.datos.errorContraseña ?: "")
        }

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 15.dp, end = 15.dp)
        ) {

            Text(
                text = "¿Olvidaste Tu Contraseña",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.bodySmall,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { navController.navigate("Restablecer") }
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            )

        }

        Button(
            onClick = {
                println("CLICK LOGIN")
                viewModel.IniciarSesion(navController)
            },
            shape = RoundedCornerShape(14.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 8.dp,
                pressedElevation = 2.dp
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.tertiary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 18.dp)
                .height(54.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            if (viewModel.datos.cargando) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
            Text(
                text = "Iniciar Sesion",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp, bottom = 10.dp, start = 15.dp, end = 15.dp)
        ) {
            Text(
                text = "¿Aun no tienes cuenta?",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.clickable {
                    navController.navigate("Registro")
                }
            )
            Text(
                text = "  Registrate aqui",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    navController.navigate("Registro")

                }
            )
        }
    }

}