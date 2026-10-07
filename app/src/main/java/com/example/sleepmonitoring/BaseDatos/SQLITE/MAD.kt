package com.example.sleepmonitoring.BaseDatos.SQLITE

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "MAD",
    foreignKeys = [
        ForeignKey(
            entity = DatoNoche::class,
            parentColumns = ["id_datos_noche"],
            childColumns = ["id_noche"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MAD(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val id_noche: Int,
    val tiempo: Long,
    val desviacion: Float
)
