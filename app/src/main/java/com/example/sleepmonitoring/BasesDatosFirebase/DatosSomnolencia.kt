package com.example.sleepmonitoring.BasesDatosFirebase

data class DatosSomnolencia(
    val sentado: String = "",
    val viendo: String = "",
    val inactivo_publico: String = "",
    val copiloto_auto: String = "",
    val acostado: String = "",
    val hablando: String = "",
    val despues_comer: String = "",
    val auto_trafico: String = "",
    val puntajeTotal: Int = 0,
    val p_sentado: Int = 0,
    val p_viendo: Int = 0,
    val p_inactivo_publico: Int = 0,
    val p_copiloto_auto: Int = 0,
    val p_acostado: Int = 0,
    val p_hablando: Int = 0,
    val p_despues_comer: Int = 0,
    val p_auto_trafico: Int = 0,
    val riesgo: String = ""
)
