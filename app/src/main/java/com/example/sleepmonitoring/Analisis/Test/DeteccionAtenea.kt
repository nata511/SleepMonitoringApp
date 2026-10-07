package com.example.sleepmonitoring.Analisis.Test

import com.example.sleepmonitoring.BasesDatosFirebase.DatosAtenea
import com.example.sleepmonitoring.Controladores.ControladorTests
import kotlin.time.Duration.Companion.nanoseconds

class DeteccionAtenea (
    val despertares:Int,
    val finSueño: Long,
    val duracion:Long,
    val calidad:String,
    val malestar:String,
    val rendimiento: String,
    val somnolencia: String,
    var latencia: Long


){
    var p_despertares: Int=0
    var p_finSueño: Int=0
    var p_duracion: Int=0
    var p_calidad: Int=0
    var p_malestar: Int=0
    var p_rendimiento: Int=0
    var p_somnolencia: Int=0
    var p_latencia: Int=0
    var riesgo: String=""
    var sig_total=""
    var valor: Long=0L
    var  minutos = latencia


    var p_total:Int =0

    fun CoordinadorDimesiones(): DatosAtenea{
        puntaje_despertares()
        puntaje_finSueño()
        puntaje_duracion()
        puntaje_calidad()
        puntaje_malestar()
        puntaje_rendimiento()
        puntaje_somnolencia()
        puntaje_latencia()
        p_total_riesgo()
       val valor= AlmacenarDatos()
        return valor
    }



    fun puntaje_despertares() {
        when {
            despertares==0 -> p_despertares = 0
            despertares==1 -> p_despertares = 1
            despertares==2 -> p_despertares = 2
            despertares>=3 -> p_despertares = 3
            else -> {p_despertares=0}
        }
    }

    fun puntaje_finSueño() {
        if (duracion < 480L) {
            val diferencia = 480L - duracion

            if (diferencia <= 60L) {
                p_finSueño = 1
            } else if (diferencia <= 120L) {
                p_finSueño = 2
            } else {
                p_finSueño = 3
            }
        } else {
            p_finSueño = 0
        }
    }

    fun puntaje_duracion() {
        p_duracion = when {
            duracion < 300L -> 3
            duracion <= 360L -> 2
            duracion <= 420L -> 1
            else -> 0
        }
    }

    fun puntaje_calidad() {
        when (calidad) {
            "Muy buena" -> p_calidad= 0
            "Buena" -> p_calidad= 1
            "Mala" -> p_calidad= 2
            "Muy mala" -> p_calidad= 3
            else -> p_calidad= 0
        }
    }
    fun puntaje_malestar() {
        when (malestar) {
            "Normal: Me siento bien, con energía y con un estado de ánimo estable." -> p_malestar = 0
            "Levemente disminuido: Me siento un poco desanimado, apático o con ligera pesadez." -> p_malestar = 1
            "Marcadamente disminuido: Me siento de mal humor, irritable o notablemente decaído." -> p_malestar = 2
            "Muy disminuido o nulo: Me siento sumamente agotado, miserable o incapaz de sentirme bien." -> p_malestar = 3
            else -> p_malestar = 0
        }
    }

    fun puntaje_rendimiento() {
        when (rendimiento) {
            "Normal: Mi rendimiento y concentración son óptimos, como cualquier día normal." -> p_rendimiento = 0
            "Levemente disminuida: Me cuesta un poco arrancar o concentrarme, pero cumplo mis tareas." -> p_rendimiento = 1
            "Marcadamente disminuida: Cometo errores, olvido cosas o mi productividad bajó notoriamente." -> p_rendimiento = 2
            "Muy disminuida o nula: Estoy totalmente incapacitado para trabajar, estudiar o concentrarme." -> p_rendimiento = 3
            else -> p_rendimiento = 0
        }

    }

    fun puntaje_somnolencia() {
        when (somnolencia) {
            "No tengo sueño durante el día  " -> p_somnolencia= 0
            "Siento un poco de sueño en momentos de inactividad" -> p_somnolencia= 1
            "Siento un sueño fuerte en actividades cotidianas y tengo que luchar por no dormirme." -> p_somnolencia= 2
            "Me quedo dormido involuntariamente en situaciones importantes" -> p_somnolencia= 3
            else -> p_somnolencia = 0
        }
    }

    fun puntaje_latencia(){
        p_latencia= when {
            latencia <= 15L -> 0
            latencia <= 30L -> 1
            latencia <= 60L -> 2
            else -> 3
        }
    }

    fun p_total_riesgo() {
        p_total = p_latencia + p_duracion + p_calidad +p_despertares + p_somnolencia +p_malestar+p_finSueño+p_rendimiento

        if (p_total<3){
            riesgo= "Bajo"
            sig_total ="Normal"
        }else if(p_total<5){
            riesgo= "Moderado"
            sig_total ="Regular"
        }else if(p_total>6){
            riesgo= "Alto"
            sig_total ="Mala"
        }


    }

    fun AlmacenarDatos(): DatosAtenea{

            return DatosAtenea(valor,malestar,calidad,somnolencia,rendimiento,p_latencia,
                p_calidad, p_DesperTemprano = p_despertares,p_duracion,p_calidad,p_malestar,p_rendimiento,
                p_somnolencia,p_total,riesgo)

    }

}