package com.example.sleepmonitoring.Visual.interfaces.Vistas

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sleepmonitoring.Visual.interfaces.ViesModelyDatas.ViewModelTerminos
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun Terminos(
    navController: NavController,
    viewModelTerminos: ViewModelTerminos = viewModel()
) {

    var selectedItem by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController,
                selectedIndex = selectedItem,
                onTabSelected = { newIndex ->
                    selectedItem = newIndex
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(vertical = 14.dp)
        ) {

            Text(
                text = "Términos y Condiciones",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 12.dp,
                        start = 26.dp,
                        end = 26.dp
                    )
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 26.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                Text(
                    text = """
                        1 ACEPTACIÓN DE LOS TÉRMINOS:
                        Al continuar, registrarse o utilizar esta plataforma, usted acepta expresamente de manera libre, voluntaria e informada los presentes Términos y Condiciones de Servicio, así como la Política de Tratamiento de datos personales de la aplicación.

                        2 ACCESO A SENSORES Y FUNCIONAMIENTO:
                        Para cumplir con la finalidad del servicio, la aplicación requiere acceder a los sensores de su dispositivo móvil tales como acelerómetro, destinados exclusivamente a la detección, registro y monitoreo de sus ciclos de sueño. El usuario otorga su autorización expresa para dicho acceso al activar la funcionalidad correspondiente.

                        3 EXCLUSIÓN DE RESPONSABILIDAD MÉDICA (DESCARGO DE RESPONSABILIDAD):
                        Usted comprende y acepta que esta herramienta es un software de bienestar y optimización del descanso que no constituye un diagnóstico médico, examen clínico, ni análisis de salud válido. Las mediciones, estadísticas y datos presentados son únicamente de carácter informativo y referencial. Esta aplicación no sustituye en ningún caso la consulta, opinión, diagnóstico o tratamiento de un profesional de la salud calificado. El uso de la herramienta y la interpretación de sus resultados se realizan bajo su propia cuenta, riesgo y estricta responsabilidad. Si experimenta trastornos severos del sueño o requiere atención médica, debe acudir a un médico especialista.

                        4 PROTECCIÓN DE DATOS PERSONALES (LEY 1581 DE 2012):
                        En cumplimiento de la Ley 1581 de 2012 de Colombia y sus decretos reglamentarios, le informamos que sus datos personales, incluyendo la información sensible relacionada con sus hábitos de descanso, serán recolectados y tratados de forma estrictamente confidencial. El almacenamiento de su información de descanso es persistente, seguro y se encuentra completamente encriptado en entornos de bases de datos altamente protegidos mediante tecnologías de Firestore y MySQL. Como titular de los datos, usted conserva los derechos de conocer, actualizar, rectificar y solicitar la supresión de su información en cualquier momento.
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    lineHeight = MaterialTheme.typography.bodySmall.lineHeight
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("terminos_aceptados", true)

                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 26.dp)
            ) {
                Text(
                    text = "Aceptar",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
