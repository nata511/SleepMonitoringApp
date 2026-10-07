        package com.example.sleepmonitoring.Visual.interfaces.ViesModelyDatas

        object Preguntas {
            val preguntasDiarias = mutableMapOf<String, MutableList<String>>(
                "¿Cómo calificaría en conjunto la calidad de su sueño?" to mutableListOf(
                    "Satisfactoria",
                    "Ligeramente insatisfactoria",
                    "Marcadamente insatisfactoria",
                    "Muy insatisfactoria o no durmió en absoluto"
                ),

                "¿Cómo se sintió durante el día?" to mutableListOf(
                    "Normal",
                    "Ligeramente disminuida",
                    "Marcadamente disminuida",
                    "Muy disminuida"
                ),

                "¿Cómo fue su funcionamiento físico y mental durante el día?" to mutableListOf(
                    "Normal",
                    "Ligeramente disminuido",
                    "Marcadamente disminuido",
                    "Muy disminuido"
                ),

                "¿Qué tan somnoliento se sintió durante el día?" to mutableListOf(
                    "Ninguna",
                    "Leve",
                    "Considerable",
                    "Intensa"
                ),
                " Durante el último mes, ¿cuántas veces habrá tomado medicinas (por su cuenta o recetadas por el médico) para dormir?" to mutableListOf(
                    "Ninguna vez en el último mes ",
                    "Menos de una vez a la semana",
                    "Una o dos veces a la semana",
                    "Tres o más veces a la semana"
                )
            )
            val preguntasIniciales = mutableListOf<String>(
                "¿A qué hora se levanta generalmente?",
                "¿A qué hora se acuesta generalmente?",
                "¿Qué días de la semana son laborales para usted?"
            )

            val diasSemana = mutableListOf<String>(
                "Lunes",
                "Martes",
                "Miércoles",
                "Jueves",
                "Viernes",
                "Sábado",
                "Domingo"
            )

            val preguntasEpworth = mutableMapOf<String, MutableList<String>>(
                "¿Qué tan probable es que te quedes dormido si estás sentado leyendo?" to mutableListOf(
                    "Nunca se queda dormido / Ninguna probabilidad",
                    "Escasa probabilidad de quedarse dormido",
                    "Moderada probabilidad de quedarse dormido",
                    "Alta probabilidad de quedarse dormido"
                ),

                "¿Qué tan probable es que te quedes dormido viendo televisión?" to mutableListOf(
                    "Nunca se queda dormido / Ninguna probabilidad",
                    "Escasa probabilidad de quedarse dormido",
                    "Moderada probabilidad de quedarse dormido",
                    "Alta probabilidad de quedarse dormido"
                ),

                "¿Qué tan probable es que te quedes dormido al estar sentado e inactivo en un lugar público (como un teatro, cine o una conferencia)?" to mutableListOf(
                    "Nunca se queda dormido / Ninguna probabilidad",
                    "Escasa probabilidad de quedarse dormido",
                    "Moderada probabilidad de quedarse dormido",
                    "Alta probabilidad de quedarse dormido"
                ),

                "¿Qué tan probable es que te quedes dormido como pasajero en un carro o bus en un viaje de una hora seguida y sin paradas?" to mutableListOf(
                    "Nunca se queda dormido / Ninguna probabilidad",
                    "Escasa probabilidad de quedarse dormido",
                    "Moderada probabilidad de quedarse dormido",
                    "Alta probabilidad de quedarse dormido"
                ),

                "¿Qué tan probable es que te quedes dormido si te acuestas a descansar por la tarde, cuando las circunstancias lo permiten?" to mutableListOf(
                    "Nunca se queda dormido / Ninguna probabilidad",
                    "Escasa probabilidad de quedarse dormido",
                    "Moderada probabilidad de quedarse dormido",
                    "Alta probabilidad de quedarse dormido"
                ),

                "¿Qué tan probable es que te quedes dormido al estar sentado conversando con alguien?" to mutableListOf(
                    "Nunca se queda dormido / Ninguna probabilidad",
                    "Escasa probabilidad de quedarse dormido",
                    "Moderada probabilidad de quedarse dormido",
                    "Alta probabilidad de quedarse dormido"
                ),

                "¿Qué tan probable es que te quedes dormido al estar sentado tranquilamente después del almuerzo, sin haber tomado alcohol?" to mutableListOf(
                    "Nunca se queda dormido / Ninguna probabilidad",
                    "Escasa probabilidad de quedarse dormido",
                    "Moderada probabilidad de quedarse dormido",
                    "Alta probabilidad de quedarse dormido"
                ),

                "¿Qué tan probable es que te quedes dormido en tu carro al estar detenido por algunos minutos debido al tráfico o un trancón?" to mutableListOf(
                    "Nunca se queda dormido / Ninguna probabilidad",
                    "Escasa probabilidad de quedarse dormido",
                    "Moderada probabilidad de quedarse dormido",
                    "Alta probabilidad de quedarse dormido"
                )
            )





        }
        data class RespuestasEpworth(
            val sentado: String = "",
            val viendo: String = "",
            val inactivo_publico: String = "",
            val copiloto_auto: String = "",
            val acostado: String = "",
            val hablando: String = "",
            val despues_comer: String = "",
            val auto_trafico: String = ""
        )

