package com.example.sleepmonitoring.Analisis.DatosCrudos.HDZC

import com.example.sleepmonitoring.Analisis.DatosCrudos.Receptora
import com.example.sleepmonitoring.BaseDatos.SQLITE.AppDataBase
import com.example.sleepmonitoring.BaseDatos.SQLITE.HDZC
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlin.math.atan2
import kotlin.math.sqrt
import android.content.Context
import com.example.sleepmonitoring.Analisis.DatosCrudos.DatosAcelerometro
import com.example.sleepmonitoring.Analisis.DatosCrudos.SensorService
import com.example.sleepmonitoring.BaseDatos.SQLITE.DatoNoche
import com.example.sleepmonitoring.BasesDatosFirebase.DatosConstantes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class EpocasHDZC ( context: Context, val flujo: Flow<DatosAcelerometro> ) {

    var epocas: MutableList<HDZC> = mutableListOf()
    private val base = AppDataBase.getDataBase(context).ConsultasDao()
    var fecha = DatosConstantes.fecha
    var tiempoI: Long = 0
    var tiempoF: Long = 0
    var duracion: Long = 5_000_000_000L
    val bufferx =mutableListOf<Float>()
    val buffery = mutableListOf<Float>()
    val bufferz = mutableListOf<Float>()

    suspend fun agregar(){

        val idNoche = base.obtener_indice_noche(fecha)?:0
        flujo.takeWhile { !(it.tiempo == -1L && it.x == -1.0F) }
            .collect { valores ->
                if (tiempoI == 0L) tiempoI = valores.tiempo
                if (valores.tiempo - tiempoI >= duracion) {
                    mediana(tiempoF, idNoche)
                    tiempoI = valores.tiempo
                }
                tiempoF = valores.tiempo
                bufferx.add(valores.x); buffery.add(valores.y); bufferz.add(valores.z)
            }

        mediana(tiempoF, idNoche)
        agregarDatos()


    }
    suspend fun mediana(tiempo: Long,id : Int){
        if (bufferx.isEmpty()) return
        var x = promedio(bufferx).toDouble()
        var y = promedio(buffery).toDouble()
        var z = promedio(bufferz).toDouble()
        var raiz: Double = sqrt(x * x + y * y)
        var angulo = Math.toDegrees(atan2(z, raiz))

        epocas.add(HDZC(id_dato_noche = id, tiempo = tiempo, angulo = angulo.toFloat()))
        if(epocas.size>=120){
            agregarDatos()
        }
        bufferx.clear()
        buffery.clear()
        bufferz.clear()


    }

    fun promedio(Lista: List<Float>): Float{
        var suma = Lista.sum()
        val largo = Lista.size
        return suma/largo
    }
    suspend fun agregarDatos(){

            base.InsertarHDZC(epocas)
            epocas.clear()

    }
}