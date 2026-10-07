package com.example.sleepmonitoring.Analisis.DatosCrudos.HDZC

import android.content.Context
import com.example.sleepmonitoring.Analisis.DatosCrudos.Angulos
import com.example.sleepmonitoring.Analisis.DatosCrudos.HDZC.EmisionAngulosCrudos
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import java.util.ArrayDeque
import kotlin.math.abs

class DeteccionHDZC(context: Context, var Emision: EmisionAngulosCrudos, var tiempoI: Long
) {

    var epocas = object : ArrayDeque<Angulos>(2){
        override fun add(element: Angulos): Boolean {
            if (size>=2) removeFirst()
            return super.add(element)
        }
    }
    var medias: MutableList<Angulos>? = mutableListOf()
    val diferen = object : ArrayDeque<Angulos>(60){
        override fun add(element: Angulos): Boolean {
            if (size>=60) removeFirst()
            return super.add(element)
        }
    }
    var indice = 0


    suspend fun obtencionDatos(){

        coroutineScope {

            launch(start = CoroutineStart.UNDISPATCHED) {

                Emision.bandaPublic
                    .takeWhile { valores ->
                        valores.angulo != -0.11111F &&
                                valores.tiempo != -1L
                    }
                    .collect { valores ->

                        println("HDZC - Dato recibido: ${valores.tiempo}")

                        epocas.add(valores)

                        diferencia()
                    }
            }
            println("HDZC - Esperando suscriptor")

            Emision.esperarSuscriptores(1)


            println("HDZC - Collect iniciado")

            Emision.obtencionDatos()

            println("HDZC - Emision terminó")
        }

        println("HDZC - Todo terminó")
    }


    fun operacionMediana(lista: List<Float>): Float{
        val listaOrdenada = lista.sorted()
        val largo = listaOrdenada.size
        if(largo%2==0){
            var valor1= largo/2
            var valor2= largo/2 - 1
            var resultado = (listaOrdenada[valor1]+listaOrdenada[valor2])/2
            return resultado
        }else{
            var valor = largo/2
            return listaOrdenada[valor]
        }
    }

    fun diferencia(): MutableList<Angulos>? {
        val x = epocas.size
        if (x >= 2){
            var dife = abs(epocas.last().angulo - epocas.first().angulo)
            diferen.add(Angulos(epocas.first().tiempo, dife))
        }else {
            var valor = mutableListOf<List<Double>>()
            return null

        }
        if (diferen.size == 60){
            var columna:List<Float> = diferen.map{it.angulo}
            val median = operacionMediana(columna)
            medias?.add(Angulos(epocas.last().tiempo, median))
            indice+=1
            if (tiempoI == 0L){
                if (medias.isNullOrEmpty()){
                    medias= null
                }
                return medias
            }
        }
        var valor = mutableListOf<List<Double>>()
        return null
    }
}