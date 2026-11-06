package com.example.sleepmonitoring.BasesDatosFirebase

data class DatosResultados(
    var nombreTest: String="",
    var DescripcionTest:String="",
    var puntaje: String="",
    var riesgo :String="",
    var clasifiaciones: MutableList<String> = mutableListOf(),
    var valoresClasi: MutableList<Float> = mutableListOf(),
    var mutableList: MutableMap<String, Long>?=mutableMapOf()
    )

data class textosTest(
    val nombres: MutableList<String> = mutableListOf(
        "Índice de Calidad del Sueño de Pittsburgh",
        "Escala de Insomnio de Atenas",
        "Escala de Somnolencia de Epworth",
        "Cuestionario de Cronotipo de Múnich (MCTQ)"
    ),
    val descripciones: MutableList<String> = mutableListOf(
        "Evalúa el nivel de riesgo de sufrir trastornos del sueño midiendo la calidad general y las interrupciones del descanso nocturno.",
        "Evalúa el nivel de riesgo y la gravedad del insomnio, identificando la dificultad para conciliar o mantener el sueño.",
        "Evalúa el nivel de riesgo de padecer somnolencia diurna excesiva midiendo la probabilidad de quedarse dormido durante el día.",
        "Evalúa el nivel de desalineación del reloj biológico analizando los horarios de sueño para identificar el perfil de descanso."
    ),
    val listaDeRangos: MutableList<MutableList<String>> = mutableListOf(
        mutableListOf("0-5 Bien", "6-10 Regular", "11-21 Malo"),          // Pittsburgh
        mutableListOf("0-5 Bien", "6-11 Regular", "12-24 Malo"),          // Atenas
        mutableListOf("0-10 Bien", "11-15 Regular", "16-24 Malo"),        // Epworth
        mutableListOf("0h-1h Bien", "1h-2h Regular", "Más de 2h Malo")    // MCTQ
    ),
    val tamañosRangos: MutableList<MutableList<Float>> = mutableListOf(
        mutableListOf(0.238f, 0.238f, 0.524f),
        mutableListOf(0.208f, 0.250f, 0.542f),
        mutableListOf(0.416f, 0.208f, 0.376f),
        mutableListOf(0.250f, 0.250f, 0.500f)
    )
)

data class tempo(
    val descripciones: MutableList<String> = mutableListOf(),
    val resultados: MutableList<String> = mutableListOf(),
    val puntajes:MutableList<String> = mutableListOf()

)
data class Dimesion(

    val descripcionesSomnolencia: MutableList<String> = mutableListOf(
        "Somnolencia al estar sentado",
        "Somnolencia viendo televisión",
        "Somnolencia en lugares públicos",
        "Somnolencia como copiloto",
        "Somnolencia estando acostado",
        "Somnolencia durante conversaciones",
        "Somnolencia después de comer",
        "Somnolencia al conducir"
    ),


    val descripcionesAtenea: MutableList<String> = mutableListOf(
        "Dificultad para conciliar",
        "Frecuencia de despertares",
        "Despertar demasiado temprano",
        "Duración del sueño",
        "Calidad del sueño",
        "Malestar por insomnio",
        "Rendimiento durante el día",
        "Somnolencia durante el día"
    ),

    val descripcionesMCTQ: MutableList<String> = mutableListOf(
        "Inicio promedio laboral",
        "Fin promedio laboral",
        "Inicio promedio fin de semana",
        "Fin promedio fin de semana",
        "Duración del sueño laboral",
        "Punto medio del sueño",
        "Duración del sueño fin de semana",
        "Diferencia entre horarios",
        "Tipo de cronotipo"
    ),

    val descripcionesPSQI: MutableList<String> = mutableListOf(
        "Tiempo para conciliar sueño",
        "Duración del sueño",
        "Calidad subjetiva del sueño",
        "Eficiencia del sueño",
        "Alteraciones durante el sueño",
        "Somnolencia durante el día",
        "Uso de medicamentos para dormir"
    ),
    val resultados: MutableList<String> = mutableListOf(),
    val puntajes:MutableList<String> = mutableListOf()
)



