package com.example.sleepmonitoring.BasesDatosFirebase
data class ResumenUsuarios(
    val duracion: String? = "",
    val latencia: String? = "0h:0m",
    val eficiencia: String? = "0.0",
    val despertares_: String? = "0",
    val riesgoSueño: String? = "",

    val atenas_completado: Boolean? = false,
    val atenas_puntaje: Long? = 0L,
    val atenas_riesgo: String? = "",

    val psqi_completado: Boolean? = false,
    val psqi_puntaje: Long? = 0L,
    val psqi_riesgo: String? = "",

    val epworth_completado: Boolean? = false,
    val epworth_puntaje: Long? = 0L,
    val epworth_riesgo: String? = "",

    val mctq_completado: Boolean? = false,
    val mctq_puntaje: Long? = 0L,
    val mctq_riesgo: String? = "",
    val mctq_cronotipo: String? = "",

    var ultimaFecha: String? = ""
)