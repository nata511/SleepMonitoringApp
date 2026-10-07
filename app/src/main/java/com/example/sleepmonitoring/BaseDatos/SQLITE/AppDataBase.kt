package com.example.sleepmonitoring.BaseDatos.SQLITE

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import android.util.Log
import androidx.room.RoomDatabase

@Database(entities = [DatoNoche::class, MAD::class, HDZC::class,Horas::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun ConsultasDao(): Consultas

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDataBase(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {

                Log.d(
                    "DATABASE",
                    "Ruta BD: ${
                        context.applicationContext
                            .getDatabasePath("DatosCrudos")
                            .absolutePath
                    }"
                )

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "DatosCrudos"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}
