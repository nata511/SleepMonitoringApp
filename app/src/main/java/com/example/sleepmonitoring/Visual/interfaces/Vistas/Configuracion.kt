package com.dreamclue.ui.configuracion

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.sleepmonitoring.BasesDatosFirebase.ConsulFirebase
import com.example.sleepmonitoring.Visual.interfaces.ViesModelyDatas.ViewModelConfiguracion
import com.example.sleepmonitoring.Visual.interfaces.Vistas.BottomNavigationBar
import com.example.sleepmonitoring.Visual.interfaces.Vistas.CatalogoAvatares
import com.example.sleepmonitoring.Visual.interfaces.Vistas.ConfiguracionAvatar
import com.example.sleepmonitoring.Visual.interfaces.Vistas.EstadoSueno
import com.example.sleepmonitoring.Visual.interfaces.Vistas.IconoCircular
import com.example.sleepmonitoring.Visual.interfaces.Vistas.ImagenAvatar
import com.example.sleepmonitoring.Visual.interfaces.Vistas.cajaInterna
import com.example.sleepmonitoring.Visual.interfaces.Vistas.tarjetaDegradada
import com.example.sleepmonitoring.ui.theme.ConfiguracionTema
import com.example.sleepmonitoring.ui.theme.glow
import com.example.sleepmonitoring.ui.theme.glow2
import com.example.sleepmonitoring.ui.theme.panel
import com.example.sleepmonitoring.ui.theme.panel2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ConfiguracionVisual(
    navController: NavController,
    viewModel: ViewModelConfiguracion = viewModel(),
    selectedItem: Int,
    onTabSelected: (Int) -> Unit
) {
    val context=LocalContext.current
    val colores = MaterialTheme.colorScheme
    val tipos = MaterialTheme.typography

    var temaSeleccionado by remember {
        mutableStateOf(ConfiguracionTema.tema)
    }

    val selectorFoto = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.foto = uri.toString()
        }
    }

    Scaffold(
        containerColor = colores.background,
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                selectedIndex = selectedItem,
                onTabSelected = onTabSelected
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .width(5.dp)
                                .height(26.dp)
                                .clip(CircleShape)
                                .background(colores.secondary)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = "Configuración",
                            color = colores.onPrimaryContainer,
                            style = tipos.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = "Personaliza tu experiencia de sueño",
                        color = colores.onPrimary.copy(alpha = 0.65f),
                        style = tipos.bodySmall,
                        modifier = Modifier.padding(start = 15.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Surface(
                    modifier = Modifier
                        .shadow(elevation = 6.dp, shape = RoundedCornerShape(14.dp))
                        .clip(RoundedCornerShape(14.dp))
                        .clickable {
                            viewModel.Guardar()
                        },
                    color = colores.secondary,
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = 14.dp,
                            vertical = 10.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(17.dp),
                            tint = colores.onSecondary
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = "Guardar",
                            color = colores.onSecondary,
                            style = tipos.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            TarjetaVisual {
                EncabezadoVisual(
                    icono = Icons.Default.Person,
                    titulo = "Perfil"
                )

                Spacer(modifier = Modifier.height(18.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var ini = "A"

                    if (viewModel.nombre != "" || selectorFoto==null) {
                        ini = viewModel.nombre[0].toString()
                    }

                    AvatarVisual(
                        inicial = ini,
                        editar = {
                            ini=""
                            selectorFoto.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        },
                        viewModel = viewModel
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = viewModel.nombre,
                        color = colores.onPrimaryContainer,
                        style = tipos.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                CampoVisual(
                    etiqueta = "Nombre",
                    valor = "Ana Torres",
                    icono = Icons.Default.Badge,
                    resaltado = true,
                    viewModel = viewModel
                )
            }

            TarjetaVisual {
                EncabezadoVisual(
                    icono = Icons.Default.Palette,
                    titulo = "Temas del Sueño"
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Elige entre paletas de colores suaves y cálidos para adaptar la interfaz a tu ambiente nocturno ideal.",
                    color = colores.onPrimary,
                    style = tipos.bodySmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TemaVisual(
                        titulo = "Vacío Cósmico",
                        tema = "1",
                        acento = glow,
                        fondo = panel,
                        seleccionado = temaSeleccionado == "1",
                        onClick = {
                            temaSeleccionado = "1"
                        },
                        modifier = Modifier.weight(1f)
                    )

                    TemaVisual(
                        titulo = "Abismo Índigo",
                        tema = "2",
                        acento = glow2,
                        fondo = panel2,
                        seleccionado = temaSeleccionado == "2",
                        onClick = {
                            temaSeleccionado = "2"
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            TarjetaVisual {
                EncabezadoVisual(icono = Icons.Default.Face, titulo = "Mi avatar")

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Elige con cuál te sientes mejor representado.",
                    color = colores.onPrimary,
                    style = tipos.bodySmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    listOf("H", "M").forEach { genero ->
                        OpcionAvatar(
                            recurso = CatalogoAvatares.obtener(
                                temaSeleccionado, genero, EstadoSueno.BIEN_DESCANSADO
                            ),
                            seleccionado = viewModel.generoAvatar == genero,
                            onClick = { viewModel.generoAvatar = genero },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .cajaInterna(RoundedCornerShape(16.dp))
                    .clickable {

                        navController.navigate("Inicio")
                        viewModel.cerrar(context)
                    },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = colores.secondary
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Cerrar Sesión en Todos los Dispositivos",
                    color = colores.onPrimaryContainer,
                    style = tipos.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun TarjetaVisual(
    contenido: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .tarjetaDegradada()
            .padding(18.dp),
        content = contenido
    )
}

@Composable
private fun EncabezadoVisual(
    icono: ImageVector,
    titulo: String
) {
    val colores = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconoCircular(icono, titulo, 40.dp)

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = titulo,
            color = colores.onPrimaryContainer,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
private fun AvatarVisual(
    inicial: String,
    editar: () -> Unit,
    viewModel: ViewModelConfiguracion
) {
    val colores = MaterialTheme.colorScheme

    Box(
        modifier = Modifier.size(108.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .shadow(elevation = 6.dp, shape = CircleShape)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = colores.secondary,
                    shape = CircleShape
                )
                .background(
                    colores.secondaryContainer
                )
                .clickable {
                    editar()
                },
            contentAlignment = Alignment.Center
        ) {
            if (viewModel.foto != null) {
                AsyncImage(
                    model = viewModel.foto,
                    contentDescription = "Foto de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Text(
                text = inicial,
                color = colores.secondary,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(32.dp)
                .clip(CircleShape)
                .background(colores.secondary)
                .border(
                    width = 2.dp,
                    color = colores.primary,
                    shape = CircleShape
                )
                .clickable {
                    editar()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = colores.onSecondary
            )
        }
    }
}
@Composable
private fun CampoVisual(
    etiqueta: String,
    valor: String,
    icono: ImageVector,
    resaltado: Boolean = false,
    viewModel: ViewModelConfiguracion
) {
    val colores = MaterialTheme.colorScheme

    val colorBorde = if (resaltado) {
        colores.secondary
    } else {
        colores.secondary.copy(alpha = 0.5f)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(
                        if (resaltado) {
                            colores.secondary.copy(alpha = 0.22f)
                        } else {
                            colores.secondaryContainer
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    modifier = Modifier.size(17.dp),
                    tint = colores.secondary
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            OutlinedTextField(
                value = viewModel.nombreCambio,
                onValueChange = {
                    viewModel.CambiarNombre(it)
                },
                label = {
                    Text(
                        text = etiqueta,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                isError = viewModel.error != null,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "People"
                    )
                },
                modifier = Modifier.weight(1f),
                enabled = !viewModel.cargando,
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = colores.onPrimaryContainer,
                    unfocusedTextColor = colores.onPrimaryContainer,
                    disabledTextColor = colores.onPrimaryContainer.copy(alpha = 0.4f),
                    focusedContainerColor = colores.secondaryContainer,
                    unfocusedContainerColor = colores.secondaryContainer,
                    disabledContainerColor = colores.secondaryContainer.copy(alpha = 0.5f),
                    focusedBorderColor = colores.secondary,
                    unfocusedBorderColor = colorBorde,
                    disabledBorderColor = colores.secondary.copy(alpha = 0.25f),
                    errorBorderColor = colores.error,
                    focusedLabelColor = colores.secondary,
                    unfocusedLabelColor = colores.onPrimaryContainer.copy(alpha = 0.7f),
                    disabledLabelColor = colores.onPrimaryContainer.copy(alpha = 0.4f),
                    errorLabelColor = colores.error,
                    focusedLeadingIconColor = colores.secondary,
                    unfocusedLeadingIconColor = colores.secondary.copy(alpha = 0.7f),
                    disabledLeadingIconColor = colores.onPrimaryContainer.copy(alpha = 0.4f),
                    errorLeadingIconColor = colores.error,
                    cursorColor = colores.secondary
                )
            )
        }

        if (viewModel.error == true) {
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.padding(start = 44.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = "Error",
                    tint = colores.error,
                    modifier = Modifier.size(13.dp)
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = "EL nuevo nombre debe ser mayor a 3 letras",
                    color = colores.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun TemaVisual(
    titulo: String,
    tema: String,
    acento: Color,
    fondo: Color,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colores = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .shadow(
                elevation = if (seleccionado) 8.dp else 3.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(fondo)
            .border(
                width = if (seleccionado) 2.dp else 1.dp,
                color = if (seleccionado) {
                    colores.secondary
                } else {
                    colores.secondary.copy(alpha = 0.35f)
                },
                shape = RoundedCornerShape(16.dp)
            )
            .clickable {
                println("hi")
                ConfiguracionTema.cambiarTema(tema)
                println("ho")
                onClick()
                println("he")
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    ConsulFirebase.ModificarTema(tema)
                }
                println("hu")
            }
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(acento)
        )

        Text(
            text = titulo,
            color = colores.onPrimary,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun OpcionAvatar(
    recurso: Int,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colores = MaterialTheme.colorScheme
    val forma = RoundedCornerShape(16.dp)

    Box(
        modifier = modifier
            .shadow(if (    seleccionado) 8.dp else 3.dp, forma)
            .clip(forma)
            .background(colores.secondaryContainer)
            .border(
                width = if (seleccionado) 2.dp else 1.dp,
                color = if (seleccionado) colores.secondary
                else colores.secondary.copy(alpha = 0.35f),
                shape = forma
            )
            .clickable { onClick() }
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        ImagenAvatar(recurso = recurso, tamano = 110.dp)
    }
}