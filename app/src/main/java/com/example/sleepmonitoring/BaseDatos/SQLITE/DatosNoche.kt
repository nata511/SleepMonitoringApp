package com.example.sleepmonitoring.BaseDatos.SQLITE

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "DatosNoches")
data class DatoNoche(
    @PrimaryKey(autoGenerate = true)
    val id_datos_noche :Int = 0,
    val fecha :String ,
    val estado:String
)
@Entity(tableName = "Horas")
data class Horas(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val horaInicio :String ,
    val horaFin:String
)

