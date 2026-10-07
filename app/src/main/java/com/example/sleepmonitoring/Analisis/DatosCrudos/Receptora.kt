    package com.example.sleepmonitoring.Analisis.DatosCrudos
    import android.content.Context
    import kotlinx.coroutines.delay
    import kotlinx.coroutines.flow.MutableSharedFlow
    import kotlinx.coroutines.flow.asSharedFlow
    import kotlinx.coroutines.flow.first


    class Receptora(context: Context,val datos : DatosArchivo = DatosArchivo(context)
    ) {
        private val banda= MutableSharedFlow<DatosAcelerometro>()
        val bandaPublic = banda.asSharedFlow()
        var cont =0
        suspend fun esperarSuscriptores(cantidad: Int) {
            banda.subscriptionCount.first { it >= cantidad }
        }
        suspend fun obtencionDatos() {
            while (true) {
                val movi = datos.movimiento()
                if (movi != null) {
                    val partes = movi.trim().split(Regex("\\s+"))
                    if (partes.size < 4) continue   // línea mal formada

                    val tiempoSeg = partes[0].toDouble()
                    val dato = DatosAcelerometro(
                        tiempo = (tiempoSeg * 1_000_000_000).toLong(),  // a nanosegundos
                        x = partes[1].toFloat(),
                        y = partes[2].toFloat(),
                        z = partes[3].toFloat()
                    )
                    banda.emit(dato)
                } else {
                    banda.emit(DatosAcelerometro(tiempo = Long.MIN_VALUE, x = -1.0F, y = -1.0F, z = -1.0F))
                    break
                }
            }
        }
    }