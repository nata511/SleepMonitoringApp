package com.example.sleepmonitoring.Analisis.DatosCrudos

data class DatosAcelerometro(
    val tiempo: Long,
    val x: Float,
    val y: Float,
    val z: Float)

data class Angulos(
    var tiempo: Long,
    var angulo: Float
)