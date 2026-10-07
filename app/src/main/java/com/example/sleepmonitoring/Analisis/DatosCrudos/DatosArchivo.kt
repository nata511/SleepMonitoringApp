package com.example.sleepmonitoring.Analisis.DatosCrudos

import android.content.Context
import android.util.Log
import java.io.BufferedReader

class DatosArchivo(private val context: Context) : Puente {

    private val lec: BufferedReader =
        context.assets.open("781756_acceleration.txt").bufferedReader()

    private var contador = 0

    override fun movimiento(): String? {
        val linea = lec.readLine()
        if (contador < 3) Log.d("ARCHIVO", "línea $contador = '$linea'")
        contador++
        if (linea == null) Log.d("ARCHIVO", "fin de archivo, total líneas = $contador")
        return linea?.trim()
    }
}