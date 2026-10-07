package com.example.sleepmonitoring.BaseDatos.SQLITE
import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface Consultas {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun InsertarDatoNoche( datoNoche: DatoNoche )
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun InsertarHoras( horas: Horas )
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun InsertarMAD( mad: List<MAD> )
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun InsertarHDZC(hdzc: List<HDZC>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun UpdateDatoNoche( datoNoche: DatoNoche )
    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun UpdateHoras( horas: Horas )
    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun UpdateMAD( mad: MAD )
    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun UpdateHDZC(hdzc: HDZC )

    @Delete
    suspend fun DeletDatoNoche( datoNoche: DatoNoche )
    @Delete
    suspend fun DeletMAD( mad: MAD )
    @Delete
    suspend fun DeletHDZC(hdzc: HDZC )

    @Query("""
    SELECT * FROM MAD
    WHERE id_noche = (
        SELECT id_datos_noche
        FROM DatosNoches
        WHERE fecha = :fecha
        ORDER BY id_datos_noche DESC
        LIMIT 1
    )
    ORDER BY tiempo
""")
    suspend fun obtener_datos_movimiento(fecha: String): List<MAD>
    @Query("""
    UPDATE DatosNoches
    SET fecha = :fechaNueva
    WHERE id_datos_noche = (
        SELECT id_datos_noche
        FROM DatosNoches
        WHERE fecha = :fechaVieja
        ORDER BY id_datos_noche DESC
        LIMIT 1
    )
""")
    suspend fun actualizarFecha(
        fechaVieja: String,
        fechaNueva: String
    )
    @Query("SELECT id_datos_noche FROM DatosNoches WHERE fecha = :fecha ORDER BY id_datos_noche DESC LIMIT 1")
    suspend fun obtener_indice_noche(fecha: String): Int?
    @Query("SELECT * FROM Horas")
    suspend fun obtenerHoras(): List<Horas>
    @Query("SELECT COUNT(*) FROM HDZC")
    suspend fun cantidadHDZC(): Int

    @Query("""
    SELECT * FROM HDZC
    WHERE id_dato_noche = (
        SELECT id_datos_noche
        FROM DatosNoches
        WHERE fecha = :fecha
        ORDER BY id_datos_noche DESC
        LIMIT 1
    )
    ORDER BY tiempo
""")
    suspend fun obtener_datos_angulos(fecha: String): List<HDZC>

}