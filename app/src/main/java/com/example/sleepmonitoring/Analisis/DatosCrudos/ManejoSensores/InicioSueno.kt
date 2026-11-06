package com.example.sleepmonitoring.Analisis.DatosCrudos.ManejoSensores

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.sleepmonitoring.Analisis.DatosCrudos.SensorService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InicioSueno : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (SensorService.activo) {
            Log.d("INICIO", "Alarma ignorada: el sensor ya está activo")
            return
        }
        Log.d("INICIO", "Alarma de inicio recibida, arrancando sensor")
        ContextCompat.startForegroundService(
            context, Intent(context, SensorService::class.java)
        )
    }
}
class Finsueno : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val app = context.applicationContext
        val pendiente = goAsync()

        if (SensorService.corriendo) {
            Log.d("FIN", "Alarma de fin recibida, deteniendo sensor")
            ContextCompat.startForegroundService(
                app,
                Intent(app, SensorService::class.java).setAction(SensorService.ACCION_STOP)
            )
        } else {
            Log.d("FIN", "Alarma ignorada: no hay sesión corriendo")
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                ProgramadorSueno.programar(app)
            } catch (e: Throwable) {
                Log.e("FIN", "Error al reprogramar", e)
            } finally {
                pendiente.finish()
            }
        }
    }
}