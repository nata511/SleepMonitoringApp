package com.example.sleepmonitoring.BaseDatos.SQLITE

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index
@Entity(
    tableName = "HDZC",
    foreignKeys = [
        ForeignKey(
            entity = DatoNoche::class,
            parentColumns = ["id_datos_noche"],
            childColumns = ["id_dato_noche"],
            onDelete = ForeignKey.CASCADE
        )

    ],
    indices = [Index(value = ["id_dato_noche"])]
)
data class HDZC(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val id_dato_noche: Int,
    val tiempo: Long,
    val angulo: Float
)
