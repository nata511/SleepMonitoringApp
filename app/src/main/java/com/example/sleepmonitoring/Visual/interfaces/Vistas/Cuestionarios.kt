    package com.example.sleepmonitoring.Visual.interfaces.Vistas
    
    import androidx.compose.runtime.*
    import androidx.compose.foundation.*
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.shape.*
    import androidx.compose.foundation.text.*
    import androidx.compose.material3.*
    import androidx.compose.ui.*
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.draw.*
    import androidx.compose.ui.graphics.*
    import androidx.compose.ui.tooling.preview.Preview
    import androidx.compose.ui.unit.*
    import androidx.compose.ui.layout.*
    import androidx.lifecycle.viewmodel.compose.viewModel
    import androidx.navigation.NavController
    import com.example.sleepmonitoring.Visual.interfaces.ViesModelyDatas.Preguntas
    import com.example.sleepmonitoring.Visual.interfaces.ViesModelyDatas.ViewModelCuestionarios
    import com.skydoves.landscapist.*
    import com.skydoves.landscapist.coil3.*
    
    
    @Composable
    fun Cuestionarios(
        tipo: Int,
        viewModel: ViewModelCuestionarios = viewModel(),
        navController: NavController,
        selectedItem: Int,
        onTabSelected: (Int) -> Unit
    ) {
    
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
            }
    
        ) { innerPadding ->
    
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxHeight()
                    .background(
                        color = MaterialTheme.colorScheme.background
                    )
            ) {
    
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(
                            color = MaterialTheme.colorScheme.background
                        )
                        .padding(vertical = 14.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    var texto=""
                    if(tipo==0){
                        texto ="Escuesta Diaria Del Sueño"
                    }else{
                        texto= "Cuestionario de Epsworth"
                    }
                    Text(
                        text = texto,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .padding(
                                bottom = 12.dp,
                                start = 28.dp,
                                top = 35.dp
                            )
                    )
    
                    Text(
                        text = viewModel.datos.pregunta,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(vertical = 10.dp, horizontal = 25.dp)
                    )
    
                    LinearProgressIndicator(
                        progress = { viewModel.progresp },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(50)).padding(horizontal = 25.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.primaryContainer
                    )
    
                    Spacer(
                        Modifier.height(15.dp)
                    )
    
                    PlantillaCuestioario(viewModel)
    
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(
                                bottom = 7.dp,
                                start = 26.dp,
                                end = 26.dp
                            )
                            .fillMaxWidth()
                    ) {
    
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
                        Spacer(Modifier.width(15.dp))
                        OutlinedButton(
                            onClick = { viewModel.Iniciar(navController) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            if (!viewModel.esUltimaPregunta) {
    
                                Text(
                                    text = "Siguiente ->",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.labelMedium
                                )
    
                            } else {
    
                                Text(
                                    text = "Terminar Cuestionario",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    
    
    @Composable
    fun PlantillaCuestioario(
        viewModel: ViewModelCuestionarios
    ) {
    
        Column(
            modifier = Modifier
                .padding(
                    bottom = 7.dp,
                    start = 26.dp,
                    end = 26.dp
                )
                .clip(
                    shape = RoundedCornerShape(10.dp)
                )
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(
                    top = 18.dp,
                    bottom = 18.dp,
                    start = 19.dp,
                    end = 30.dp
                )
        ) {
    
            Column(
                modifier = Modifier
                    .padding(bottom = 7.dp)
                    .clip(
                        shape = RoundedCornerShape(10.dp)
                    )
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(top = 4.dp)
            ) {
    
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(
                            bottom = 7.dp,
                            start = 15.dp,
                            end = 15.dp
                        )
                        .fillMaxWidth()
                ) {
    
                    OpcionSeleccionable(
                        texto = viewModel.datos.opcion1,
                        seleccionada = viewModel.opcion == 0,
                        onSeleccionar = {
                            viewModel.opcion = 0
                        }
                    )
                }
            }
    
            Column(
                modifier = Modifier
                    .padding(bottom = 7.dp)
                    .clip(
                        shape = RoundedCornerShape(10.dp)
                    )
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(
                        top = 4.dp,
                        end = 15.dp
                    )
            ) {
    
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(
                            bottom = 12.dp,
                            start = 15.dp
                        )
                        .fillMaxWidth()
                ) {
    
                    OpcionSeleccionable(
                        texto = viewModel.datos.opcion2,
                        seleccionada = viewModel.opcion == 1,
                        onSeleccionar = {
                            viewModel.opcion = 1
                        }
                    )
                }
            }
    
            Column(
                modifier = Modifier
                    .padding(bottom = 7.dp)
                    .clip(
                        shape = RoundedCornerShape(10.dp)
                    )
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(top = 4.dp)
            ) {
    
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(
                            bottom = 12.dp,
                            start = 15.dp,
                            end = 15.dp
                        )
                        .fillMaxWidth()
                ) {
    
                    OpcionSeleccionable(
                        texto = viewModel.datos.opcion3,
                        seleccionada = viewModel.opcion == 2,
                        onSeleccionar = {
                            viewModel.opcion = 2
                        }
                    )
                }
            }
    
            Column(
                modifier = Modifier
                    .clip(
                        shape = RoundedCornerShape(10.dp)
                    )
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(top = 4.dp)
            ) {
    
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(
                            bottom = 12.dp,
                            start = 15.dp,
                            end = 15.dp
                        )
                        .fillMaxWidth()
                ) {
    
                    OpcionSeleccionable(
                        texto = viewModel.datos.opcion4,
                        seleccionada = viewModel.opcion == 3,
                        onSeleccionar = {
                            viewModel.opcion = 3
                        }
                    )
                }
            }
        }
    }
    
    
    @Composable
    fun OpcionSeleccionable(
        texto: String,
        seleccionada: Boolean,
        onSeleccionar: () -> Unit
    ) {
    
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onSeleccionar()
                }
                .padding(10.dp)
        ) {
    
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .background(
                        color = if (seleccionada)
                            MaterialTheme.colorScheme.secondary
                        else
                            Color.Transparent,
                        shape = RoundedCornerShape(5.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
    
                if (seleccionada) {
    
                    Text(
                        text = "✓",
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
    
            Spacer(
                modifier = Modifier.width(12.dp)
            )
    
            Text(
                text = texto,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
