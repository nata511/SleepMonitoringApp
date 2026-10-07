package com.example.sleepmonitoring.BasesDatosFirebase

data class DatosMCTQ(
    val promLaboralIni: Double? = 0.0,
    val promLaboralFin: Double? = 0.0,
    val promFindeIni: Double? = 0.0,
    val promFindeFin: Double? = 0.0,
    val duracionLaboral: Double = 0.0,
    val puntoMedio: Double = 0.0,
    val duracionFinde: Double = 0.0,
    val jetLag: Double = 0.0,
    val cronotipo: String="",
    val riesgo:String="",
    val sig:String="",

)
data class TemporalMCTQ(
    val promLaboralIni: Double? = 0.0,
    val promLaboralFin: Double? = 0.0,
    val promFindeIni: Double? = 0.0,
    val promFindeFin: Double? = 0.0,
    val duracionLaboral: Double = 0.0,
    val duracionFinde: Double = 0.0
)