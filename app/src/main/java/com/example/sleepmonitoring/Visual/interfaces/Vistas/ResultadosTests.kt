package com.example.sleepmonitoring.Visual.interfaces.Vistas

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.sleepmonitoring.Visual.interfaces.ViesModelyDatas.ViewModelResultadosTest
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries


@Composable
fun ResultadosTests(
    navController: NavController,
    viewModel: ViewModelResultadosTest = viewModel(),
    tipo: Int,
    selectedItem: Int,
    onTabSelected: (Int) -> Unit
) {

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(Unit) {
        viewModel.Deteccion(tipo)
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController,
                selectedIndex = selectedItem,
                onTabSelected = onTabSelected
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(16.dp)
            ) { data ->

                Snackbar(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(16.dp)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Filled.Warning,
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        Text(
                            text = data.visuals.message,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.background
                )
                .padding(innerPadding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {

                // ---------- Encabezado ----------
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(
                            bottom = 24.dp,
                            start = 17.dp,
                            end = 32.dp
                        )
                        .fillMaxWidth()
                ) {

                    Simbolo()
                }

                Column(
                    modifier = Modifier
                        .padding(
                            start = 21.dp,
                            end = 21.dp,
                            bottom = 22.dp
                        )
                        .fillMaxWidth()
                ) {

                    Text(
                        text = "RESULTADOS DE TAMIZAJE",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = viewModel.datos.nombreTest,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = viewModel.datos.DescripcionTest,
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(
                            bottom = 16.dp,
                            start = 21.dp,
                            end = 21.dp
                        )
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .shadow(
                            elevation = 4.dp,
                            spotColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        .padding(vertical = 8.dp)
                ) {

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(
                                top = 10.dp,
                                bottom = 20.dp,
                                start = 15.dp,
                                end = 8.dp
                            )
                            .fillMaxWidth()
                    ) {

                        Text(
                            text = "CALIFICACION GLOBAL",
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "RIESGO",
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(end = 50.dp)
                        )


                    }

                    Row(
                        modifier = Modifier
                            .padding(
                                bottom = 16.dp,
                                start = 29.dp,
                                end = 10.dp
                            )
                            .fillMaxWidth()
                    ) {

                        Text(
                            text = viewModel.datos.puntaje,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(4.dp)

                        )
                        Text(
                            text = viewModel.sobre,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .padding(top = 15.dp)
                        )

                        Spacer(
                            modifier = Modifier.weight(1f)
                        )

                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .width(100.dp).height(30.dp)
                                .padding(end = 10.dp)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.outline,
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .clip(RoundedCornerShape(5.dp))
                                .background(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(5.dp)
                                )

                        ) {

                            Text(
                                text = viewModel.datos.riesgo,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        if (viewModel.datos.valoresClasi.isEmpty()) {

                            barra(
                                grave = 0.300F,
                                moderado = 0.300F,
                                bueno = 0.400F,
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )

                        } else {

                            barra(
                                grave = viewModel.datos.valoresClasi[2],
                                moderado = viewModel.datos.valoresClasi[1],
                                bueno = viewModel.datos.valoresClasi[0],
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )
                        }

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(horizontal = 37.dp)
                                .fillMaxWidth()
                        ) {

                            if (viewModel.datos.clasifiaciones.isEmpty()) {

                                Text(
                                    text = "0-5 Bien",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.labelSmall
                                )

                                Text(
                                    text = "6-10 Regular",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.labelSmall
                                )

                                Text(
                                    text = "11-21 Malo",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.labelSmall
                                )

                            } else {

                                Text(
                                    text = viewModel.datos.clasifiaciones[0],
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.labelSmall
                                )

                                Text(
                                    text = viewModel.datos.clasifiaciones[1],
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.labelSmall
                                )

                                Text(
                                    text = viewModel.datos.clasifiaciones[2],
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                    }
                }

                if (tipo != 3) {

                    Column(
                        modifier = Modifier
                            .padding(
                                horizontal = 21.dp,
                                vertical = 6.dp
                            )
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(
                                top = 14.dp,
                                start = 12.dp,
                                end = 12.dp,
                                bottom = 8.dp
                            )
                    ) {

                        Text(
                            text = "EVOLUCION SEMANAL",
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(
                                start = 6.dp,
                                bottom = 8.dp
                            )
                        )

                        Grafica2(viewModel)
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(
                            horizontal = 21.dp,
                            vertical = 10.dp
                        )
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(
                            top = 14.dp,
                            start = 12.dp,
                            end = 12.dp,
                            bottom = 10.dp
                        )
                ) {

                    Text(
                        text = "DESGLOSE ANALITICO",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(
                            start = 3.dp,
                            bottom = 6.dp
                        )
                    )

                    val cantidad = minOf(
                        viewModel.variables.descripciones.size,
                        viewModel.variables.resultados.size,
                        viewModel.variables.puntajes.size
                    )

                    for (i in 0 until cantidad) {

                        DimensionWidget(
                            titulo = viewModel.variables.descripciones[i],
                            descripcion = viewModel.variables.resultados[i],
                            valor = viewModel.variables.puntajes[i]
                        )

                        if (i < cantidad - 1) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 3.dp),
                                thickness = 0.5.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )
            }
        }
    }
}


@Composable
fun DimensionWidget(
    titulo: String,
    descripcion: String,
    valor: String,
    estado: String = "",
    modifier: Modifier = Modifier,
    colorBarra: Color = MaterialTheme.colorScheme.primary
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(
                horizontal = 3.dp,
                vertical = 10.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Barra de acento lateral
        Box(
            modifier = Modifier
                .width(3.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(50))
                .background(
                    if (colorBarra == MaterialTheme.colorScheme.primary)
                        MaterialTheme.colorScheme.tertiary
                    else
                        colorBarra
                )
        )

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Top
        ) {

            Text(
                text = titulo,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = descripcion,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (valor.isNotEmpty()) {

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Text(
                text = valor,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        if (estado.isNotEmpty()) {

            Spacer(
                modifier = Modifier.width(6.dp)
            )

            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(
                        horizontal = 6.dp,
                        vertical = 2.dp
                    )
            ) {

                Text(
                    text = estado,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}


@Composable
fun barra(
    grave: Float,
    moderado: Float,
    bueno: Float,
    modifier: Modifier = Modifier
) {

    val total = grave + moderado + bueno

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(4.dp)
            .clip(RoundedCornerShape(50))
    ) {


        if (bueno > 0f) {

            Box(
                modifier = Modifier
                    .weight(bueno / total)
                    .fillMaxHeight()
                    .background(
                        MaterialTheme.colorScheme.error
                    )
            )
        }
        if (moderado > 0f) {

            Box(
                modifier = Modifier
                    .weight(moderado / total)
                    .fillMaxHeight()
                    .background(
                        MaterialTheme.colorScheme.secondary
                    )
            )
        }


        if (grave > 0f) {

            Box(
                modifier = Modifier
                    .weight(grave / total)
                    .fillMaxHeight()
                    .background(
                        MaterialTheme.colorScheme.tertiary
                    )
            )
        }
    }
}


@Composable
fun Grafica2(viewModel: ViewModelResultadosTest) {

    val modelProducer = remember {
        CartesianChartModelProducer()
    }

    val mapa = viewModel.datos.mutableList

    val dias = listOf(
        "Lun",
        "Mar",
        "Mier",
        "Juev",
        "Vie",
        "Sab",
        "Dom"
    )

    // Los días sin datos quedan en 0. Aquí son puntajes, no horas.
    val valores = dias.map {
        (mapa?.get(it) ?: 0L).toDouble()
    }

    LaunchedEffect(valores) {
        modelProducer.runTransaction {
            lineSeries {
                series(valores)
            }
        }
    }

    val label = rememberAxisLabelComponent(
        color = MaterialTheme.colorScheme.onPrimary,
        textSize = MaterialTheme.typography.bodySmall.fontSize
    )

    val ejex = HorizontalAxis.rememberBottom(
        label = label,
        valueFormatter = CartesianValueFormatter { _, value, _ ->
            dias.getOrNull(value.toInt()) ?: ""
        }
    )

    val ejey = VerticalAxis.rememberStart(
        label = label,
        valueFormatter = CartesianValueFormatter { _, value, _ ->
            "${value.toInt()}"
        }
    )

    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(),
            startAxis = ejey,
            bottomAxis = ejex
        ),
        modelProducer = modelProducer,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}