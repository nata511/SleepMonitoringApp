package com.example.sleepmonitoring.Visual.interfaces.ViesModelyDatas

data class DatosPrincipal(
    var duracion :Long?=0,
    var eficiencia:Float?=0.0F,
    var racha :Int?=0,
    var mutableList: MutableMap<String, Long>?=mutableMapOf(),
    var riesgo:String?="Sin datos",
    var fraseRiesgo : String?="Sin datos",
    var promDura :Double=0.0
)
