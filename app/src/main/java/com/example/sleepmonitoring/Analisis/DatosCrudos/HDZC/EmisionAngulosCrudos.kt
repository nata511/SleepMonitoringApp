package com.example.sleepmonitoring.Analisis.DatosCrudos.HDZC

import android.content.Context
import android.util.Log
import com.example.sleepmonitoring.Analisis.DatosCrudos.Angulos
import com.example.sleepmonitoring.BaseDatos.SQLITE.AppDataBase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first

class EmisionAngulosCrudos(context: Context,private val fecha: String) {
    private val banda = MutableSharedFlow<Angulos>(
        replay = 0,
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.SUSPEND
    )

    val bandaPublic = banda.asSharedFlow()
    var cont =0
    private val base = AppDataBase.Companion.getDataBase(context).ConsultasDao()
    var final =false

    suspend fun esperarSuscriptores(cantidad: Int) {
        banda.subscriptionCount.first { it >= cantidad }
    }
    suspend fun  obtencionDatos(){



        val cantidad = base.cantidadHDZC()
        Log.d("DB", "HDZC filas = ${base.cantidadHDZC()}")

        println("DATABASE - Cantidad de registros HDZC: $cantidad")
        val datos = base.obtener_datos_angulos(fecha)
        for (d in datos) {
            banda.emit(Angulos(d.tiempo, d.angulo))
        }
        banda.emit(Angulos(-1L, -0.11111F))
    }
}