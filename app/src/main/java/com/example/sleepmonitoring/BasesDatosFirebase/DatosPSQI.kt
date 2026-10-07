package com.example.sleepmonitoring.BasesDatosFirebase

data class DatosPSQI(
    val medicamento: String = "",
    val p_Latencia: Int = 0,
    val p_Duracion: Int = 0,
    val p_Calidad: Int = 0,
    val p_Eficiencia: Int = 0,
    val p_Despertares: Int = 0,
    val p_Somnolencia: Int = 0,
    val p_Medicamentos: Int = 0,
    val p_total: Int = 0,
    val calidad : String="",
    val somnolencia:String="",
    val sig_total:String="",
    val riesgo: String = ""
)
