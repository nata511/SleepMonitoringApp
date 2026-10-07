package com.example.sleepmonitoring.BasesDatosFirebase

data class DatosCuestionario(
    var calidad: String="",
    var Somnolencia:String="",
    var malestar :String="",
    var rendimiento:String="",
    var medi:String=""
)
data class preguntasSomnolencia(
    var sentado: String = "",
    var viendo: String = "",
    var inactivo_publico: String = "",
    var copiloto_auto: String = "",
    var acostado: String = "",
    var hablando: String = "",
    var despues_comer: String = "",
    var auto_trafico: String = "",
)
