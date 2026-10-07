package com.example.sleepmonitoring.Analisis.Test

import com.example.sleepmonitoring.BasesDatosFirebase.DatosAtenea
import com.example.sleepmonitoring.BasesDatosFirebase.DatosPSQI
import com.example.sleepmonitoring.Controladores.ControladorTests
import kotlin.time.Duration.Companion.nanoseconds

class DeteccionPSQI(
    val medi:String="",
    val Duracion:Long,
    val Eficiencia:Float,
    val despertares:Int,
    val calidad: String,
    val Somnolencia:String,
    var latencia: Long

) {
    var p_Latencia: Int=0
    var p_Duracion: Int=0
    var p_Calidad: Int=0
    var p_Eficiencia: Int=0
    var p_Despertares: Int=0
    var p_Somnolencia: Int=0
    var p_Medicamentos: Int=0
    var p_total:Int=0
    var p_riesgo: String=""
    var sig_total: String=""

    var  minutos = latencia


    fun CoordinadorVariables(){
        puntaje_latencia()
        puntaje_Duracion()
        puntaje_despertares()
        puntaje_somnolencia()
        medicamento()
        puntaje_calidad()
        punataje_eficiencia()
        p_total_riesgo()

    }
    fun puntaje_latencia(){
        if(minutos<=15 && minutos>=0){
             p_Latencia = 0
        }
        else if(latencia<=30){
             p_Latencia = 1
        }
        else if(latencia<=60){
             p_Latencia = 2
        }
        else if(latencia>60){
             p_Latencia = 3
        }
        else{
             p_Latencia= 0
        }
    }
    fun puntaje_Duracion() {
        p_Duracion = when {
            Duracion < 300L -> 3
            Duracion <= 360L -> 2
            Duracion <= 420L -> 1
            else -> 0
        }
    }
    fun puntaje_despertares(){
        when {
            despertares==0 -> p_Despertares = 0
            despertares==1 -> p_Despertares = 1
            despertares==2 -> p_Despertares = 2
            despertares>=3 -> p_Despertares = 3
            else -> {p_Despertares=0}
        }
    }
    fun puntaje_somnolencia(){
        when (Somnolencia) {
            "Ninguna vez en el ultimo mes" -> p_Somnolencia= 0
            "Menos de una vez en la semana" -> p_Somnolencia= 1
            "Una o dos veces a la semana" -> p_Somnolencia= 2
            "Tres o mas veces a la semana" -> p_Somnolencia= 3
            else -> p_Somnolencia = 0
        }
    }
    fun medicamento(){
        when (medi) {
            "Ninguna vez en el ultimo mes" -> p_Medicamentos= 0
            "Menos de una vez en la semana" -> p_Medicamentos= 1
            "Una o dos veces a la semana" -> p_Medicamentos= 2
            "Tres o mas veces a la semana" -> p_Medicamentos= 3
            else -> p_Medicamentos = 0
        }

    }
    fun puntaje_calidad(){
        when (calidad) {
            "Muy buena" -> p_Calidad= 0
            "Buena" -> p_Calidad= 1
            "Mala" -> p_Calidad= 2
            "Muy mala" -> p_Calidad= 3
            else -> p_Calidad= 0
        }

    }
    fun punataje_eficiencia(){
        if(Eficiencia >= 85f){
            p_Eficiencia = 0
        }else if(Eficiencia >= 75f){
            p_Eficiencia = 1
        }else if(Eficiencia >= 65f){
            p_Eficiencia = 2
        }else{
            p_Eficiencia = 3
        }
    }
    fun p_total_riesgo() {
        p_total = p_Latencia + p_Duracion + p_Calidad + p_Eficiencia + p_Despertares + p_Somnolencia + p_Medicamentos

        if (p_total<5){
            p_riesgo= "Bajo"
            sig_total ="Buena"
        }else if(p_total<11){
            p_riesgo= "Moderado"
            sig_total ="Regulas"
        }else if(p_total<=21){
            p_riesgo= "Alto"
            sig_total ="Mala"
        }

    }
    fun AlmacenarDatos(datosAtenea: DatosAtenea){
        ControladorTests.AlmacenarPSQI(
            DatosPSQI(medi, p_Latencia ,p_Duracion,p_Calidad,p_Eficiencia
            ,p_Despertares,p_Somnolencia,p_Medicamentos,p_total,calidad,Somnolencia,sig_total,p_riesgo ),
            datosAtenea
        )
    }


}