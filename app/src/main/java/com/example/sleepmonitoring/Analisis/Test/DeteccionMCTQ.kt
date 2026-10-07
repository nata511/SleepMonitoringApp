package com.example.sleepmonitoring.Analisis.Test

import com.example.sleepmonitoring.BasesDatosFirebase.DatosMCTQ
import com.example.sleepmonitoring.Controladores.ControladorHora
import com.example.sleepmonitoring.Controladores.ControladorTests
import kotlin.math.abs
import kotlin.time.Duration.Companion.nanoseconds

class DeteccionMCTQ(
    var prom_duracion_fines:Double,
    var prom_duracion_semana: Double,
    val promLaboralIni: Double ?,
    val promLaboralFin: Double?,
    val promFindeIni: Double?,
    val promFindeFin: Double?

) {

    var riesgo_medio=""
    var sig_medio=""
    var riesgo:String=""
    var sig =""
    var cronotipo=""
    var puntoMedioSemana: Double=0.0
    var jetLag: Double=0.0
    var horaPuntoMedio:Double=0.0
    fun CoordinadorVariables(){
        puntoMedioFines()
        horaMayor()
        CalcularJetLag()
        EvaluarRiesgo()
        riesgoPuntoMedio()
        obtenerCronotipo()
        AlmacenarDatos()
    }
    fun puntoMedioFines(){
        if (promFindeIni!=null && promLaboralIni!=null){
            var division = (prom_duracion_fines/2).toDouble()
            horaPuntoMedio= promFindeIni + division
            division = (prom_duracion_semana/2)
            puntoMedioSemana= promLaboralIni+division
        }
        }

    fun horaMayor(){
        if (prom_duracion_fines>prom_duracion_fines){
            var valor = (prom_duracion_fines - prom_duracion_semana)/2
            horaPuntoMedio= (horaPuntoMedio-valor)
        }
    }
    fun obtenerCronotipo(){
        val hora_alondraE = ControladorHora.crearHora(2, 10)
        val hora_alondra = ControladorHora.crearHora(3, 0)
        val hora_ModeradoT = ControladorHora.crearHora(3, 59)
        val hora_Neutro = ControladorHora.crearHora(4, 59)
        val hora_ModeradoTardio = ControladorHora.crearHora(5, 59)
        val hora_buho = ControladorHora.crearHora(7, 24)
        var hora= ControladorHora.TrasformarHora(horaPuntoMedio)
        if (hora.isBefore(hora_alondraE) || hora == hora_alondraE) {
            cronotipo = "Extremadamente Temprano"

        } else if (hora.isBefore(hora_alondra)) {
            cronotipo = "Muy Temprano"

        } else if (hora.isBefore(hora_ModeradoT)) {
            cronotipo = "Moderadamente Temprano"

        } else if (hora.isBefore(hora_Neutro)) {
            cronotipo = "Intermedio / Neutro"

        } else if (hora.isBefore(hora_ModeradoTardio)) {
            cronotipo = "Moderadamente Tardío"

        } else if (hora.isBefore(hora_buho)) {
            cronotipo = "Muy Tardío"

        } else {
            cronotipo = "Extremadamente Tardío"
        }

    }
    fun CalcularJetLag(){
        jetLag= abs(horaPuntoMedio-puntoMedioSemana)
    }
    fun EvaluarRiesgo(){
        var horas = jetLag.nanoseconds.inWholeHours
        if (horas < 1) {
            riesgo_medio = "Bajo"
            sig_medio = "Desajuste leve. Tu cuerpo puede tolerar esta diferencia sin grandes consecuencias biológicas."

        } else if (horas < 2) {
            riesgo_medio = "Moderado"
            sig_medio = "Existe un desajuste circadiano moderado que puede relacionarse con mayor fatiga y aturdimiento al despertar."

        } else {
            riesgo_medio =     "Alto"
            sig_medio = "Tu cuerpo presenta un desajuste circadiano importante, asociado con una mayor alteración de los ritmos de sueño y vigilia."
        }
    }
    fun riesgoPuntoMedio(){
        var hora= ControladorHora.TrasformarHora(horaPuntoMedio)
        var hora_nueva1= ControladorHora.crearHora(6,0)
        var hora_nueva2= ControladorHora.crearHora(2,15)

        if (hora.isAfter(hora_nueva1) && riesgo_medio=="Bajo" ){
            riesgo="Bajo"
            sig="Tu cuerpo se acuesta y se levanta temprano de forma constante los 7 días de la semana."
        }else if(hora.isBefore(hora_nueva2) && riesgo_medio=="Bajo"){
            riesgo="Bajo"
            sig="Tu cuerpo se acuesta y se levanta temprano de forma constante los 7 días de la semana."
        }else if (hora.isBefore(hora_nueva2) && (riesgo_medio=="Alto" || riesgo_medio=="Moderado")){

            riesgo="Trastorno de la Fase de Sueño Avanzada"
            sig=" Indica que los ritmos circadianos de temperatura corporal y melatonina están adelantados de manera rígida. La persona siente una somnolencia médica abrumadora al caer la tarde (ej. 19:00 u 20:00) y se despierta espontáneamente en la madrugada (ej. 02:00 o 03:00) sin posibilidad de volver a dormirse"
        }else{
            riesgo="Trastorno de la Fase de Sueño Retrasada"
            sig="El individuo es incapaz de conciliar el sueño a horarios convencionales (ej. antes de la medianoche), desplazando su ventana natural de sueño hacia la madrugada"

        }
    }
    fun AlmacenarDatos(){
        ControladorTests.AlmacenarMCTQ(
            DatosMCTQ(promLaboralIni,promLaboralFin,promFindeIni,promFindeFin,
                prom_duracion_semana, horaPuntoMedio,prom_duracion_fines
            ,jetLag,cronotipo,riesgo,sig)
        )
    }
}