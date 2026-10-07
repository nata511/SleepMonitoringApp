package com.example.sleepmonitoring.Visual.interfaces.ViesModelyDatas


data class valoresCuestionario(
    var pregunta:String="",
    var descripcion:String="",
    var labeli: String="",
    var valor:Int=0
)


data class preguntasInciales(
    val dias: List<String> = listOf(
        "Lunes",
        "Martes",
        "Miércoles",
        "Jueves",
        "Viernes",
        "Sábado",
        "Domingo"
    ),

    val preguntas: MutableList<String> = mutableListOf(
        "¿Cómo quieres que te llamemos?",
        "¿Cuál es tu fecha de nacimiento?",
        "¿A qué hora te acuestas normalmente?",
        "¿A qué hora te despiertas normalmente?",
        "¿Qué días descansas?"
    ),
    val error: String?= null,
    val descripciones: MutableList<String> = mutableListOf(
        "Tu nombre o apodo.",
        "Ten encuenta que los test manejados son los mas presentes en estudiantes universitarios no obstante son aplicables para cualquier persona",
        "El sensor se activa solo a esta hora, salvo que lo inicies antes con el botón del reloj.",
        "El sensor se desactiva solo a esta hora, salvo que lo detengas antes con el botón del reloj.",
        "Marca los días que no trabajas ni estudias. Puedes dejarlo vacío."
    )
    ,val labels: MutableList<String> = mutableListOf(
        "Nombre o apodo",
        "Fecha de nacimiento",
        "Hora de acostarse",
        "Hora de despertar",
        "Días de descanso"
    )
)
