package com.example.sleepmonitoring.BasesDatosFirebase

data class DatosSueño(
    val hora_acostar: Long = 0L,
    val hora_levantar:Long =0L,
    val hora_inicio_R:Long = 0L,
    val hora_final_R: Long = 0L,
    val Dia:Int=1,
    val latencia: Long=0L,
    val duracion: Long=0L,
    val despertares:Long=0L,
    val cant_deper:Int=0,
    val profundidad: String="",
    val eficiencia: Float = 0.0f,
    val riesgo:String="",
    val fraseRiesgo:String="",
    val racha:Int=0,
    val estadoCuestionatio: Boolean=false
)
