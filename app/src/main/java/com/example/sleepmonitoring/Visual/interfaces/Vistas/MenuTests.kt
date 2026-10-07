package com.example.sleepmonitoring.Visual.interfaces.Vistas

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sleepmonitoring.BasesDatosFirebase.ConsulFirebase
import com.example.sleepmonitoring.BasesDatosFirebase.DatosConstantes
import com.example.sleepmonitoring.Visual.interfaces.ViesModelyDatas.ViewModelMenuTest

@Composable
fun MenuTests(
    viewModel: ViewModelMenuTest = viewModel(),
    navController: NavController,
    selectedItem: Int,
    onTabSelected: (Int) -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        ConsulFirebase.obtenerResumenUsuarios()
        ConsulFirebase.puenteResumen.collect { dato ->
            viewModel.CambiarDatos(dato)
            println("El test Psqi esta completado " + viewModel.datos.psqi_completado)
        }
    }

    LaunchedEffect(viewModel.error) {
        if (viewModel.error) {
            snackbarHostState.showSnackbar(
                message = "Datos insuficientes para MCTQ (se necesitan 7 días)",
                duration = SnackbarDuration.Short
            )
            viewModel.error = false
        }
    }

    LaunchedEffect(viewModel.faltaDatos) {
        if (viewModel.faltaDatos) {
            snackbarHostState.showSnackbar(
                message = """No hay datos del sueño para realizar el analisis
                          Asegurese de ubicar el celular cerca suyo y en la cama para dormir""",
                duration = SnackbarDuration.Short
            )
            viewModel.faltaDatos = false
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    shape = RoundedCornerShape(14.dp),
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            }
        },
        bottomBar = {
            BottomNavigationBar(
                navController,
                selectedIndex = selectedItem,
                onTabSelected = onTabSelected
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxHeight()
                .background(color = MaterialTheme.colorScheme.background)
        ) {

            Spacer(Modifier.height(15.dp))
            Row(Modifier.padding(start = 15.dp)) {
                Simbolo()
            }


            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(color = MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 10.dp, horizontal = 5.dp)
            ) {

                // ---------- Sección: datos de la última noche ----------
                TituloSeccion("Datos de tu Ultima Noche")

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 22.dp)
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                ) {

                    var riesgo = ""

                    if (
                        viewModel.datos.riesgoSueño == null ||
                        viewModel.datos.riesgoSueño == ""
                    ) {
                        riesgo = "Tiempo Dormido"
                    } else {
                        riesgo = viewModel.datos.riesgoSueño ?: ""
                    }

                    var lola = ""

                    if (
                        viewModel.datos.duracion == null ||
                        viewModel.datos.duracion == ""
                    ) {
                        lola = "0h:0m"
                    } else {
                        lola = viewModel.datos.duracion ?: ""
                    }

                    TarjetaReutilizable(
                        "Duracion",
                        lola,
                        riesgo,
                        Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    TarjetaReutilizable(
                        "Eficiencia",
                        "${viewModel.datos.eficiencia} /100",
                        "Tiempo dormido efectivo",
                        Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(start = 22.dp, end = 22.dp, bottom = 6.dp)
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                ) {

                    TarjetaReutilizable(
                        "Despertares",
                        "${viewModel.datos.despertares_ ?: 0}",
                        "Cantidad de veces",
                        Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    TarjetaReutilizable(
                        "Latencia",
                        viewModel.datos.latencia ?: "0",
                        "Tiempo en dormirse",
                        Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(26.dp))

                TituloSeccion("Resultados de test ")

                Text(
                    "Esto es lo que revelan tus test de sueño sobre tu descanso y tu reloj biológico",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 22.dp, end = 22.dp, top = 8.dp, bottom = 16.dp)
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .padding(start = 22.dp, end = 22.dp, bottom = 20.dp)
                        .fillMaxWidth()
                ) {

                    if (viewModel.datos.psqi_completado == false) {
                        WidgetTest(
                            "Medicion habitos",
                            "Calidad del Sueño de Pittsburgh",
                            "Sin datos/24",
                            riesgo = "Sin datos",
                            navController,
                            "-1",
                            viewModel
                        )
                    } else {
                        WidgetTest(
                            "Medicion habitos",
                            "Calidad del Sueño de Pittsburgh",
                            "${viewModel.datos.psqi_puntaje}/21",
                            riesgo = viewModel.datos.psqi_riesgo ?: "",
                            navController,
                            "0",
                            viewModel
                        )
                    }

                    if (viewModel.datos.atenas_completado == false) {
                        WidgetTest(
                            "Calcular el Insomnio",
                            "Test de Atenea",
                            "Sin datos/15",
                            "Sin datos",
                            navController,
                            "-1",
                            viewModel
                        )
                    } else {
                        WidgetTest(
                            "Calcular el Insomnio",
                            "Test de Atenea",
                            "${viewModel.datos.atenas_puntaje}/24",
                            viewModel.datos.atenas_riesgo ?: "",
                            navController,
                            "1",
                            viewModel
                        )
                    }

                    if (viewModel.datos.epworth_completado == false) {
                        WidgetTest(
                            "Escala de Somnolencia Diruna",
                            "EscalaEpsworth",
                            "Sin datos",
                            riesgo = "Sin Datos ",
                            navController,
                            "-2",
                            viewModel
                        )
                    } else {
                        WidgetTest(
                            "Escala de Somnolencia Diruna",
                            "Escala de Epsworth",
                            "${viewModel.datos.epworth_puntaje}/24",
                            riesgo = viewModel.datos.epworth_riesgo ?: "",
                            navController,
                            "2",
                            viewModel
                        )
                    }

                    if (viewModel.datos.mctq_completado == true) {
                        WidgetTest(
                            "Calcular el cronotipo",
                            "Test de Munich (MCQT)",
                            "${viewModel.datos.mctq_puntaje}",
                            viewModel.datos.riesgoSueño ?: "",
                            navController,
                            "3",
                            viewModel
                        )
                    } else {
                        WidgetTest(
                            "Calcular el cronotipo",
                            "Test de Munich (MCQT)",
                            "Sin datos/15",
                            "Sin datos",
                            navController,
                            "-3",
                            viewModel
                        )
                    }
                }
            }
        }
    }
}

/** Título de sección con barra de acento (solo diseño). */
@Composable
fun TituloSeccion(texto: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp)
    ) {
        Box(
            modifier = Modifier
                .width(5.dp)
                .height(26.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondary)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            texto,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun TarjetaReutilizable(
    texto: String,
    valor: String,
    riesgo: String,
    modifier: Modifier
) {
    val forma = RoundedCornerShape(22.dp)

    Column(
        modifier = modifier
            .fillMaxHeight()
            .heightIn(min = 148.dp)
            .shadow(elevation = 8.dp, shape = forma)
            .clip(forma)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.45f)
                            .compositeOver(MaterialTheme.colorScheme.primary)
                    )
                ),
                shape = forma
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.35f),
                shape = forma
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.22f))
                .padding(horizontal = 12.dp, vertical = 5.dp)
        ) {
            Text(
                text = texto,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            valor,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.bodyLarge,

        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            riesgo,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun WidgetTest(
    NombreText: String,
    nombrePrincipal: String,
    puntaje: String,
    riesgo: String,
    navController: NavController,
    tipo: String,
    viewModel: ViewModelMenuTest
) {

    val lista = listOf("0", "1", "2", "3")
    val forma = RoundedCornerShape(24.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .shadow(elevation = 8.dp, shape = forma)
            .clip(forma)
            .background(color = MaterialTheme.colorScheme.primary)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                shape = forma
            )
            .clickable {

                if (tipo == "-2") {
                    navController.navigate("Cuestionario/1")
                    return@clickable
                } else if (tipo == "2") {
                    navController.navigate("Resultados/2")
                    return@clickable
                }

                if (DatosConstantes.sinDatos == true) {

                    if (tipo == "-1") {
                        navController.navigate("Cuestionario/0")

                    } else if (tipo == "-3") {
                        viewModel.error = true

                    } else if (lista.contains(tipo)) {
                        navController.navigate("Resultados/$tipo")
                    }

                } else {

                    if (tipo == "-3") {
                        viewModel.error = true
                    } else {
                        viewModel.faltaDatos = true
                    }
                }
                println(tipo)
                println(DatosConstantes.sinDatos)
            }
    ) {

        Box(
            modifier = Modifier
                .width(7.dp)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.secondary)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(18.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 12.dp)
                ) {

                    Text(
                        text = NombreText,
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.labelMedium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = nombrePrincipal,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .heightIn(min = 88.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(color = MaterialTheme.colorScheme.secondaryContainer)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(12.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = "Puntaje",
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.labelLarge
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = puntaje,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .heightIn(min = 88.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(color = MaterialTheme.colorScheme.secondaryContainer)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(12.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = "Riesgo",
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.labelLarge
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = riesgo,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}