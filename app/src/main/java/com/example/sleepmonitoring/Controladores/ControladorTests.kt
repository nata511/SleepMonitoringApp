    package com.example.sleepmonitoring.Controladores

    import android.util.Log
    import com.example.sleepmonitoring.Analisis.Test.DeteccionAtenea
    import com.example.sleepmonitoring.Analisis.Test.DeteccionPSQI
    import com.example.sleepmonitoring.BasesDatosFirebase.ConsulFirebase
    import com.example.sleepmonitoring.BasesDatosFirebase.DatosAtenea
    import com.example.sleepmonitoring.BasesDatosFirebase.DatosMCTQ
    import com.example.sleepmonitoring.BasesDatosFirebase.DatosPSQI
    import com.example.sleepmonitoring.BasesDatosFirebase.DatosSomnolencia
    import com.example.sleepmonitoring.BasesDatosFirebase.DatosSueño
    import com.example.sleepmonitoring.Analisis.Test.DeteccionSomnolencia
    import com.example.sleepmonitoring.Analisis.Test.DeteccionMCTQ
    import com.example.sleepmonitoring.BasesDatosFirebase.DatosCuestionario
    import com.example.sleepmonitoring.BasesDatosFirebase.Dimesion
    import com.example.sleepmonitoring.BasesDatosFirebase.preguntasSomnolencia
    import com.example.sleepmonitoring.Visual.interfaces.ViesModelyDatas.RespuestasEpworth
    import kotlinx.coroutines.CoroutineExceptionHandler
    import kotlinx.coroutines.CoroutineScope
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.SupervisorJob
    import kotlinx.coroutines.flow.MutableSharedFlow
    import kotlinx.coroutines.flow.asSharedFlow
    import kotlinx.coroutines.launch
    import java.time.LocalDate

    object ControladorTests {
        val consul= ConsulFirebase
        private val handler = CoroutineExceptionHandler  { _, e ->
            Log.e("TESTS", "Error guardando en Firestore", e)
        }
        private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO + handler)


        fun PuenteTestsDimensiones( dimensiones: DatosSueño, cuestionario: DatosCuestionario, somnolencia: preguntasSomnolencia?){
            val psqi= DeteccionPSQI( cuestionario.medi, dimensiones.duracion , dimensiones.eficiencia , dimensiones.cant_deper, cuestionario.calidad , cuestionario.Somnolencia,dimensiones.latencia )
            val datos= DeteccionAtenea(dimensiones.cant_deper, dimensiones.hora_final_R, dimensiones.duracion , cuestionario.calidad , cuestionario.malestar , cuestionario.rendimiento , cuestionario.Somnolencia , dimensiones.latencia).CoordinadorDimesiones()
            psqi.CoordinadorVariables()
            psqi.AlmacenarDatos(datosAtenea = datos)
            if(somnolencia!=null){
                DeteccionSomnolencia(somnolencia.sentado, somnolencia.viendo , somnolencia.inactivo_publico , somnolencia.copiloto_auto , somnolencia.acostado , somnolencia.hablando, somnolencia.despues_comer, somnolencia.auto_trafico).CoordinadorVarriables()

            }
            var diferencia= consul.diaUnion()
            if(diferencia>7 && LocalDate.now().dayOfWeek.value==7){
                scope.launch {
                    IniciarMCTQ()
                }

            }
        }

        suspend fun IniciarMCTQ(): String {
            val lista = consul.obtenerListaDias()

            if (lista != null) {
                val dato = consul.DatosParaMCTQ(lista)
                if (dato != null) {
                    DeteccionMCTQ(
                        dato.duracionFinde, dato.duracionLaboral, dato.promLaboralIni,
                        dato.promLaboralFin, dato.promFindeIni, dato.promFindeFin
                    ).CoordinadorVariables()
                    return "Éxito"
                }
            }

            return "Datos Insuficientes"
        }
        suspend fun objetosEncuestas(cuestio: DatosCuestionario){
            val dimension =  consul.ObtenerDatosSueno()
            if (dimension!= null){
                PuenteTestsDimensiones(dimension,cuestio,null)}
        }


        suspend fun ConsultasResultadoAtenea(): Dimesion {
            val sueno =consul.ObtenerDatosSueno()
            val datosAtenea= consul.ObtenerDatosAtenea()
            if (datosAtenea!= null && sueno!= null){
                var lista = listOf<String>(
                    ControladorHora.Long_Texto(sueno.latencia),
                    sueno.cant_deper.toString(),
                    ControladorHora.Long_Texto(datosAtenea.desperTemprano),
                    ControladorHora.Long_Texto(sueno.duracion),
                    datosAtenea.calidad,
                    datosAtenea.malestar,
                    datosAtenea.rendimiento,
                    datosAtenea.somnolencia,
                    datosAtenea.riesgo,
                    datosAtenea.p_total.toString()
                    )
                val punt= listOf<String>(
                    datosAtenea.p_Latencia.toString(),
                    datosAtenea.p_Despertares.toString(),
                    datosAtenea.p_DesperTemprano.toString(),
                    datosAtenea.p_Duracion.toString(),
                    datosAtenea.p_Calidad.toString(),
                    datosAtenea.p_Malestar.toString(),
                    datosAtenea.p_Rendimiento.toString(),
                    datosAtenea.p_Somnolencia.toString()

                )
                val datos = Dimesion()
                datos.puntajes.addAll(punt)
                datos.resultados.addAll(lista)
                return datos
            }
            val datos = Dimesion()
            return datos
        }
        suspend fun ConsultasResultadoPSQI(): Dimesion {
            val sueno = consul.ObtenerDatosSueno()
            val datosPSQI = consul.ObtenerDatosPSQI()
            if (datosPSQI != null && sueno != null) {
                var lista = listOf<String>(
                    ControladorHora.Long_Texto(sueno.latencia),
                    ControladorHora.Long_Texto(sueno.duracion),
                    datosPSQI.calidad,
                    sueno.eficiencia.toString(),
                    sueno.cant_deper.toString(),
                    datosPSQI.somnolencia,
                    datosPSQI.medicamento,
                    datosPSQI.riesgo,
                    datosPSQI.p_total.toString()
                )
                val punt= listOf<String>(
                    datosPSQI.p_Latencia.toString(),
                    datosPSQI.p_Duracion.toString(),
                    datosPSQI.p_Calidad.toString(),
                    datosPSQI.p_Eficiencia.toString(),
                    datosPSQI.p_Despertares.toString(),
                    datosPSQI.p_Somnolencia.toString(),
                    datosPSQI.p_Medicamentos.toString(),

                )
                val datos = Dimesion()
                datos.resultados.addAll(lista)
                datos.puntajes.addAll(punt)
                return datos
            }
            val datos = Dimesion()
            return datos
        }
        suspend fun ConsultasResultadoMCTQ(): Dimesion {
            val datosMCTQ = consul.ObtenerDatosMCTQ()
            if (datosMCTQ != null) {
                var lista = listOf<String>(
                    datosMCTQ.promLaboralIni?.toString() ?: "",
                    datosMCTQ.promLaboralFin?.toString() ?: "",
                    datosMCTQ.promFindeIni?.toString() ?: "",
                    datosMCTQ.promFindeFin?.toString() ?: "",
                    datosMCTQ.duracionLaboral.toString(),
                    datosMCTQ.puntoMedio.toString(),
                    datosMCTQ.duracionFinde.toString(),
                    datosMCTQ.jetLag.toString(),
                    datosMCTQ.cronotipo,
                    datosMCTQ.riesgo,
                )
                val datos = Dimesion()
                datos.resultados.addAll(lista)
                return datos
            }
            val datos = Dimesion()
            return datos
        }
        suspend fun ConsultasResultadoSomnolencia(): Dimesion {
            val datosSomnolencia = consul.ObtenerDatosSomnolencia()
            if (datosSomnolencia != null) {
                val resultadosLista = listOf(          // respuestas + riesgo + total
                    datosSomnolencia.sentado,
                    datosSomnolencia.viendo,
                    datosSomnolencia.inactivo_publico,
                    datosSomnolencia.copiloto_auto,
                    datosSomnolencia.acostado,
                    datosSomnolencia.hablando,
                    datosSomnolencia.despues_comer,
                    datosSomnolencia.auto_trafico,
                    datosSomnolencia.riesgo,                    // índice 8
                    datosSomnolencia.puntajeTotal.toString()    // índice 9
                )
                val puntajesLista = listOf(            // puntos por pregunta
                    datosSomnolencia.p_sentado.toString(),
                    datosSomnolencia.p_viendo.toString(),
                    datosSomnolencia.p_inactivo_publico.toString(),
                    datosSomnolencia.p_copiloto_auto.toString(),
                    datosSomnolencia.p_acostado.toString(),
                    datosSomnolencia.p_hablando.toString(),
                    datosSomnolencia.p_despues_comer.toString(),
                    datosSomnolencia.p_auto_trafico.toString()
                )
                val datos = Dimesion()
                datos.resultados.addAll(resultadosLista)
                datos.puntajes.addAll(puntajesLista)
                return datos
            }
            val datos = Dimesion()
            return datos
        }

        suspend fun AlmacenarDatosSueno(Datos: DatosSueño) {
            val datosResumen = ControladorDatosCrudos.ModicarDatosSueñoResumen(Datos)
            consul.insertarDatosSueno(Datos, datosResumen)
        }

        fun AlmacenarPSQI(Datos: DatosPSQI, datosAtenea: DatosAtenea){
            scope.launch {
                consul.insertarDatosPSQI(Datos,datosAtenea)
            }
        }
        fun AlmacenarMCTQ(Datos: DatosMCTQ){
            scope.launch {
                consul.insertarDatosMCQT(Datos)
            }
        }
        fun ConexionSomno(datos: RespuestasEpworth){
            DeteccionSomnolencia(datos.sentado,datos.viendo,datos.inactivo_publico,datos.copiloto_auto,
                datos.acostado,datos.hablando,datos.despues_comer,datos.auto_trafico).CoordinadorVarriables()

        }
        fun AlmacenarAtenea(Datos: DatosAtenea){
            scope.launch {
                consul.insertarDatosAtenea(Datos)
            }
        }
        fun AlmacenarSomnolencia(Datos: DatosSomnolencia){
            scope.launch {
                consul.insertarDatosSomnolencia(Datos)
            }
        }
    }