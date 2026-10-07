package com.example.sleepmonitoring.Controladores
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.roundToLong
import kotlin.math.sin
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

object ControladorHora {
    var hora_acostar: Long = 0L
    var hora_levantar: Long = 0L

    fun DiaSemana(millis: Long = System.currentTimeMillis()): Int {
        val base = if (millis > 0L) millis else System.currentTimeMillis()
        return Instant.ofEpochMilli(base)
            .atZone(ZoneId.systemDefault())
            .dayOfWeek.value
    }
    fun TrasformarHora(tim: Long): LocalTime {
        var horaEspecifica = Instant.ofEpochMilli(tim).atZone(ZoneId.systemDefault())
            .toLocalTime()
        var hora_texto = horaEspecifica.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        return horaEspecifica
    }
    fun TrasformarHora(tim: Double): LocalTime {
        val horaEspecifica = Instant.ofEpochMilli(tim.toLong())
            .atZone(ZoneId.systemDefault())
            .toLocalTime()
        val horaTexto = horaEspecifica.format(DateTimeFormatter.ofPattern("HH:mm:ss"))

        return horaEspecifica
    }
    fun ObtenerFecha(): LocalDate{
        var fecha= LocalDate.now()
        return fecha
    }
    fun Long_Texto(tim: Long?): String {
        if (tim == null) return "Sin datos"
        val total = tim.coerceAtLeast(0L)        // nunca "0h -5m"
        return "${total / 60}h ${total % 60}m"
    }

    fun Double_Texto(tim: Double): String {
        if (tim == 0.0) return "0"
        val total = tim.roundToLong().coerceAtLeast(0L)   // minutos enteros
        return "${total / 60}h ${total % 60}m"
    }

        fun HoraTexto(hora: LocalTime):String{
            var hora_texto = hora.format(DateTimeFormatter.ofPattern("HH:mm"))
            return hora_texto
        }
    fun HoraTexto(hora: LocalDate): String {
        return hora.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))   // dd, no DD
    }
    fun crearHora(hora:Int, min:Int): LocalTime{
        var horaNueva: LocalTime = LocalTime.of(hora, min)
        return horaNueva
    }

    fun promDuracion(tiempos: MutableList<Long?>):Double{
        var prom = tiempos.filterNotNull().average().takeIf { !it.isNaN() } ?: 0.0
        return prom
    }
    fun promDuracionsigno(tiempos: MutableList<Long>?):Double{
        if (tiempos!=null) {
            var prom = tiempos.filterNotNull().average().takeIf { !it.isNaN() } ?: 0.0
            return prom
        }
        return 0.0
    }
    fun promTiempos(tiempos: List<Long?>): Double? {
        val validos = tiempos.filterNotNull()
        if (validos.isEmpty()) return null

        val zona = ZoneId.systemDefault()
        val nsPorDia = 86_400_000_000_000.0
        var x = 0.0
        var y = 0.0
        for (t in validos) {
            val fraccion = Instant.ofEpochMilli(t).atZone(zona).toLocalTime().toNanoOfDay() / nsPorDia
            val angulo = fraccion * 2 * Math.PI
            x += cos(angulo)
            y += sin(angulo)
        }
        x /= validos.size
        y /= validos.size

        if (hypot(x, y) < 1e-9) return null          // horas opuestas: el promedio no existe

        var angulo = atan2(y, x)
        if (angulo < 0) angulo += 2 * Math.PI
        return (angulo / (2 * Math.PI)) * 24
    }
    fun esAlfanumericoMixto(palabra: String): Boolean {
        val regex = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]+$".toRegex()
        return palabra.matches(regex)
    }


}