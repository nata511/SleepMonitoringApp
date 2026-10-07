package com.example.sleepmonitoring.Analisis.DatosCrudos

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.sleepmonitoring.BaseDatos.SQLITE.AppDataBase
import com.example.sleepmonitoring.BaseDatos.SQLITE.Horas
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs

class VerificadorHora(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {

        val dao = AppDataBase.getDataBase(applicationContext).ConsultasDao()

        val horas = dao.obtenerHoras()

        if (horas.isEmpty()) {
            Log.w("WORKER", "La tabla Horas está vacía")
            return@withContext Result.success()
        }

        val ahora = LocalTime.now()

        val diferencia = horas.minOf { h ->
            diferenciaMinutos(h.horaInicio, ahora)
        }

        Log.d("WORKER", "Hora actual: $ahora")
        Log.d("WORKER", "Diferencia mínima: $diferencia minutos")

        if (diferencia <= 15) {
            Log.d("WORKER", "HORA CERCANA")
            iniciarAnalisis()
        } else {
            Log.d("WORKER", "Todavía no es hora")
        }
        val horaFin = horas.minOf { h ->
        h.horaFin
    }
        programarFin(horaFin)

        Result.success()

    }


    private fun diferenciaMinutos(
        horaInicio: String,
        horaActual: LocalTime
    ): Long {

        val formato = DateTimeFormatter.ofPattern("HH:mm")

        val inicio = LocalTime.parse(
            horaInicio,
            formato
        )

        val inicioMinutos =
            inicio.hour * 60 + inicio.minute

        val actualMinutos =
            horaActual.hour * 60 + horaActual.minute

        val diferencia =
            abs(actualMinutos - inicioMinutos)


        return minOf(
            diferencia,
            1440 - diferencia
        ).toLong()
    }

    private fun iniciarAnalisis() {

        println("WORKER - INICIANDO ANALISIS DEL SUEÑO")
        val intent = Intent(
            applicationContext,
            SensorService::class.java
        )

        ContextCompat.startForegroundService(
            applicationContext,
            intent
        )


    }
    private fun programarFin(hora: String) {
        val fin = LocalTime.parse(hora, DateTimeFormatter.ofPattern("HH:mm"))

        val momento = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, fin.hour)
            set(java.util.Calendar.MINUTE, fin.minute)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) add(java.util.Calendar.DAY_OF_YEAR, 1)
        }.timeInMillis

        val pending = PendingIntent.getBroadcast(
            applicationContext,
            1001,                                        // requestCode fijo: evita duplicados
            Intent(applicationContext, Finsueno::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val am = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val exacta = Build.VERSION.SDK_INT < Build.VERSION_CODES.S || am.canScheduleExactAlarms()

        if (exacta) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, momento, pending)
        } else {
            Log.w("FIN", "Sin permiso de alarma exacta, uso alarma inexacta")
            am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, momento, pending)
        }
        Log.d("FIN", "Alarma de fin programada para ${java.util.Date(momento)}")
    }
}