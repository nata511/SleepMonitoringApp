package com.example.sleepmonitoring.Analisis.Test

import com.example.sleepmonitoring.BasesDatosFirebase.DatosSomnolencia
import com.example.sleepmonitoring.Controladores.ControladorTests

class DeteccionSomnolencia(
    val sentado: String = "",
    val viendo: String = "",
    val inactivo_publico: String = "",
    val copiloto_auto: String = "",
    val acostado: String = "",
    val hablando: String = "",
    val despues_comer: String = "",
    val auto_trafico: String = "",
) {
    var puntajeTotal: Int = 0
    var p_sentado: Int = 0
    var p_viendo: Int = 0
    var p_inactivo_publico: Int = 0
    var p_copiloto_auto: Int = 0
    var p_acostado: Int = 0
    var p_hablando: Int = 0
    var p_despues_comer: Int = 0
    var p_auto_trafico: Int = 0
    var riesgo: String = ""

    fun CoordinadorVarriables(){
        CalcularSentado()
        CalcularViendo()
        Calcular_Inactivo()
        CalcularCopilto()
        CalcularAcostado()
         CalcularHablando()
        CalcularComer()
        CalcularTrafico()
        CalcularRiesgo()
        AlmacenarDatos()
    }
    fun CalcularSentado(){
        if (sentado=="Nunca me he dormido "){
            p_sentado=0
        }else if (sentado=="Escasa posibilidad de cabecear"){
            p_sentado=1
        }else if(sentado=="Moderada posibilidad de cabecear"){
            p_sentado=2
        }else{
            p_sentado=3
        }

    }
    fun CalcularViendo(){
        if (viendo=="Nunca me he dormido "){
            p_viendo=0
        }else if (viendo=="Escasa posibilidad de cabecear"){
            p_viendo=1
        }else if(viendo=="Moderada posibilidad de cabecear"){
            p_viendo=2
        }else{
            p_viendo=3
        }
    }
    fun Calcular_Inactivo(){
        if (inactivo_publico=="Nunca me he dormido "){
            p_inactivo_publico=0
        }else if (inactivo_publico=="Escasa posibilidad de cabecear"){
            p_inactivo_publico=1
        }else if(inactivo_publico=="Moderada posibilidad de cabecear"){
            p_inactivo_publico=2
        }else{
            p_inactivo_publico=3
        }

    }
    fun CalcularCopilto(){
        if (copiloto_auto=="Nunca me he dormido "){
            p_copiloto_auto=0
        }else if (copiloto_auto=="Escasa posibilidad de cabecear"){
            p_copiloto_auto=1
        }else if(copiloto_auto=="Moderada posibilidad de cabecear"){
            p_copiloto_auto=2
        }else{
            p_copiloto_auto=3
        }
    }
    fun CalcularAcostado(){
        if (acostado=="Nunca me he dormido "){
            p_acostado=0
        }else if (acostado=="Escasa posibilidad de cabecear"){
            p_acostado=1
        }else if(acostado=="Moderada posibilidad de cabecear"){
            p_acostado=2
        }else{
            p_acostado=3
        }
    }
    fun CalcularHablando(){
        if (hablando=="Nunca me he dormido "){
            p_hablando=0
        }else if (hablando=="Escasa posibilidad de cabecear"){
            p_hablando=1
        }else if(hablando=="Moderada posibilidad de cabecear"){
            p_hablando=2
        }else{
            p_hablando=3
        }
    }
    fun CalcularComer(){
        if (despues_comer=="Nunca me he dormido "){
            p_despues_comer=0
        }else if (despues_comer=="Escasa posibilidad de cabecear"){
            p_despues_comer=1
        }else if(despues_comer=="Moderada posibilidad de cabecear"){
            p_despues_comer=2
        }else{
            p_despues_comer=3
        }
    }
    fun CalcularTrafico(){
        if (auto_trafico=="Nunca me he dormido "){
            p_auto_trafico=0
        }else if (auto_trafico=="Escasa posibilidad de cabecear"){
            p_auto_trafico=1
        }else if(auto_trafico=="Moderada posibilidad de cabecear"){
            p_auto_trafico=2
        }else{
            p_auto_trafico=3
        }
    }

    fun CalcularRiesgo(){
        puntajeTotal = p_sentado+p_viendo+p_inactivo_publico+p_copiloto_auto+p_acostado+p_hablando+p_despues_comer+p_auto_trafico
        if(puntajeTotal<=9){
            riesgo="Normal"
        }else if(puntajeTotal<=14){
            riesgo="Somnolecia Diurna Leve"
        }else if(puntajeTotal<=18){
            riesgo="Somnolecia Diurna Moderada"
        }else if(puntajeTotal>18){
            riesgo="Somnolecia Diurna Grave"
        }
    }
    fun AlmacenarDatos(){
        ControladorTests.AlmacenarSomnolencia(
            DatosSomnolencia(
                sentado,viendo,inactivo_publico,copiloto_auto,acostado,hablando,despues_comer,auto_trafico,puntajeTotal
                ,p_sentado,p_viendo,p_inactivo_publico,p_copiloto_auto,p_acostado,p_hablando,p_despues_comer,p_auto_trafico,riesgo
            )
        )
    }
}