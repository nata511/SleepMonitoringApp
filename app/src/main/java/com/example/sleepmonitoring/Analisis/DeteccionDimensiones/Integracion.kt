package com.example.sleepmonitoring.Analisis.DeteccionDimensiones
import com.example.sleepmonitoring.Analisis.DatosCrudos.Angulos
import kotlin.math.abs
class Integracion {
    fun compararBloques(listaMAD:MutableList<MutableList<Angulos>>, bloqueHDCZ: MutableList<Angulos>): MutableList<MutableList<Angulos>>{


        var bloqueD : MutableList<block> = mutableListOf()
        var diferenciaT = 0L
        for(i in listaMAD){
            if (i[0].tiempo<bloqueHDCZ[1].tiempo && i[1].tiempo>bloqueHDCZ[0].tiempo){
                var diferencia1= abs(i[0].tiempo - bloqueHDCZ[0].tiempo)
                var diferencai2= abs(i[1].tiempo - bloqueHDCZ[1].tiempo)
                diferenciaT= diferencia1-diferencai2
                bloqueD.add(block(i,diferenciaT))

            }
        }
        for (fila in bloqueD){
            var inicio= maxOf(fila.MAD[0].tiempo,bloqueHDCZ[0].tiempo)
            fila.MAD[0].tiempo = inicio
            var fin = minOf(fila.MAD[1].tiempo, bloqueHDCZ[1].tiempo)
            fila.MAD[1].tiempo = fin

        }
        var bloques = bloqueD.map{it.MAD}.toMutableList()
        return bloques
    }
}
data class block(
    var MAD: MutableList<Angulos>,
    var diferencia: Long
)