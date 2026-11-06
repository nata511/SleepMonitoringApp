package com.example.sleepmonitoring.BasesDatosFirebase

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.sleepmonitoring.BaseDatos.SQLITE.Horas
import com.example.sleepmonitoring.Controladores.ControladorHora
import com.example.sleepmonitoring.Visual.interfaces.ViesModelyDatas.DatosPrincipal
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.tasks.await
import java.time.DayOfWeek
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters

private object Col {
    const val PSQI = "TestPSQI"
    const val ATENEA = "TestAtenea"
    const val SOMNOLENCIA = "TestSomnolencia"
    const val MCTQ = "TestMCTQ"
}

object ConsulFirebase {

    val base = Firebase.firestore
    val identificador = Firebase.auth


    val actualizar = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val actualizarPublico = actualizar.asSharedFlow()
    val estadoTest = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val estadoTestPublico = estadoTest.asSharedFlow()
    val puenteResumen = MutableSharedFlow<ResumenUsuarios>(replay = 1, extraBufferCapacity = 1)
    val puenteResumenPublic = puenteResumen.asSharedFlow()

    private val DIAS = listOf("Lun", "Mar", "Mier", "Juev", "Vie", "Sab", "Dom")
    private var registroResumen: ListenerRegistration? = null

    private val cache = mutableMapOf<String, MutableMap<String, Long>>()

    fun invalidarCache() = cache.clear()

    private fun lunesDeEstaSemana(): String = ControladorHora.HoraTexto(LocalDate.now().with(DayOfWeek.MONDAY))

    private suspend fun semanalRango(ref: CollectionReference, campo: String): MutableMap<String, Long> {
        val res = ref
            .whereGreaterThanOrEqualTo(FieldPath.documentId(), lunesDeEstaSemana())
            .whereLessThanOrEqualTo(FieldPath.documentId(), DatosConstantes.fecha)
            .get().await()

        val mapa = mutableMapOf<String, Long>()
        for (doc in res) {
            val idx = try {
                LocalDate.parse(doc.id).dayOfWeek.value - 1
            } catch (e: Exception) {
                null
            }
            if (idx != null) mapa[DIAS[idx]] = doc.getLong(campo) ?: 0L
        }
        return mapa
    }

    private suspend fun semanalCacheado(
        nombre: String, ref: CollectionReference, campo: String, uid: String
    ): MutableMap<String, Long> {
        val clave = "$uid:$nombre:${DatosConstantes.fecha}"
        cache[clave]?.let { return it }
        val datos = semanalRango(ref, campo)
        cache[clave] = datos
        return datos
    }

    suspend fun registrarUsuario(correo: String, contraseña: String): String? {
        return try {
            identificador.createUserWithEmailAndPassword(correo, contraseña).await()
            null
        } catch (e: FirebaseAuthUserCollisionException) {
            "El correo ya está registrado"
        } catch (e: FirebaseAuthWeakPasswordException) {
            "La contraseña es demasiado débil"
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            "El correo no tiene un formato válido"
        } catch (e: Exception) {
            "No se pudo registrar el usuario"
        }
    }

    fun diaUnion(): Long {
        val usuario = identificador.currentUser
        val tiempo = usuario?.metadata?.creationTimestamp ?: 0L
        return ChronoUnit.DAYS.between(java.time.Instant.ofEpochMilli(tiempo), java.time.Instant.now())
    }

    suspend fun ObtenerDatosrUsuario(correo: String, contraseña: String): String? {
        return try {
            identificador.signInWithEmailAndPassword(correo, contraseña).await()
            invalidarCache()
            null
        } catch (e: FirebaseAuthInvalidUserException) {
            "El usuario no existe o la cuenta está deshabilitada"
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            "El correo o la contraseña son incorrectos"
        } catch (e: FirebaseAuthException) {
            when (e.errorCode) {
                "ERROR_TOO_MANY_REQUESTS" -> "Demasiados intentos. Intenta nuevamente más tarde"
                "ERROR_NETWORK_REQUEST_FAILED" -> "No hay conexión a Internet"
                "ERROR_USER_DISABLED" -> "La cuenta está deshabilitada"
                else -> "No se pudo iniciar sesión"
            }
        } catch (e: Exception) {
            "Ocurrió un error inesperado"
        }
    }
    suspend fun InsertarUsuario(datos: Usuario) {
        val actual = identificador.currentUser ?: return
        try {
            base.collection("Usuarios").document(actual.uid).set(datos).await   ()
        } catch (e: Exception) {
            Log.e("FB", "InsertarUsuario", e)
        }
    }
    suspend fun ModificarUsuario(datos: MutableMap<String,Any>){
        val actual = identificador.currentUser ?: return
        try {
            val id = actual.uid
            base.collection("Usuarios").document(id).update(datos).await()
        }catch (e: Exception){
            println(e)
        }
    }

    suspend fun ObtenerUsuario(): Usuario? {
        val actual = identificador.currentUser ?: return null
        return try {
            val doc = base.collection("Usuarios").document(actual.uid).get().await()
            if (doc.exists()) doc.toObject(Usuario::class.java) else null
        } catch (e: Exception) {
            Log.e("FB", "ObtenerUsuario", e)
            null
        }
    }
    suspend fun ModificarTema(tema: String): Boolean {
        val actual = identificador.currentUser ?: return false
        return try {
            base
                .collection("Usuarios")
                .document(actual.uid)
                .update("tema", tema)
                .await()
            println("Se modifico tema")
            true
        } catch (e: Exception) {

            Log.e("FB", "ModificarTema", e)
            false
        }
    }

    suspend fun obtenerListaDias(): MutableList<Int>? {
        val actual = identificador.currentUser ?: return null
        return try {
            val doc = base.collection("Usuarios").document(actual.uid).get().await()
            (doc.get("diasLaborales") as? List<*>)
                ?.mapNotNull { (it as? Number)?.toInt() }
                ?.toMutableList()
                ?: mutableListOf()
        } catch (e: Exception) {
            Log.e("FB", "obtenerListaDias", e)
            null
        }
    }
    suspend fun leerHorasFirestore(): Horas? {
        val uid = identificador.currentUser?.uid ?: return null
        return try {
           val doc= base.collection("Usuarios").document(uid).get().await()
            val ini = doc.getString("horaAcostar") ?: return null
            val fin = doc.getString("horaLevantar") ?: return null
            Horas(horaInicio = ini, horaFin = fin)
        } catch (e: Exception) {
            Log.e("FIRESTORE", "No se pudieron leer los horarios", e)
            null
        }
    }

    suspend fun insertarDatosSueno(datos: DatosSueño, datosResumenUsuarios: ResumenUsuarios) {
        val actual = identificador.currentUser ?: return
        datosResumenUsuarios.ultimaFecha = DatosConstantes.fecha

        val batch = base.batch()
        val referencia1 = base.collection("RegistroSueno").document(actual.uid)
            .collection("Resultados").document(DatosConstantes.fecha)
        val referencia2 = base.collection("ResumenTests").document(actual.uid)

        try {
            batch.set(referencia1, datos)
            batch.set(referencia2, datosResumenUsuarios, SetOptions.merge())
            batch.commit().await()
            invalidarCache()
            actualizar.tryEmit(Unit)      // avisa solo si se guardó
        } catch (e: Exception) {
            Log.e("FB", "insertarDatosSueno", e)
        }
    }

    suspend fun ObtenerDatosSueno(): DatosSueño? {
        val actual = identificador.currentUser ?: return null
        return try {
            val doc = base.collection("RegistroSueno").document(actual.uid)
                .collection("Resultados").document(DatosConstantes.fecha).get().await()
            if (doc.exists()) doc.toObject(DatosSueño::class.java) else null
        } catch (e: Exception) {
            Log.e("FB", "ObtenerDatosSueno", e)
            null
        }
    }


    suspend fun ObtenerRacha(): Int? {
        val actual = identificador.currentUser ?: return null
        val fechaAc = ControladorHora.HoraTexto(LocalDate.now().minusDays(1))
        return try {
            val doc = base.collection("RegistroSueno").document(actual.uid)
                .collection("Resultados").document(fechaAc).get().await()
            if (doc.exists()) doc.getLong("racha")?.toInt() else null
        } catch (e: Exception) {
            Log.e("FB", "ObtenerRacha", e)
            null
        }
    }

    suspend fun ObtenDatosPrincipal(): DatosPrincipal? {
        val actual = identificador.currentUser ?: return null
        return try {
            val doc = base.collection("RegistroSueno").document(actual.uid)
                .collection("Resultados").document(DatosConstantes.fecha).get().await()
            if (doc.exists()) {
                DatosPrincipal(
                    doc.getLong("duracion"),
                    doc.getDouble("eficiencia")?.toFloat(),
                    doc.getLong("racha")?.toInt(),
                    mutableList = null,
                    doc.getString("riesgo"),
                    doc.getString("fraseRiesgo")
                )
            } else {
                DatosPrincipal(mutableList = null)
            }
        } catch (e: Exception) {
            Log.e("FB", "ObtenDatosPrincipal", e)
            null
        }
    }

    suspend fun insertarDatosAtenea(datos: DatosAtenea) {
        DatosConstantes.cuespsqi=true
        val actual = identificador.currentUser ?: return
        val batch = base.batch()
        val referencia1 = base.collection("Tests").document(actual.uid)
            .collection(Col.ATENEA).document(DatosConstantes.fecha)
        val referencia3 = base.collection("ResumenTests").document(actual.uid)
        val mapaDatos = mapOf(
            "atenas_completado" to true,
            "atenas_puntaje" to datos.p_total,
            "atenas_riesgo" to datos.riesgo
        )
        try {
            batch.set(referencia1, datos)
            batch.set(referencia3, mapaDatos, SetOptions.merge())
            batch.commit().await()
            invalidarCache()
            estadoTest.tryEmit(Unit)
        } catch (e: Exception) {
            Log.e("FB", "insertarDatosAtenea", e)
        }
    }

    suspend fun ObtenerDatosAtenea(): DatosAtenea? {
        val actual = identificador.currentUser ?: return null
        return try {
            val doc = base.collection("Tests").document(actual.uid).collection(Col.ATENEA)
                .document(DatosConstantes.fecha).get().await()
            if (doc.exists()) doc.toObject(DatosAtenea::class.java) else null
        } catch (e: Exception) {
            Log.e("FB", "ObtenerDatosAtenea", e)
            null
        }
    }

    suspend fun datosSemanalAtenea(): MutableMap<String, Long>? {
        val actual = identificador.currentUser ?: return null
        return try {
            semanalCacheado(
                "atenea",
                base.collection("Tests").document(actual.uid).collection(Col.ATENEA),
                "p_total", actual.uid
            )
        } catch (e: Exception) {
            Log.e("FB", "datosSemanalAtenea", e)
            null
        }
    }

    suspend fun insertarDatosPSQI(datos: DatosPSQI, datos12: DatosAtenea) {
        val actual = identificador.currentUser ?: return
        val batch = base.batch()
        val tests = base.collection("Tests").document(actual.uid)
        val referencia1 = tests.collection(Col.PSQI).document(DatosConstantes.fecha)
        val referencia2 = tests.collection(Col.ATENEA).document(DatosConstantes.fecha)   // antes "Atenas"
        val referencia3 = base.collection("ResumenTests").document(actual.uid)
        val mapaDatos = mapOf(
            "atenas_completado" to true,
            "atenas_puntaje" to datos12.p_total,
            "atenas_riesgo" to datos12.riesgo,
            "psqi_completado" to true,
            "psqi_puntaje" to datos.p_total,
            "psqi_riesgo" to datos.riesgo
        )
        try {
            batch.set(referencia1, datos)
            batch.set(referencia2, datos12)
            batch.set(referencia3, mapaDatos, SetOptions.merge())
            batch.commit().await()
            invalidarCache()
            estadoTest.tryEmit(Unit)
        } catch (e: Exception) {
            Log.e("FB", "insertarDatosPSQI", e)
        }
    }

    suspend fun ObtenerDatosPSQI(): DatosPSQI? {
        val actual = identificador.currentUser ?: return null
        return try {
            val doc = base.collection("Tests").document(actual.uid).collection(Col.PSQI)
                .document(DatosConstantes.fecha).get().await()
            if (doc.exists()) doc.toObject(DatosPSQI::class.java) else null
        } catch (e: Exception) {
            Log.e("FB", "ObtenerDatosPSQI", e)
            null
        }
    }

    suspend fun datosSemanalPSQI(): MutableMap<String, Long>? {
        val actual = identificador.currentUser ?: return null
        return try {
            semanalCacheado(
                "psqi",
                base.collection("Tests").document(actual.uid).collection(Col.PSQI),
                "p_total", actual.uid
            )
        } catch (e: Exception) {
            Log.e("FB", "datosSemanalPSQI", e)
            null
        }
    }

    suspend fun insertarDatosSomnolencia(datos: DatosSomnolencia) {
        val actual = identificador.currentUser ?: return
        val batch = base.batch()
        val referencia1 = base.collection("Tests").document(actual.uid)
            .collection(Col.SOMNOLENCIA).document(DatosConstantes.fecha)
        val referencia3 = base.collection("ResumenTests").document(actual.uid)
        val mapaDatos = mapOf(
            "epworth_completado" to true,
            "epworth_puntaje" to datos.puntajeTotal,
            "epworth_riesgo" to datos.riesgo
        )
        try {
            batch.set(referencia1, datos)
            batch.set(referencia3, mapaDatos, SetOptions.merge())
            batch.commit().await()
            invalidarCache()
        } catch (e: Exception) {
            Log.e("FB", "insertarDatosSomnolencia", e)
        }
    }

    suspend fun ObtenerDatosSomnolencia(): DatosSomnolencia? {
        val actual = identificador.currentUser ?: return null
        return try {
            val doc = base.collection("Tests").document(actual.uid).collection(Col.SOMNOLENCIA)   // antes "TestSomnolencias"
                .document(DatosConstantes.fecha).get().await()
            // en ObtenerDatosSomnolencia, después de obtener doc
            Log.d("RESULTADOS", "doc=${doc.id} exists=${doc.exists()} data=${doc.data}")
            if (doc.exists()) doc.toObject(DatosSomnolencia::class.java) else null
        } catch (e: Exception) {
            Log.e("FB", "ObtenerDatosSomnolencia", e)
            null
        }
    }


    suspend fun datosSemanalSomnolencia(): MutableMap<String, Long>? {
        val actual = identificador.currentUser ?: return null
        return try {
            semanalCacheado(
                "somno",
                base.collection("Tests").document(actual.uid).collection(Col.SOMNOLENCIA),
                "puntajeTotal", actual.uid
            )
        } catch (e: Exception) {
            Log.e("FB", "datosSemanalSomnolencia", e)
            null
        }
    }

    suspend fun insertarDatosMCQT(datos: DatosMCTQ) {
        val actual = identificador.currentUser ?: return
        val batch = base.batch()
        val referencia1 = base.collection("Tests").document(actual.uid)
            .collection(Col.MCTQ).document(DatosConstantes.fecha)
        val referencia3 = base.collection("ResumenTests").document(actual.uid)
        val mapaDatos = mapOf(
            "mctq_completado" to true,
            "mctq_puntaje" to datos.cronotipo,
            "mctq_riesgo" to datos.riesgo
        )
        try {
            batch.set(referencia1, datos)
            batch.set(referencia3, mapaDatos, SetOptions.merge())
            batch.commit().await()
            invalidarCache()
        } catch (e: Exception) {
            Log.e("FB", "insertarDatosMCQT", e)
        }
    }

    fun fechaDomingo(): String =
        ControladorHora.HoraTexto(
            LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
        )

    suspend fun ObtenerDatosMCTQ(): DatosMCTQ? {
        val actual = identificador.currentUser ?: return null
        return try {
            val doc = base.collection("Tests").document(actual.uid).collection(Col.MCTQ)
                .document(fechaDomingo()).get().await()
            if (doc.exists()) doc.toObject(DatosMCTQ::class.java) else null
        } catch (e: Exception) {
            Log.e("FB", "ObtenerDatosMCTQ", e)
            null
        }
    }



    suspend fun DatosParaMCTQ(lista: MutableList<Int>): TemporalMCTQ? {
        val actual = identificador.currentUser ?: return null
        return try {
            val desde = ControladorHora.HoraTexto(LocalDate.now().minusDays(7))

            val promLaboralIni = mutableListOf<Long?>()
            val promLaboralFin = mutableListOf<Long?>()
            val promFindeIni = mutableListOf<Long?>()
            val promFindeFin = mutableListOf<Long?>()
            val duracionLaboral = mutableListOf<Long?>()
            val duracionFinde = mutableListOf<Long?>()

            val resultado = base.collection("RegistroSueno").document(actual.uid)
                .collection("Resultados")
                .whereGreaterThanOrEqualTo(FieldPath.documentId(), desde)
                .whereLessThanOrEqualTo(FieldPath.documentId(), DatosConstantes.fecha)
                .get().await()

            for (d in resultado) {
                val dia = d.getLong("dia")?.toInt()
                if (dia != null && lista.contains(dia)) {
                    promLaboralIni.add(d.getLong("hora_inicio_R"))
                    promLaboralFin.add(d.getLong("hora_final_R"))
                    duracionLaboral.add(d.getLong("duracion"))
                } else {
                    promFindeIni.add(d.getLong("hora_inicio_R"))
                    promFindeFin.add(d.getLong("hora_final_R"))
                    duracionFinde.add(d.getLong("duracion"))
                }
            }

            TemporalMCTQ(
                ControladorHora.promTiempos(promLaboralIni),
                ControladorHora.promTiempos(promLaboralFin),
                ControladorHora.promTiempos(promFindeIni),
                ControladorHora.promTiempos(promFindeFin),
                ControladorHora.promDuracion(duracionLaboral),
                ControladorHora.promDuracion(duracionFinde)
            )
        } catch (e: Exception) {
            Log.e("FB", "DatosParaMCTQ", e)
            null
        }
    }

    suspend fun DatosGraficas(): MutableMap<String, Long>? {
        val actual = identificador.currentUser ?: return null
        return try {
            semanalCacheado(
                "duracion",
                base.collection("RegistroSueno").document(actual.uid).collection("Resultados"),
                "duracion", actual.uid
            )
        } catch (e: Exception) {
            Log.e("FB", "DatosGraficas", e)
            null
        }
    }
    fun obtenerResumenUsuarios() {
        val actual = identificador.currentUser ?: return
        registroResumen?.remove()
        registroResumen = base.collection("ResumenTests").document(actual.uid)
            .addSnapshotListener { snapshots, error ->
                if (error != null){
                    Log.e("FB", "obtenerResumenUsuarios", error)
                    return@addSnapshotListener
                }
                if (snapshots != null && snapshots.exists()) {
                    snapshots.toObject(ResumenUsuarios::class.java)?.let { puenteResumen.tryEmit(it) }
                }
            }
    }

    fun cerrarSesion() {
        registroResumen?.remove()
        registroResumen = null
        invalidarCache()
        identificador.signOut()
    }
}

object DatosConstantes {
    val fecha: String get() = ControladorHora.HoraTexto(LocalDate.now())
    var horaInicio: Long = 0L
    var horaFin: Long = 0L
    var cuespsqi=false
    var somno=false
    var sinDatos=false
    var Encendido by mutableStateOf(false)
}