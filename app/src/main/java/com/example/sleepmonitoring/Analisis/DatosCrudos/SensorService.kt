    package com.example.sleepmonitoring.Analisis.DatosCrudos
    
    import android.R
    import android.app.Notification
    import android.app.NotificationChannel
    import android.app.NotificationManager
    import android.app.Service
    import android.content.Context
    import android.content.Intent
    import android.hardware.Sensor
    import android.hardware.SensorEvent
    import android.hardware.SensorEventListener
    import android.hardware.SensorManager
    import android.os.Build
    import android.os.IBinder
    import android.os.PowerManager
    import android.os.SystemClock
    import android.util.Log
    import androidx.core.app.NotificationCompat
    import com.example.sleepmonitoring.Analisis.DatosCrudos.HDZC.EpocasHDZC
    import com.example.sleepmonitoring.Analisis.DatosCrudos.MAD.EpocasMAD
    import com.example.sleepmonitoring.BaseDatos.SQLITE.AppDataBase
    import com.example.sleepmonitoring.BaseDatos.SQLITE.DatoNoche
    import com.example.sleepmonitoring.BasesDatosFirebase.DatosConstantes
    import com.example.sleepmonitoring.Controladores.ControladorDatosCrudos
    import kotlinx.coroutines.CoroutineExceptionHandler
    import kotlinx.coroutines.CoroutineScope
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.Job
    import kotlinx.coroutines.SupervisorJob
    import kotlinx.coroutines.cancel
    import kotlinx.coroutines.channels.BufferOverflow
    import kotlinx.coroutines.flow.MutableSharedFlow
    import kotlinx.coroutines.flow.first
    import kotlinx.coroutines.joinAll
    import kotlinx.coroutines.launch
    import kotlinx.coroutines.withTimeoutOrNull
    import java.time.LocalDate
    import java.time.format.DateTimeFormatter
    
    class SensorService : Service(), SensorEventListener {
    
        companion object {
    
            const val ACCION_STOP = "STOP"
            @Volatile var corriendo = false
            @Volatile var procesando = false
            val activo: Boolean get() = corriendo || procesando
        }
    
        private val manejadorErrores = CoroutineExceptionHandler { _, e ->
            Log.e("SERVICE", "Error en corrutina del servicio", e)
        }
    
        var contadorLogs = 0
        private val banda = MutableSharedFlow<DatosAcelerometro>(
            replay = 0,
            extraBufferCapacity = 2048,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
        private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO + manejadorErrores)
        private var jobs: List<Job> = emptyList()
        private var epocasMad: EpocasMAD? = null
        private var wakeLock: PowerManager.WakeLock? = null
        private var fechaSesion: String = ""
    
        private val base by lazy { AppDataBase.Companion.getDataBase(applicationContext).ConsultasDao() }
    
        private lateinit var SensorControl: SensorManager
        private var Acelerometro: Sensor? = null
    
        override fun onBind(intent: Intent): IBinder? = null
    
        override fun onCreate() {
            super.onCreate()
            SensorControl = getSystemService(SENSOR_SERVICE) as SensorManager
            Acelerometro = SensorControl.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        }
    
        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            startForeground(1, crearNotificacion())
    
            if (intent == null) {                 // reinicio automático del sistema: no revivir la sesión
                stopSelf()
                return START_NOT_STICKY
            }
            if (intent.action == ACCION_STOP) finalizar() else iniciar()
            return START_NOT_STICKY
        }
    
        private fun iniciar() {
            if (corriendo || procesando) return

            corriendo = true
            adquirirWakeLock()
            DatosConstantes.Encendido=true
                fechaSesion = LocalDate.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    
            DatosConstantes.horaInicio = SystemClock.elapsedRealtimeNanos()
            DatosConstantes.sinDatos=false
            scope.launch {
    
                base.InsertarDatoNoche(DatoNoche(fecha = fechaSesion, estado = "proceso"))
    
                val hdzc = EpocasHDZC(applicationContext, banda)
    
                val mad = EpocasMAD(applicationContext, banda)
    
                epocasMad = mad
    
                jobs = listOf(
                    launch {
                        mad.agregar()
                    },
                    launch {
                        hdzc.agregar()
                    }
                )
    
                withTimeoutOrNull(5_000L) {
                    banda.subscriptionCount.first { it >= 2 }
                }
    
                if (corriendo) {
                    Acelerometro?.let {
                        SensorControl.registerListener(
                            this@SensorService,
                            it,
                            SensorManager.SENSOR_DELAY_GAME
                        )
                    }
                }
            }
        }
    
        private fun finalizar() {
            if (!corriendo) {
                if (!procesando) stopSelf()
                return
            }
            DatosConstantes.Encendido=false
            DatosConstantes.sinDatos=true
            DatosConstantes.cuespsqi=false
            DatosConstantes.somno=false
            DatosConstantes.horaFin= SystemClock.elapsedRealtimeNanos()
    
    
            corriendo = false
            procesando = true
            SensorControl.unregisterListener(this)
    
            scope.launch {
                base.actualizarFecha(fechaSesion, DatosConstantes.fecha)
                try {
                    banda.emit(DatosAcelerometro(-1L, -1.0F, -1.0F, -1.0F))   // centinela
                    jobs.joinAll()
                    ControladorDatosCrudos.tiempoI = epocasMad?.tiempoI ?: 0L
    
                    ControladorDatosCrudos.procesarAngulosMovimientos(applicationContext) { _ ->
                        terminar()
                    }
                } catch (e: Throwable) {
                    Log.e("SERVICE", "error al finalizar", e)
                    terminar()
                }
            }
        }
    
        private fun terminar() {
            procesando = false
            liberarWakeLock()
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }
    
        override fun onSensorChanged(datos: SensorEvent?) {
            if (datos == null || datos.sensor.type != Sensor.TYPE_ACCELEROMETER) return
            banda.tryEmit(
                DatosAcelerometro(
                    datos.timestamp,
                    datos.values[0],
                    datos.values[1],
                    datos.values[2]
                )
            )
            contadorLogs++
            if (contadorLogs % 100 == 0) {
                Log.d("SENSOR_CHECK", "Sensor activo. x: ${datos.values[0]}, y: ${datos.values[1]}, z: ${datos.values[2]},${datos.timestamp} | ¿Emitido a SharedFlow?: ")
            }
        }
    
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    
        override fun onDestroy() {
            super.onDestroy()
            SensorControl.unregisterListener(this)
            liberarWakeLock()
            corriendo = false
            procesando = false
            scope.cancel()
        }
    
        private fun adquirirWakeLock() {
            val pm = getSystemService(POWER_SERVICE) as PowerManager
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SleepMonitoring::Sensor").apply {
                acquire(12 * 60 * 60 * 1000L)     // tope de seguridad: 12 horas
            }
        }
    
        private fun liberarWakeLock() {
            wakeLock?.takeIf { it.isHeld }?.release()
            wakeLock = null
        }
    
        fun crearNotificacion(): Notification {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val canal = NotificationChannel(
                    "Monitoreo",
                    "Monitoreando Actualmente",
                    NotificationManager.IMPORTANCE_LOW
                )
                getSystemService(NotificationManager::class.java)?.createNotificationChannel(canal)
            }
            return NotificationCompat.Builder(this, "Monitoreo")
                .setSmallIcon(R.drawable.ic_dialog_info)   // provisional, ver abajo
                .setContentTitle("Monitoreando")
                .setContentText("Se esta monitoreando su sueño actualmente")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .build()
        }
    }