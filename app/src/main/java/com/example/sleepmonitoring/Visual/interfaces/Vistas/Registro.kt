package com.example.sleepmonitoring.Visual.interfaces.Vistas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sleepmonitoring.R
import com.example.sleepmonitoring.Visual.interfaces.ViesModelyDatas.ViewModelRegis
import com.example.sleepmonitoring.Visual.interfaces.ViesModelyDatas.ViewModelRegis.RegistroUi

@Composable
fun Registro(
    viewModel: ViewModelRegis = viewModel(),
    navaController: NavController,
    savedStateHandle: SavedStateHandle
) {

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(Unit) {
        println("🔥 INICIANDO ESCUCHA DEL CHANNEL")

        viewModel.eventosPublic.collect { evento ->

            println("🔥 EVENTO RECIBIDO POR LA UI: $evento")

            when (evento) {

                is RegistroUi.Error -> {
                    snackbarHostState.showSnackbar(
                        message = evento.mensaje,
                        duration = SnackbarDuration.Short
                    )
                }

                is RegistroUi.RegistroExitoso -> {
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
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background.copy(alpha = 0.30f),
                            MaterialTheme.colorScheme.background.copy(alpha = 0.68f),
                            MaterialTheme.colorScheme.background.copy(alpha = 0.92f)
                        )
                    )
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 18.dp,
                    vertical = 24.dp
                )
        ) {

            HeadRegis(
                viewModel = viewModel,
                navController = navaController,
                savedStateHandle = savedStateHandle
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
        ) { data ->

            Snackbar(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.shadow(
                    10.dp,
                    RoundedCornerShape(16.dp)
                )
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Estado",
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

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

@Composable
fun HeadRegis(
    viewModel: ViewModelRegis,
    navController: NavController,
    savedStateHandle: SavedStateHandle
) {

    var mostrarContraseña by remember {
        mutableStateOf(false)
    }

    val aceptado by savedStateHandle
        .getStateFlow("terminos_aceptados", false)
        .collectAsState()

    LaunchedEffect(aceptado) {
        viewModel.state = aceptado
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(88.dp)
                .shadow(
                    elevation = 14.dp,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .padding(4.dp)
        ) {

            Image(
                painter = painterResource(id = R.drawable.icono_app),
                contentDescription = "Logo de Dream Clue",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        }

        Text(
            text = "Crear Cuenta",
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 14.dp)
        )

        Text(
            text = "DREAM CLUE",
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            letterSpacing = 2.sp,
            modifier = Modifier.padding(top = 5.dp)
        )

        Spacer(modifier = Modifier.height(18.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(24.dp)
                )
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.primary)
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.secondary,
                            MaterialTheme.colorScheme.tertiary,
                            MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(
                    horizontal = 8.dp,
                    vertical = 18.dp
                )
        ) {

            Text(
                text = "Completa tus datos para comenzar a utilizar Dream Clue",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(
                    start = 15.dp,
                    end = 15.dp,
                    top = 5.dp,
                    bottom = 18.dp
                )
            )

            val coloresCampo = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedBorderColor = MaterialTheme.colorScheme.secondary,

                unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,

                unfocusedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedLabelColor = MaterialTheme.colorScheme.secondary,

                unfocusedLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedLeadingIconColor = MaterialTheme.colorScheme.secondary,

                unfocusedTrailingIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedTrailingIconColor = MaterialTheme.colorScheme.secondary,

                cursorColor = MaterialTheme.colorScheme.secondary,

                errorBorderColor = MaterialTheme.colorScheme.error,
                errorLabelColor = MaterialTheme.colorScheme.error,
                errorLeadingIconColor = MaterialTheme.colorScheme.error
            )

            OutlinedTextField(
                value = viewModel.datos.correo,
                onValueChange = {
                    viewModel.cambiarCorreo(it)
                },
                label = {
                    Text("Correo")
                },
                isError = viewModel.datos.errorCorreo != null,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = "Correo"
                    )
                },
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                colors = coloresCampo,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                enabled = !viewModel.datos.cargando
            )

            viewModel.datos.errorCorreo?.let {
                ErrorRegistro(it)
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = viewModel.datos.contraseña,
                onValueChange = {
                    viewModel.cambiarContraseña(it)
                },
                label = {
                    Text("Contraseña")
                },
                isError = viewModel.datos.errorContraseña != null,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = "Contraseña"
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            mostrarContraseña = !mostrarContraseña
                        }
                    ) {
                        Icon(
                            imageVector = if (mostrarContraseña) {
                                Icons.Filled.VisibilityOff
                            } else {
                                Icons.Filled.Visibility
                            },
                            contentDescription = if (mostrarContraseña) {
                                "Ocultar Contraseña"
                            } else {
                                "Ver Contraseña"
                            }
                        )
                    }
                },
                visualTransformation = if (mostrarContraseña) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                colors = coloresCampo,
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                enabled = !viewModel.datos.cargando
            )

            viewModel.datos.errorContraseña?.let {
                ErrorRegistro(it)
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = viewModel.datos.confirContra,
                onValueChange = {
                    viewModel.cambiarConfiContraseña(it)
                },
                label = {
                    Text("Confirmar Contraseña")
                },
                isError = viewModel.datos.errorConfir != null,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.LockReset,
                        contentDescription = "Confirmar contraseña"
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            mostrarContraseña = !mostrarContraseña
                        }
                    ) {
                        Icon(
                            imageVector = if (mostrarContraseña) {
                                Icons.Filled.VisibilityOff
                            } else {
                                Icons.Filled.Visibility
                            },
                            contentDescription = if (mostrarContraseña) {
                                "Ocultar Contraseña"
                            } else {
                                "Ver Contraseña"
                            }
                        )
                    }
                },
                visualTransformation = if (mostrarContraseña) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                colors = coloresCampo,
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                enabled = !viewModel.datos.cargando
            )

            viewModel.datos.errorCoincidencia?.let {
                ErrorRegistro(it)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 12.dp
                    )
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        MaterialTheme.colorScheme.primaryContainer.copy(
                            alpha = 0.45f
                        )
                    )
                    .clickable {
                        navController.navigate("Terminos")
                    }
                    .padding(
                        horizontal = 6.dp,
                        vertical = 4.dp
                    )
            ) {

                Checkbox(
                    checked = aceptado,
                    onCheckedChange = {
                        savedStateHandle["terminos_aceptados"] = it
                    }
                )

                Text(
                    text = "Acepto los términos y condiciones",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodySmall,
                    textDecoration = TextDecoration.Underline
                )
            }

            if (viewModel.err) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(
                        start = 15.dp,
                        top = 5.dp
                    )
                ) {

                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(14.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "Acepte los términos y condiciones",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Button(
                onClick = {
                    viewModel.RegistraSesion(navController)
                    println("guardar")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(14.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 7.dp,
                    pressedElevation = 2.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 15.dp,
                        vertical = 15.dp
                    )
                    .height(54.dp)
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
                    text = "Crear Cuenta",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 3.dp,
                        bottom = 5.dp
                    )
            ) {

                Text(
                    text = "¿Ya tienes una cuenta?",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "  Inicia sesión",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navController.navigate("Inicio")
                    }
                )

                Spacer(modifier = Modifier.width(3.dp))

                Icon(
                    imageVector = Icons.Filled.ArrowRight,
                    contentDescription = "Ir a inicio de sesión",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(17.dp)
                )
            }
        }
    }
}

@Composable
private fun ErrorRegistro(texto: String) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 5.dp
            )
    ) {

        Icon(
            imageVector = Icons.Filled.Warning,
            contentDescription = "Error",
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(13.dp)
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = texto,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodySmall
        )
    }
}