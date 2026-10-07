        package com.example.sleepmonitoring.Analisis.DeteccionDimensiones
        import androidx.annotation.RestrictTo
        import androidx.compose.runtime.ControlledComposition
        import com.example.sleepmonitoring.Analisis.DatosCrudos.Angulos
        import com.example.sleepmonitoring.BasesDatosFirebase.ConsulFirebase
        import com.example.sleepmonitoring.BasesDatosFirebase.DatosConstantes
        import com.example.sleepmonitoring.BasesDatosFirebase.DatosSueño
        import com.example.sleepmonitoring.Controladores.ControladorAlmacenaje
        import com.example.sleepmonitoring.Controladores.ControladorHora
        import com.example.sleepmonitoring.Controladores.ControladorTests
        import kotlinx.coroutines.CoroutineScope
        import kotlinx.coroutines.Dispatchers
        import kotlinx.coroutines.launch
        import kotlin.collections.MutableList
        import kotlin.math.abs
        import kotlin.time.Duration.Companion.milliseconds
        import kotlin.time.Duration.Companion.nanoseconds

        class DimensionesSueño(val lista:MutableList<MutableList<Angulos>>) {
            var totalSueño = 0L
            var racha=0
            var tiempoCama = 0L

            var eficen = 0.0F
            var duracionDesper = 0L
            var cantDesper:Int=0
            var latencia:Long=0L
            var profun=""
            var fraseRiesgo=""
            var riesgo =""

            suspend fun CoordinadorVariables(
                espaVigilia: List<List<Angulos>>,
                vigilia: List<List<Angulos>>
            ): DatosSueño {
                ObtenerLatencia()
                ConteoDespertares(espaVigilia, vigilia)
                DuracionSueno()
                eficiencia()
                profundidad()
                DeteccionRiesgo()
                mensajeRiesgo()
                CalculoRacha()
                return almacenarDatosSueno()
            }


            fun inicioSueño(): Long{
                return lista[0][0].tiempo
            }
            fun finSueño(): Long{
                return lista.last()[1].tiempo
            }
            fun ObtenerLatencia(){
                var hora_inicio= DatosConstantes.horaInicio
                latencia = inicioSueño()-hora_inicio
                latencia = latencia.coerceAtLeast(0L).nanoseconds.inWholeMinutes
            }
            fun DuracionSueno(){
                var tiempo = finSueño()-inicioSueño()

                totalSueño = tiempo - duracionDesper
                totalSueño = totalSueño.nanoseconds.inWholeMinutes
            }


            fun eficiencia(){
                tiempoCama = DatosConstantes.horaFin - DatosConstantes.horaInicio
                tiempoCama=tiempoCama.nanoseconds.inWholeMinutes
                eficen = if (tiempoCama > 0) {
                    ((totalSueño.toFloat() / tiempoCama) * 100f).coerceIn(0f, 100f)
                } else {
                    0f
                }
            }
            fun ConteoDespertares(
                espaVigilia: List<List<Angulos>>,
                vigilia: List<List<Angulos>>,
                toleranciaNs: Long = 10L * 60L * 1_000_000_000L    // 200 ms en nanosegundos
            ) {
                val ini = inicioSueño()
                val fin = finSueño()


                val intervalos = (espaVigilia + vigilia)
                    .map { it[0].tiempo to it[1].tiempo }
                    .map { (a, b) -> maxOf(a, ini) to minOf(b, fin) }
                    .filter { (a, b) -> b > a }
                    .sortedBy { it.first }

                val fusionados = mutableListOf<Pair<Long, Long>>()
                for ((a, b) in intervalos) {
                    val ultimo = fusionados.lastOrNull()
                    if (ultimo != null && a <= ultimo.second + toleranciaNs) {
                        fusionados[fusionados.lastIndex] = ultimo.first to maxOf(ultimo.second, b)
                    } else {
                        fusionados.add(a to b)
                    }
                }

                cantDesper = fusionados.size
                duracionDesper = fusionados.sumOf { it.second - it.first }
            }


            fun fragmentacion(): Double{
                var frag=0.0
                if (cantDesper>0 && totalSueño>0) {
                    frag =cantDesper / totalSueño.toDouble()
                }
                return frag
            }
            fun profundidad() {
                profun = when {
                    eficen > 80 && cantDesper <= 2 -> "Profundo"
                    eficen > 60 && cantDesper <= 3 -> "Moderado"
                    else -> "Ligero"
                }
            }
            fun DeteccionRiesgo() {
                var puntaje = 0

                when {
                    totalSueño < 300 -> puntaje += 3
                    totalSueño < 360 -> puntaje += 2
                    totalSueño < 420 -> puntaje += 1
                }

                when {
                    eficen < 70f -> puntaje += 3
                    eficen < 80f -> puntaje += 2
                    eficen < 85f -> puntaje += 1
                }

                riesgo = when{
                    puntaje >= 5 -> "Alto"
                    puntaje >= 3 -> "Moderado"
                    puntaje >= 1 -> "Bajo"
                    else -> "Sin riesgo"
                }
            }
            fun mensajeRiesgo(){
                fraseRiesgo = when (riesgo) {
                    "Alto" -> "Se observa un alto riesgo de alteraciones del sueño."
                    "Moderado" -> "Se observa un riesgo moderado de alteraciones del sueño."
                    "Bajo" -> "Se observa un bajo riesgo de alteraciones del sueño."
                    "Sin riesgo" -> "La duración y eficiencia del sueño se encuentran dentro de los parámetros evaluados."
                    else -> "No fue posible determinar el nivel de riesgo."
                }
            }

            suspend fun almacenarDatosSueno(): DatosSueño{
                var ini= inicioSueño()
                var fin= finSueño(  )

                var dato = DatosSueño(DatosConstantes.horaFin, DatosConstantes.horaFin,
                    ini, fin, ControladorHora.DiaSemana(),
                    latencia, duracion = totalSueño, duracionDesper,cantDesper,  profun,eficen,riesgo,fraseRiesgo,racha,false)

                ControladorTests.AlmacenarDatosSueno(dato)
                return dato
            }

            suspend fun CalculoRacha() {
                val rachaVieja = ControladorAlmacenaje.PasarRacha() ?: 0
                racha = if (totalSueño >= 360) rachaVieja + 1 else 0
            }
        }


