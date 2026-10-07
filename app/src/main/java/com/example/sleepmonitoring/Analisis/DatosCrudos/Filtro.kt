package com.example.sleepmonitoring.Analisis.DatosCrudos
class Filtro {
    val nano = 1_000_000_000L
    fun unionBloques(listaBloques : MutableList<MutableList<Angulos>>, umbral: List<Int>): Union {
        var indice = 0
        val listaNueva: MutableList<MutableList<Angulos>> = mutableListOf()
        val espaciosVigilia: MutableList<MutableList<Angulos>> = mutableListOf()

        while (listaBloques.size > indice) {
            val x = indice
            while (true) {
                if (indice == listaBloques.size - 1) {
                    indice += 1
                    break
                }

                if ((listaBloques[indice + 1][1].angulo - listaBloques[indice][1].angulo) > umbral[0]*nano && listaBloques[indice + 1][1].angulo - listaBloques[indice][1].angulo <= umbral[1]*nano) {
                    espaciosVigilia.add(mutableListOf(Angulos(listaBloques[indice][1].tiempo,listaBloques[indice][1].angulo),
                        Angulos(listaBloques[indice+1][1].tiempo, listaBloques[indice+1][1].angulo) ))
                    indice += 1
                } else if ((listaBloques[indice + 1][1].angulo - listaBloques[indice][1].angulo) > umbral[1]*nano) {
                    espaciosVigilia.add(mutableListOf(Angulos(listaBloques[indice][1].tiempo,listaBloques[indice][1].angulo),
                        Angulos(listaBloques[indice+1][1].tiempo, listaBloques[indice+1][1].angulo) ))
                    indice += 1
                    break
                } else {
                    indice += 1
                    break
                }
            }

            if (indice != x) {
                listaNueva.add(
                    mutableListOf(
                        Angulos(listaBloques[x][0].tiempo, listaBloques[indice - 1][0].angulo),
                            Angulos(listaBloques[x][1].tiempo, listaBloques[indice - 1][1].angulo))
                    )

            }

        }
        return Union(listaNueva, espaciosVigilia)
    }
}
data class Union(
    val listaNueva: MutableList<MutableList<Angulos>> = mutableListOf(),
    val espaciosVigilia: MutableList<MutableList<Angulos>> = mutableListOf()
)