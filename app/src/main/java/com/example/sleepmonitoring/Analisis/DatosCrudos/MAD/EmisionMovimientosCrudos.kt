package com.example.sleepmonitoring.Analisis.DatosCrudos.MAD
/*
import android.content.Context
import com.example.sleepmonitoring.Analisis.DatosCrudos.Angulos
import com.example.sleepmonitoring.BaseDatos.SQLITE.AppDataBase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first

class EmisionMovimientosCrudos(context: Context) {
    private val banda= MutableSharedFlow<Angulos>()
    val bandaPublic = banda.asSharedFlow()
    var final =false
    private val base = AppDataBase.Companion.getDataBase(context).ConsultasDao()

    suspend fun esperarSuscriptores(cantidad: Int) {
        banda.subscriptionCount.first { it >= cantidad }
    }
    suspend fun  obtencionDatos(){

        val datos = base.obtener_datos_movimiento()
        for (d in datos) {
            banda.emit(Angulos(d.tiempo, d.desviacion))
        }
        banda.emit(Angulos(-1L, -0.11111F))

    }

}*/