    package com.example.sleepmonitoring.Visual.interfaces.Vistas
    import androidx.compose.runtime.saveable.rememberSaveable
    import androidx.compose.foundation.Image
    import androidx.compose.foundation.background
    import androidx.compose.foundation.border
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.*
    import com.example.sleepmonitoring.Controladores.ControladorHora
    import androidx.compose.foundation.rememberScrollState
    import androidx.compose.foundation.shape.CircleShape
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.foundation.verticalScroll
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.Alarm
    import androidx.compose.material.icons.filled.ArrowRight
    import androidx.compose.material.icons.filled.Assignment
    import androidx.compose.material.icons.filled.BarChart
    import androidx.compose.material.icons.filled.Bed
    import androidx.compose.material.icons.filled.Check
    import androidx.compose.material.icons.filled.DarkMode
    import androidx.compose.material.icons.filled.LocalFireDepartment
    import androidx.compose.material.icons.filled.MonitorHeart
    import androidx.compose.material.icons.filled.PlayArrow
    import androidx.compose.material.icons.filled.Settings
    import androidx.compose.material.icons.filled.StarBorder
    import androidx.compose.material.icons.filled.Warning
    import androidx.compose.material3.Button
    import androidx.compose.material3.ButtonDefaults
    import androidx.compose.material3.Icon
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.NavigationBarItem
    import androidx.compose.material3.NavigationBarItemDefaults
    import androidx.compose.material3.Scaffold
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.LaunchedEffect
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableIntStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.draw.clip
    import androidx.compose.ui.draw.shadow
    import androidx.compose.ui.graphics.Brush
    import androidx.compose.ui.graphics.Shape
    import androidx.compose.ui.graphics.compositeOver
    import androidx.compose.ui.graphics.vector.ImageVector
    import androidx.compose.ui.layout.ContentScale
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.text.font.FontStyle
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.text.style.TextAlign
    import androidx.compose.ui.unit.Dp
    import androidx.compose.ui.unit.dp
    import androidx.lifecycle.viewmodel.compose.viewModel
    import androidx.navigation.NavController
    import com.example.sleepmonitoring.BasesDatosFirebase.ConsulFirebase
    import com.example.sleepmonitoring.Controladores.ControladorDatosCrudos
    import com.example.sleepmonitoring.R
    import com.example.sleepmonitoring.Visual.interfaces.ViesModelyDatas.ViewModelPrincipal
    import android.content.Context
    import androidx.compose.foundation.layout.Arrangement
    import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
    import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
    import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
    import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
    import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
    import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
    import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
    import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
    import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
    import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
    import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
    import androidx.compose.ui.platform.LocalContext
    import androidx.navigation.NavGraph.Companion.findStartDestination
    import com.example.sleepmonitoring.Analisis.DatosCrudos.SensorService
    import com.example.sleepmonitoring.BasesDatosFirebase.DatosConstantes
    import com.example.sleepmonitoring.Controladores.ControladorAlmacenaje
    import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
    import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
    import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
    import kotlinx.coroutines.CoroutineScope
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.launch
    
    
    @Composable
    fun PrincipalInterfaz(
        navController: NavController,
        viewModel: ViewModelPrincipal = viewModel(),
        selectedItem: Int,
        onTabSelected: (Int) -> Unit
    ) {
        val context = LocalContext.current
        val scope = CoroutineScope(Dispatchers.IO)
        Scaffold(
            bottomBar = {
                BottomNavigationBar(
                    navController,
                    selectedIndex = selectedItem,
                    onTabSelected = onTabSelected
                )
            }
        ) { innerPadding ->

    
            Box(
            ) {
                Image(
                    painter = painterResource(id = R.drawable.fondo),
                    contentDescription = "Imagen de fondo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().fillMaxHeight()
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 21.dp)
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    DreamClueHeader(navController)
                    Spacer(modifier = Modifier.height(14.dp))
                    NightArchetypeCard(viewModel)
                    Spacer(modifier = Modifier.height(12.dp))
                    SleepMonitoringCard(context, viewModel)
                    Spacer(modifier = Modifier.height(12.dp))
                    WeeklyStatisticsCard(viewModel, navController)
                    Spacer(modifier = Modifier.height(12.dp))
                    SleepMetricsRow(viewModel)
                    Spacer(modifier = Modifier.height(12.dp))
                    if (DatosConstantes.sinDatos == true) {
                        TestResult(viewModel, navController)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    MensajeSueño()
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
    
    @Composable
    fun Modifier.tarjetaDegradada(
        forma: Shape = RoundedCornerShape(22.dp),
        elevacion: Dp = 8.dp
    ): Modifier {
        val cs = MaterialTheme.colorScheme
        return this
            .shadow(elevation = elevacion, shape = forma)
            .clip(forma)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        cs.primary,
                        cs.secondaryContainer.copy(alpha = 0.45f).compositeOver(cs.primary)
                    )
                ),
                shape = forma
            )
            .border(1.dp, cs.secondary.copy(alpha = 0.35f), forma)
    }
    
    @Composable
    fun Modifier.cajaInterna(forma: Shape = RoundedCornerShape(16.dp)): Modifier {
        val cs = MaterialTheme.colorScheme
        return this
            .clip(forma)
            .background(cs.secondaryContainer)
            .border(1.dp, cs.secondary.copy(alpha = 0.5f), forma)
    }
    
    @Composable
            fun IconoCircular(icono: ImageVector, descripcion: String, tamano: Dp = 40.dp) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(tamano)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
        ) {
            Icon(
                imageVector = icono,
                contentDescription = descripcion,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(tamano * 0.6f)
            )
        }
    }
    
    @Composable
    fun BotonAcento(
        texto: String,
        icono: ImageVector? = null,
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        val cs = MaterialTheme.colorScheme
        val forma = RoundedCornerShape(16.dp)
        Button(
            onClick = onClick,
            shape = forma,
            colors = ButtonDefaults.buttonColors(
                containerColor = cs.secondaryContainer,
                contentColor = cs.onPrimaryContainer
            ),
            modifier = modifier
                .fillMaxWidth()
                .height(52.dp)
                .border(1.5.dp, cs.secondary, forma)
        ) {
            if (icono != null) {
                Icon(icono, null, tint = cs.secondary, modifier = Modifier.size(22.dp))
                Spacer(Modifier.width(8.dp))
            }
            Text(texto, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        }
    }
    
    @Composable
    fun DreamClueHeader(navController: NavController) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Simbolo()
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
    
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Ajustes",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { navController.navigate("Configuracion") }
                )
            }
        }
    }
    
    @Composable
    fun NightArchetypeCard(viewModel: ViewModelPrincipal) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .tarjetaDegradada()
                .padding(top = 14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 14.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
            ) {
    
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .cajaInterna(RoundedCornerShape(50))
                        .padding(vertical = 8.dp, horizontal = 6.dp)
                ) {
                    Text(
                        text = "Duracion de Ultima Noche: ${ControladorHora.Long_Texto(viewModel.datos.duracion ?: 0L)}",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Text(
                text = "Representación de tu Sueño",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(bottom = 14.dp)
                    .fillMaxWidth()
            ) {
                AvatarSueno(minutosDormidos = viewModel.datos.duracion ?: 0L)
            }
    
            Text(
                text = viewModel.datos.riesgo ?: "Buena calidad Sueño",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 20.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 20.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .cajaInterna()
                    .clickable { println("Pressed!") }
                    .padding(vertical = 8.dp, horizontal = 12.dp)
            ) {
                IconoCircular(Icons.Filled.MonitorHeart, "Latido", 36.dp)
                Spacer(Modifier.width(10.dp))
                Text(
                    text = viewModel.datos.fraseRiesgo ?: "No hay Datos",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
    
    @Composable
    fun SleepMonitoringCard(context: Context, viewModel: ViewModelPrincipal) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .tarjetaDegradada()
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = "Iniciar tu Hora de Dormir",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Text(
                text = "Optimiza tu noche con Dream Clue",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 14.dp)
            )
            BotonAcento(
                texto = if (viewModel.botonReloj == false) "Iniciar Sueño" else "Finalizar Sueño",
                icono = Icons.Filled.Alarm,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                if (!viewModel.botonReloj) {
                    viewModel.iniciarSensor(context)
                } else {
                    viewModel.finalizarSensor(context)
                }
            }
        }
    }
    
    @Composable
    fun WeeklyStatisticsCard(viewModel: ViewModelPrincipal, navController: NavController) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .tarjetaDegradada()
                .padding(vertical = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
            ) {
                IconoCircular(Icons.Filled.BarChart, "Estadisticas", 44.dp)
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "ESTADISTICAS",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.weight(1f))
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "${ControladorHora.Double_Texto(viewModel.datos.promDura)}",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                    Text(
                        text = "Promedio Nocturno",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
    
            Box(
                modifier = Modifier
                    .padding(horizontal = 14.dp)
                    .fillMaxWidth()
                    .cajaInterna(RoundedCornerShape(18.dp))
            ) {
                Grafica(viewModel)
            }
    
            Spacer(modifier = Modifier.height(12.dp))
    
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .cajaInterna(RoundedCornerShape(25.dp))
                    .clickable { navController.navigate("MenuTests") }
                    .padding(start = 24.dp, end = 12.dp)
            ) {
                Text(
                    text = "Ver resultados",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(end = 6.dp)
                )
                Icon(
                    imageVector = Icons.Filled.ArrowRight,
                    contentDescription = "Flecha",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
    
    @Composable
    fun SleepMetricsRow(viewModel: ViewModelPrincipal) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(bottom = 5.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Column(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .weight(1f)
                    .fillMaxHeight()
                    .tarjetaDegradada()
                    .padding(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.22f))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "RACHA",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 10.dp)
                ) {
                    Text(
                        text = "${viewModel.datos.racha}",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(
                        text = "DÍAS",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 6.dp, end = 8.dp)
                    )
                    Icon(
                        imageVector = Icons.Filled.LocalFireDepartment,
                        contentDescription = "Llama simbolo de racha",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(40.dp)
                    )
                }
                Text(
                    text = "Cuantos dias has dormido bien",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
    
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .tarjetaDegradada()
                    .padding(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.22f))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Eficiencia",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 10.dp)
                ) {
                    Text(
                        text = "${viewModel.datos.eficiencia}/100",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Filled.Bed,
                        contentDescription = "Eficen",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(36.dp).padding(start = 10.dp)
                    )
                }
                Text(
                    text = "Porcentaje de que tanto tiempo descansate",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
    
    @Composable
    fun TestResult(viewModel: ViewModelPrincipal, navController: NavController) {
        val terminado = viewModel.estadoTest
    
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .tarjetaDegradada()
        ) {
            Box(
                modifier = Modifier
                    .width(7.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.secondary)
            )
            Column(modifier = Modifier.weight(1f).padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconoCircular(
                        if (terminado) Icons.Filled.Check else Icons.Filled.Warning,
                        if (terminado) "Test Terminado" else "Advertencia",
                        36.dp
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = if (terminado) "VE TUS RESULTADOS DEL SUEÑO" else "CUESTIONARIO PENDIENTE",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = if (terminado)
                        "Observa de forma detallada los resultados de cada test"
                    else
                        "Responde a estas breves preguntas y descubre la calidad de tu descanso nocturno",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp, bottom = 14.dp)
                )
                BotonAcento(
                    texto = if (terminado) "Ve tus resultados" else "Inciar Cuestionario",
                    icono = Icons.Filled.PlayArrow
                ) {
                    navController.navigate(if (terminado) "Cuestionario/1" else "Cuestionario/0")
                }
            }
        }
    }
    
    @Composable
    fun MensajeSueño() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .tarjetaDegradada()
        ) {
            Box(
                modifier = Modifier
                    .width(7.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.secondary)
            )
            Row(modifier = Modifier.padding(14.dp)) {
                IconoCircular(Icons.Filled.StarBorder, "Estrella", 36.dp)
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxWidth().padding(start = 12.dp)
                ) {
                    Text(
                        text = "INSPIRACION DEL SUEÑO",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "«El sueño es la cadena de oro que une la salud y nuestros cuerpos.»",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "- Thomas Dekker (1572-1632), Dramaturgo & Ensayista",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
    
    @Composable
    fun BottomNavigationBar(navController: NavController, selectedIndex: Int, onTabSelected: (Int) -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primary)
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.35f))
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 2.dp, horizontal = 20.dp)
                    .fillMaxWidth()
            ) {
                val icons = listOf(
                    Icons.Filled.DarkMode,
                    Icons.Filled.BarChart,
                    Icons.Filled.Settings
                )
                val labels = listOf("Inicio", "Resultados", "Ajustes")
                icons.forEachIndexed { index, icon ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = icon,
                                contentDescription = "Icono Menu",
                                modifier = Modifier.size(26.dp)
                            )
                        }, label = {
                            Text(
                                text = labels[index],
                                style = MaterialTheme.typography.labelSmall
                            )
                        }, selected = selectedIndex == index,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.secondary,
                            selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            unselectedTextColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        onClick = {
                            onTabSelected(index)
                            navController.navigate(ObtenerIndex(index)) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
    
                }
            }
    
        }
    }
    
    fun ObtenerIndex(index: Int): String {
        val labels = listOf("Principal", "MenuTests", "Configuracion")
        return labels[index]
    }
    
    @Composable
    fun Simbolo() {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(width = 40.dp, height = 40.dp)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(10.dp))
                    .background(color = MaterialTheme.colorScheme.background, shape = RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
    
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icono_app),
                    contentDescription = "Descripción de la imagen para accesibilidad"
                )
            }
    
            Text(
                text = "DREAM CLUE",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
    
    @Composable
    fun Grafica(viewModel: ViewModelPrincipal) {
    
        val modelProducer = remember {
            CartesianChartModelProducer()
        }
    
        val mapa = viewModel.datos.mutableList
    
        val dias = listOf(
            "Lun", "Mar", "Mier", "Juev", "Vie", "Sab", "Dom"
        )
    
        val valores = dias.map {
            (mapa?.get(it) ?: 0L) / 60.0
        }
    
        LaunchedEffect(valores) {
            modelProducer.runTransaction {
                columnSeries {
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
                "${value.toInt()}h"
            }
        )
    
        CartesianChartHost(
            chart = rememberCartesianChart(
                rememberColumnCartesianLayer(),
                startAxis = ejey,
                bottomAxis = ejex
            ),
            modelProducer = modelProducer,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(10.dp)
        )
    }