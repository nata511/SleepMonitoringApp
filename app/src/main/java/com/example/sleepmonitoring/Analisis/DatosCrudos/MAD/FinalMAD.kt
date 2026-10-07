package com.example.sleepmonitoring.Analisis.DatosCrudos.MAD
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlin.math.sqrt
import com.example.sleepmonitoring.Analisis.DatosCrudos.Angulos
import android.content.Context
import com.example.sleepmonitoring.BaseDatos.SQLITE.AppDataBase
import kotlinx.coroutines.flow.takeWhile

class FinalMAD(context: Context, private val fech:String) {
    val epocas: MutableList<Angulos> = mutableListOf()
    private val base = AppDataBase.getDataBase(context).ConsultasDao()
    var estado = "Despierto"
    var buffe : MutableList<Float> = mutableListOf()
    val bloqueDormido:MutableList<MutableList<Int>> = mutableListOf()
    var tiempoI: Long = 0L
    var tiempoF: Long = 0L
    var duracion: Long = 5L
    var descanso:Int = 0
    var vigilia:Int = 0
    var sueñoI = 0
    var sueñoF = 0
    var con = 0

    suspend fun agregar() {
        val datos = base.obtener_datos_movimiento(fech)
        for (d in datos) {
            clasificar(d.desviacion, epocas.size)
            epocas.add(Angulos(d.tiempo, d.desviacion))

            if (estado == "Despierto" && descanso >= 900 ) {
                estado = "Dormido"
                bloqueDormido.add(mutableListOf(sueñoI))
            } else if (estado == "Dormido" && vigilia >= 900) {
                estado = "Despierto"
                bloqueDormido.last().add(sueñoF)
            }
        }
        if (bloqueDormido.isNotEmpty() && bloqueDormido.last().size == 1) {
            bloqueDormido.last().add(epocas.size - 1)
        }
    }


    fun clasificar(desviacion: Float, indice: Int) {
        val umbral = 0.03f

        if (desviacion < umbral) {
            if (descanso == 0) {
                sueñoI = indice
            }

            descanso += 5
            vigilia = 0

        } else {
            if (vigilia == 0) {
                sueñoF = indice
            }

            vigilia += 5
            sueñoF = indice
            descanso = 0
        }
    }

    fun listaBloques(): MutableList<MutableList<Angulos>>{
        var lista: MutableList<MutableList<Angulos>> = mutableListOf()
        for (valor in bloqueDormido){
            var valores = buscarIndice(valor)
            lista.add(valores)
        }
        return lista
    }

    fun buscarIndice(indices: MutableList<Int>): MutableList<Angulos>{
        var inicio = epocas[indices[0]]
        var fin = epocas[indices[1]]
        return mutableListOf(inicio, fin)
    }

}