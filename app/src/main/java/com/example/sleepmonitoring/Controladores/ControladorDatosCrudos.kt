package com.example.sleepmonitoring.Controladores

import com.example.sleepmonitoring.Analisis.DatosCrudos.HDZC.EpocasHDZC
import com.example.sleepmonitoring.Analisis.DatosCrudos.Receptora
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import com.example.sleepmonitoring.Analisis.DatosCrudos.Filtro
import com.example.sleepmonitoring.Analisis.DatosCrudos.HDZC.DeteccionHDZC
import com.example.sleepmonitoring.Analisis.DatosCrudos.HDZC.EmisionAngulosCrudos
import com.example.sleepmonitoring.Analisis.DatosCrudos.HDZC.UmbralMovimiento
import com.example.sleepmonitoring.Analisis.DatosCrudos.MAD.EpocasMAD
import com.example.sleepmonitoring.Analisis.DatosCrudos.MAD.FinalMAD
import com.example.sleepmonitoring.Analisis.DeteccionDimensiones.DimensionesSueño
import com.example.sleepmonitoring.Analisis.DeteccionDimensiones.Integracion
import com.example.sleepmonitoring.BasesDatosFirebase.ConsulFirebase
import com.example.sleepmonitoring.BasesDatosFirebase.DatosConstantes
import com.example.sleepmonitoring.BasesDatosFirebase.DatosSueño
import com.example.sleepmonitoring.BasesDatosFirebase.Dimesion
import com.example.sleepmonitoring.BasesDatosFirebase.ResumenUsuarios
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object ControladorDatosCrudos{

    var tiempoI:Long=0L
    private val handler = CoroutineExceptionHandler  { _, e ->
        Log.e("TESTS", "Error guardando en Firestore", e)
    }
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO + handler)


    fun reprocesarNoche(
        context: Context,
        resultado: (DatosSueño?) -> Unit
    ) {
            try {
                val tiempoBase = 3939686156989L
                DatosConstantes.horaInicio = tiempoBase
                DatosConstantes.horaFin =
                    tiempoBase + 28_200_000_000_000L
                tiempoI = 3939686156989L
                println("tiempoI = ${tiempoI}")
             procesarAngulosMovimientos(
                    context
                ) { datosSueño ->
                    resultado(datosSueño)
                }
            } catch (e: Exception) {
                println("ERROR reprocesando: ${e.message}")
                e.printStackTrace()
                resultado(null)
            }
        }


    fun procesarAngulosMovimientos(context: Context, resultado: (DatosSueño?) -> Unit) {
        scope.launch {

            try {
                var DeteHdzc = DeteccionHDZC(
                    context,
                    tiempoI = tiempoI,Emision = EmisionAngulosCrudos(
                        context,
                        DatosConstantes.fecha
                    )
                )
                var finalM = FinalMAD(context, DatosConstantes.fecha)
                DeteHdzc.obtencionDatos()

                finalM.agregar()

                if (DeteHdzc.medias.isNullOrEmpty()) {
                    withContext(Dispatchers.Main) { resultado(null) }
                    return@launch
                }else {
                    var temporal = UmbralMovimiento()
                        .DeteccionBloques(DeteHdzc.medias ?: null)


                    if (temporal != null) {
                        var (bloque, espaVigilia) = temporal
                        var mad = finalM.listaBloques()
                        if (bloque.isNullOrEmpty() || mad.isEmpty()) {
                            Log.w("ANALISIS", "Sin datos suficientes, no se procesa")
                            withContext(Dispatchers.Main) { resultado(null) }
                            return@launch
                        }


                        var bloqueFinal = Integracion().compararBloques(
                            listaMAD = mad,
                            bloqueHDCZ = bloque
                        )


                        var (valor, Vigilia) = Filtro().unionBloques(
                            bloqueFinal,
                            mutableListOf(300, 1800)
                        )

                        if (valor.isEmpty()) {
                            Log.w("ANALISIS", "Sin bloques de sueño tras la integración")
                            withContext(Dispatchers.Main) { resultado(null) }
                            return@launch
                        }
                        var DatosSue = DimensionesSueño(valor)
                            .CoordinadorVariables(
                                espaVigilia,
                                Vigilia
                            )
                        withContext(Dispatchers.Main) {
                            resultado(DatosSue)
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            resultado(null)
                        }
                    }
                }

            } catch (e: Exception) {
                println("ERROR - Se produjo una excepción")
                println("ERROR - Mensaje: ${e.message}")
                println("ERROR - Tipo: ${e.javaClass.name}")
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    println("ERROR - enviando NULL al resultado")
                    resultado(null)
                }
            }
        }
    }
    fun ModicarDatosSueñoResumen(datosSueño: DatosSueño): ResumenUsuarios {


        val datosRen = ResumenUsuarios(
            ControladorHora.Long_Texto(datosSueño.duracion),

            ControladorHora.Long_Texto(datosSueño.latencia),
            datosSueño.eficiencia.toString(),
            datosSueño.cant_deper.toString(),
            datosSueño.riesgo,
            false,
            null,
            null,
            false,
            null,
            null,
            false,
            null,
            null,
            false,
            null,
            null,
            null,
            DatosConstantes.fecha
        )

        return datosRen

    }


}
