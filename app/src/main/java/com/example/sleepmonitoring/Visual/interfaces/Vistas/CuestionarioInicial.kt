package com.example.sleepmonitoring.Visual.interfaces.Vistas

import android.app.TimePickerDialog
import androidx.compose.runtime.Composable
import androidx.compose.material3.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.sleepmonitoring.Analisis.DatosCrudos.VerificadorHora
import com.example.sleepmonitoring.BaseDatos.SQLITE.Horas
import com.example.sleepmonitoring.Controladores.ControladorAlmacenaje
import com.example.sleepmonitoring.Visual.interfaces.ViesModelyDatas.ViewModelCuestionarioInicial
import java.time.LocalTime
import java.util.Calendar
import java.util.concurrent.TimeUnit


@Composable
fun CuestionarioInicial(
    viewModel: ViewModelCuestionarioInicial = viewModel(),
    navController: NavController
) {
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(450.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )
        ) {

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                Text(
                    text = "Cuéntanos sobre ti",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Con estos datos configuramos tu usuario y tu sensor de sueño.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                LinearProgressIndicator(
                    progress = { viewModel.progreso },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(50)),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.primaryContainer
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                ) {

                    Text(
                        text = viewModel.datosI.pregunta,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    Text(
                        text = viewModel.datosI.descripcion,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    if (viewModel.datosI.valor == 0) {

                        OutlinedTextField(
                            value = viewModel.datosI.labeli,
                            onValueChange = { viewModel.CambiarNombre(it) },
                            modifier = Modifier.fillMaxWidth(),
                            label = {
                                Text(
                                    "Nombre",
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            },
                            placeholder = {
                                Text(
                                    "Escribe tu nombre",
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )

                        if (viewModel.error != "") {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Warning,
                                    contentDescription = "Error",
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(10.dp)
                                )

                                Text(
                                    text = viewModel.error,
                                    color = MaterialTheme.colorScheme.secondary,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                    } else if (viewModel.datosI.valor == 1) {

                        SelectorFecha(viewModel)

                    } else if (viewModel.datosI.valor == 2) {

                        SelectorHora(
                            "Hora de Acostarse",
                            viewModel
                        )

                    } else if (viewModel.datosI.valor == 3) {

                        SelectorHora(
                            "Hora de Levantarse",
                            viewModel
                        )

                    } else if (viewModel.datosI.valor == 4) {

                        listaDias(viewModel)
                    }

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        if (viewModel.datosI.valor != 0) {

                            OutlinedButton(
                                onClick = { viewModel.anterior() },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    "← Anterior",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }

                        if (!viewModel.estado) {

                            Button(
                                onClick = { viewModel.Siguiente() },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    "Siguiente →",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                        } else {

                            Button(
                                onClick = {
                                    ControladorAlmacenaje.regisUsuario(viewModel.datosH)

                                    ControladorAlmacenaje.inicializar(
                                        context,
                                        Horas(
                                            horaInicio = viewModel.datosH.horaAcostar,
                                            horaFin = viewModel.datosH.horaLevantar
                                        )
                                    )

                                    programarVerificador(context)
                                    navController.navigate("Principal")
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    "Finalizar →",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun SelectorFecha(viewModel: ViewModelCuestionarioInicial) {

    var fecha by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    OutlinedTextField(
        value = fecha,
        onValueChange = {},
        readOnly = true,
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(
                "Fecha de nacimiento",
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        },
        placeholder = {
            Text(
                "Selecciona una fecha",
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    val calendario = Calendar.getInstance()

                    DatePickerDialog(
                        context,
                        { _, año, mes, dia ->
                            fecha = "%02d/%02d/%04d".format(
                                dia,
                                mes + 1,
                                año
                            )

                            viewModel.CambiarFecha(fecha)
                        },
                        calendario.get(Calendar.YEAR),
                        calendario.get(Calendar.MONTH),
                        calendario.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Seleccionar fecha",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        },
        shape = RoundedCornerShape(12.dp)
    )
}


@Composable
fun SelectorHora(
    etiqueta: String,
    viewModel: ViewModelCuestionarioInicial
) {

    var hora by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    OutlinedTextField(
        value = hora,
        onValueChange = {},
        readOnly = true,
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(
                etiqueta,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        },
        placeholder = {
            Text(
                "Selecciona una hora",
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    val calendario = Calendar.getInstance()

                    TimePickerDialog(
                        context,
                        { _, horaSeleccionada, minutoSeleccionado ->
                            hora = "%02d:%02d".format(
                                horaSeleccionada,
                                minutoSeleccionado
                            )

                            viewModel.CambiarHora(hora)
                        },
                        calendario.get(Calendar.HOUR_OF_DAY),
                        calendario.get(Calendar.MINUTE),
                        true
                    ).show()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "Seleccionar hora",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        },
        shape = RoundedCornerShape(12.dp)
    )
}


@Composable
fun listaDias(viewModel: ViewModelCuestionarioInicial) {

    Column {

        viewModel.preguntas.dias.forEachIndexed { index, dia ->

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Checkbox(
                    checked = index in viewModel.nuevaLista,
                    onCheckedChange = { seleccionado ->

                        if (seleccionado) {
                            viewModel.agregarLista(index)

                        } else {
                            viewModel.quitarLista(index)
                        }

                        viewModel.datosH = viewModel.datosH.copy(
                            diasLibres = viewModel.nuevaLista.toMutableList()
                        )
                    }
                )

                Text(
                    text = dia,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


fun programarVerificador(context: Context) {

    val trabajo = PeriodicWorkRequestBuilder<VerificadorHora>(
        15,
        TimeUnit.MINUTES
    ).build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "VerificadorHora",
        ExistingPeriodicWorkPolicy.KEEP,
        trabajo
    )
}