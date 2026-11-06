        package com.example.sleepmonitoring.Analisis.DatosCrudos.ManejoSensores
    
        import android.app.AlarmManager
        import android.app.PendingIntent
        import android.content.Context
        import android.content.Intent
        import android.os.Build
        import android.util.Log
        import androidx.core.content.ContextCompat
        import com.example.sleepmonitoring.BaseDatos.SQLITE.AppDataBase
        import java.time.LocalDate
        import java.time.LocalTime
        import java.time.ZoneId
        import java.time.format.DateTimeFormatter
        import android.content.BroadcastReceiver
        import com.example.sleepmonitoring.Analisis.DatosCrudos.SensorService
        import kotlinx.coroutines.CoroutineScope
        import kotlinx.coroutines.Dispatchers
        import kotlinx.coroutines.launch
    
        object ProgramadorSueno {
    
            private const val RC_INICIO = 1000
            private const val RC_FIN = 1001
            private const val RC_MEDIANOCHE = 1002
            private val FORMATO = DateTimeFormatter.ofPattern("HH:mm")
    
            private data class Ventana(val inicio: Long, val fin: Long)
    
            suspend fun programar(context: Context) {
                val app = context.applicationContext
    
                cancelar(app, RC_INICIO, InicioSueno::class.java)
                cancelar(app, RC_FIN, Finsueno::class.java)
    
                programarMedianoche(app)
    
                val horas = AppDataBase.getDataBase(app).ConsultasDao().obtenerHoras()
                if (horas.isEmpty()) {
                    Log.w("PROGRAMADOR", "Tabla Horas vacía, no se programa nada")
                    return
                }
    
                val ahora = System.currentTimeMillis()
                val zona = ZoneId.systemDefault()
                val hoy = LocalDate.now()
    
                val ventana = horas.flatMap { h ->
                    val ini = LocalTime.parse(h.horaInicio, FORMATO)
                    val fin = LocalTime.parse(h.horaFin, FORMATO)
                    var duracion = fin.toSecondOfDay() - ini.toSecondOfDay()
                    if (duracion <= 0) duracion += 24 * 3600
    
                    (-1L..0L).map { d ->
                        val inicioMs = hoy.plusDays(d).atTime(ini)
                            .atZone(zona).toInstant().toEpochMilli()
                        Ventana(inicioMs, inicioMs + duracion * 1000L)
                    }
                }
                    .filter { it.fin > ahora }
                    .minByOrNull { it.inicio }
                    ?: run {
                        Log.d("PROGRAMADOR", "No quedan ventanas hoy; se programará a medianoche")
                        return
                    }
    
                if (ventana.inicio <= ahora) {
                    if (SensorService.activo) {
                        Log.d("PROGRAMADOR", "Dentro de la ventana, pero el sensor ya está activo")
                    } else {
                        Log.d("PROGRAMADOR", "Dentro de la ventana, iniciando ahora")
                        ContextCompat.startForegroundService(app, Intent(app, SensorService::class.java))
                    }
                } else {
                    alarma(app, RC_INICIO, InicioSueno::class.java, ventana.inicio)
                }
    
                alarma(app, RC_FIN, Finsueno::class.java, ventana.fin)
    
                Log.d(
                    "PROGRAMADOR",
                    "Inicio: ${java.util.Date(ventana.inicio)} | Fin: ${java.util.Date(ventana.fin)}"
                )
            }
    
            private fun programarMedianoche(context: Context) {
                val momento = LocalDate.now().plusDays(1).atStartOfDay()
                    .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                alarma(context, RC_MEDIANOCHE, ReprogramadorSueno::class.java, momento)
            }
    
            private fun pending(context: Context, rc: Int, clase: Class<*>): PendingIntent =
                PendingIntent.getBroadcast(
                    context, rc, Intent(context, clase),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
    
            private fun cancelar(context: Context, rc: Int, clase: Class<*>) {
                val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                am.cancel(pending(context, rc, clase))
            }
    
            private fun alarma(context: Context, rc: Int, clase: Class<*>, momento: Long) {
                val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val pi = pending(context, rc, clase)
                val exacta = Build.VERSION.SDK_INT < Build.VERSION_CODES.S || am.canScheduleExactAlarms()
                if (exacta) {
                    am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, momento, pi)
                } else {
                    Log.w("PROGRAMADOR", "Sin permiso de alarma exacta, uso inexacta")
                    am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, momento, pi)
                }
            }
        }
    
    
    
    
        class ReprogramadorSueno : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                Log.d("REPROGRAMADOR", "Acción: ${intent.action}")
                val pendiente = goAsync()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        ProgramadorSueno.programar(context)
                    } catch (e: Throwable) {
                        Log.e("REPROGRAMADOR", "Error al programar", e)
                    } finally {
                        pendiente.finish()
                    }
                }
            }
        }