package com.example.sleepmonitoring.Analisis.DatosCrudos.MAD

import com.example.sleepmonitoring.Analisis.DatosCrudos.Receptora
import com.example.sleepmonitoring.BaseDatos.SQLITE.AppDataBase
import com.example.sleepmonitoring.BaseDatos.SQLITE.MAD
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlin.math.abs
import kotlin.math.sqrt
import android.content.Context
import com.example.sleepmonitoring.Analisis.DatosCrudos.DatosAcelerometro
import com.example.sleepmonitoring.BasesDatosFirebase.DatosConstantes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class EpocasMAD( context: Context,val flujo: Flow<DatosAcelerometro>) {


    val epocas: MutableList<MAD> = mutableListOf()

    var buffe : MutableList<Float> = mutableListOf()
    var tiempoI: Long = 0L
    var tiempoF: Long = 0L
    var duracion: Long = 5_000_000_000L
    val base = AppDataBase.getDataBase(context).ConsultasDao()
    var fecha = DatosConstantes.fecha
    suspend fun agregar(){
        val idNoche = base.obtener_indice_noche(fecha)?:0
        flujo.takeWhile { !(it.tiempo == -1L && it.x == -1.0F) }
            .collect { valores ->

            if (tiempoI == 0L && valores.tiempo != -1L) {
                tiempoI = valores.tiempo
            }
            if (valores.tiempo != -1L && valores.x != -1F) {
                tiempoF = valores.tiempo
            }
            if ((valores.tiempo == -1L && valores.x == -1.0F) || (valores.tiempo - tiempoI) >= duracion) {
                if(epocas.size>=120){
                    agregarDatos()
                }
                var desviacion = desviacioMedia(buffe)

                epocas.add(MAD(id_noche = idNoche, tiempo = tiempoF , desviacion = desviacion))
                buffe.clear()
                tiempoI=valores.tiempo
                if (valores.tiempo == -1L && valores.x == -1.0F) {
                    agregarDatos()

                }
            }
            var raiz = sqrt(valores.x * valores.x + valores.y * valores.y + valores.z * valores.z)
            buffe.add(raiz/ 9.81f)
        }
        agregarDatos()

    }



    suspend fun desviacioMedia(lista:List<Float>): Float{
        if (lista.isEmpty()){
            return 0.0F
        }
        var suma = lista.sum()
        var largo = lista.size
        var prom= suma/largo
        val nuevaLista: MutableList<Float> =  mutableListOf()

        var resta: Float
        for (i in lista){
            resta = i-prom
            nuevaLista.add(resta)
        }
        var listaF = nuevaLista.map { abs(it) }
        suma = listaF.sum()
        largo = listaF.size
        return suma/largo

    }
     suspend fun agregarDatos() {
        if (epocas.isEmpty()) return
        val copia = epocas.toList()
        epocas.clear()
         withContext(Dispatchers.IO) { base.InsertarMAD(copia) }
    }


    /*
    fun listaBloques(): MutableList<MutableList<MutableList<Double>>>{
        var lista: MutableList<MutableList<MutableList<Double>>> = mutableListOf()
        for (valor in bloqueDormido){
            var valores = buscarIndice(valor)
            lista.add(valores)
        }
        return lista
    }
    fun buscarIndice(indices: MutableList<Int>): MutableList<MutableList<Double>>{
        var inicio = epocas[indices[0]]
        var fin = epocas[indices[1]]
        return mutableListOf(mutableListOf(inicio[1], fin[1]),mutableListOf(inicio[0], fin[0]))
    }
    */

}