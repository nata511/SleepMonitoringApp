package com.example.sleepmonitoring.BasesDatosFirebase

data class DatosAtenea(
    val desperTemprano: Long =0L,
    val malestar: String = "",
    val calidad : String="",
    val somnolencia:String="",
    val rendimiento: String = "",
    val p_Latencia: Int = 0,
    val p_Despertares: Int = 0,
    val p_DesperTemprano: Int = 0,
    val p_Duracion: Int = 0,
    val p_Calidad: Int = 0,
    val p_Malestar: Int = 0,
    val p_Rendimiento: Int = 0,
    val p_Somnolencia: Int = 0,
    val p_total: Int = 0,
    val riesgo: String = ""
)
