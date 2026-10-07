    package com.example.sleepmonitoring.Controladores

    import com.example.sleepmonitoring.Analisis.DeteccionDimensiones.DimensionesSueño
    import com.example.sleepmonitoring.BaseDatos.SQLITE.AppDataBase
    import com.example.sleepmonitoring.BaseDatos.SQLITE.Consultas
    import com.example.sleepmonitoring.BasesDatosFirebase.ConsulFirebase
    import com.example.sleepmonitoring.BasesDatosFirebase.Usuario
    import com.example.sleepmonitoring.Visual.interfaces.ViesModelyDatas.DatosPrincipal
    import com.example.sleepmonitoring.Visual.interfaces.ViesModelyDatas.ViewModelPrincipal
    import kotlinx.coroutines.CoroutineScope
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.launch
    import android.content.Context
    import com.example.sleepmonitoring.BaseDatos.SQLITE.Horas
    import com.google.firebase.auth.FirebaseAuth
    import kotlinx.coroutines.tasks.await
    import kotlinx.coroutines.withContext

    object ControladorAlmacenaje {

        var base= ConsulFirebase
         lateinit var sql: Consultas
        fun inicializar(context: Context,horas: Horas) {
            if (!::sql.isInitialized) {
                sql = AppDataBase.getDataBase(context.applicationContext).ConsultasDao()
            }
            val scope=CoroutineScope(Dispatchers.IO)
            scope.launch {
                sql.InsertarHoras(horas)
            }

        }
        suspend fun puenteInicio(correo:String, contraseña:String): String?{

            var result: String?=""

            result = base.ObtenerDatosrUsuario(correo,contraseña)
            return if(result==null){
                null
            }else{
                 result
            }


        }
        suspend fun validarDatosUsuraio(): String?{
             val result = base.ObtenerUsuario()
            return if (result==null) {
               "Faltan Datos"
            } else {
               null
            }
        }

        suspend fun puenteRegistro(correo: String, contraseña: String): String? {
            val result = base.registrarUsuario(correo, contraseña)

            return if (result==null) {
                null
            } else {
                "Fallo el registro:${result}"
            }
        }

        fun regisUsuario(datos: Usuario){
            val scope= CoroutineScope(Dispatchers.IO)
            scope.launch {
                base.InsertarUsuario(datos)

        }
    }
        suspend fun ObtenerDatosPrincipal(): DatosPrincipal? = withContext(Dispatchers.IO) {
            val dato = base.ObtenDatosPrincipal() ?: return@withContext null
            val grafica = base.DatosGraficas()
            dato.copy(
                mutableList = grafica,
                promDura = ControladorHora.promDuracionsigno(grafica?.values?.toMutableList())
            )
        }

        suspend fun PasarRacha(): Int? = withContext(Dispatchers.IO) { base.ObtenerRacha() }

        suspend fun puenteRecuperacion(correo: String): String? {
            return try {
                FirebaseAuth.getInstance().sendPasswordResetEmail(correo).await()
                null
            } catch (e: Exception) {
                e.message ?: "Error al enviar el correo"
            }
        }
    }