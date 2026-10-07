package com.example.sleepmonitoring.Analisis.DatosCrudos.HDZC

import com.example.sleepmonitoring.Analisis.DatosCrudos.Angulos
import com.example.sleepmonitoring.Analisis.DatosCrudos.Filtro


class UmbralMovimiento {
    var bloques: MutableList<MutableList<Angulos>> = mutableListOf()
    val nano = 1_000_000_000L

    fun calcularPercentil(lista: MutableList<Angulos>): DatosRetorno {
        val movimientos = lista.map { it.angulo }
        val tiempos = lista.map { it.tiempo }
        val movimientosOrdenados = movimientos.sorted()
        val percen = (10*movimientosOrdenados.size)/100
        val umbral= movimientosOrdenados[percen]*15
        return DatosRetorno(umbral, movimientos,tiempos)

    }
    fun DeteccionBloques(lista: MutableList<Angulos>?): DatosRetorno2?{

        val medias = lista
        if (medias!=null){
            val(umbral,movimientos, tiempos)=calcularPercentil(medias)
            if (umbral<=0){return null}
            var inicio=-1
            for (i in movimientos.indices) {
                if (movimientos[i] < umbral) {
                    if (inicio == -1) inicio = i
                } else if (inicio != -1) {
                    bloques.add(mutableListOf(
                        Angulos(tiempos[inicio], movimientos[inicio]),
                        Angulos(tiempos[i - 1], movimientos[i - 1])))
                    inicio = -1
                }
            }
            if (inicio != -1) {
                val u = movimientos.lastIndex
                bloques.add(mutableListOf(
                    Angulos(tiempos[inicio], movimientos[inicio]),
                    Angulos(tiempos[u], movimientos[u])))
            }
            val filt = Filtro()
            val valores = filt.unionBloques(bloques, listOf(300,1800))
            bloques = valores.listaNueva
            val vigilia = valores.espaciosVigilia

            val ind = BloqueLargo() ?: return null
            MostrarInfo(bloques[0].map { it.angulo }, bloques[0].map { it.tiempo })
            return DatosRetorno2(ind,vigilia)
        }
        return null}


    fun BloqueLargo(): MutableList<Angulos>?{
        val seleccionados: MutableList<MutableList<Angulos>> = mutableListOf()

        var x = bloques.size
        for(i in 0 until x){
            if(bloques[i][1].tiempo -bloques[i][0].tiempo>1800*nano ){
                seleccionados.add(bloques[i])
            }

        }
        x = seleccionados.size
        if (x>=1){
            var mayor: Long = seleccionados[0][1].tiempo - seleccionados[0][0].tiempo
            var indice =0
            for(i in 1 until x){
                var resta=seleccionados[i][1].tiempo - seleccionados[i][0].tiempo
                if(resta > mayor){
                    indice=i
                    mayor = resta

                }

            }
            return seleccionados[indice]
        }
        return null
    }
    fun MostrarInfo(movimientos: List<Float>, tiempos: List<Long>){
        println("En el algoritmo MAD")
        println("Movimientos:${movimientos[0]} - ${movimientos[1]} ")
        println("Movimientos:${tiempos[0]}  - ${tiempos[1]} ")

    }
}
data class DatosRetorno(
    val percentil: Float,
    val movimiento: List<Float>,
    val tiempos: List<Long>

)

data class DatosRetorno2(
    val ind: MutableList<Angulos>,
    val vigilia: MutableList<MutableList<Angulos>>
)
