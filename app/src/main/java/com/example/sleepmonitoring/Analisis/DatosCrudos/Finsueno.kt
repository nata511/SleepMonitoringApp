package com.example.sleepmonitoring.Analisis.DatosCrudos

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat

class Finsueno: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val stop = Intent(context, SensorService::class.java)
            .setAction(SensorService.ACCION_STOP)
        try {
            ContextCompat.startForegroundService(context, stop)
        } catch (e: Exception) {
            Log.e("FIN", "no se pudo enviar STOP", e)
        }
    }
}
