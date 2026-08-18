package DeteccionSueño

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
import androidx.core.app.NotificationCompat

class SensorService : Service() , SensorEventListener{

    override fun onBind(intent: Intent): IBinder? = null
    fun crearNotificacion(): Notification {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val canal = NotificationChannel("Monitoreo","Monitoreando Actualmente", NotificationManager.IMPORTANCE_LOW)
            val system = getSystemService(NotificationManager::class.java).createNotificationChannel(canal)
        }
         return NotificationCompat.Builder(this, "Monitoreo")

            .setContentTitle("Monitoreando")
            .setContentText("Se esta monitoreando su sueño actualmente")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
             .setOngoing(true)
             .build()


    }
    private lateinit var SensorControl: SensorManager
    private var     Acelerometro: Sensor?= null

    override fun onCreate(){
        super.onCreate()
        SensorControl = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        Acelerometro = SensorControl.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
         var notifica = crearNotificacion()
        startForeground(1, notifica)
        Acelerometro?.let {
            SensorControl.registerListener(this,Acelerometro, SensorManager.SENSOR_DELAY_NORMAL)
        }
        return START_STICKY
    }

    override fun onSensorChanged(datos: SensorEvent?) {
        if(datos == null) {
            return
        }
        if(datos.sensor.type == Sensor.TYPE_ACCELEROMETER){
            var valores = DatosAcelerometro(datos.timestamp, datos.values[0], datos.values[1],datos.values[2])
            retornarDatos(valores)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SensorControl.unregisterListener(this)
    }
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        TODO("Not yet implemented")
    }
    fun retornarDatos(Datos: DatosAcelerometro): DatosAcelerometro{
        return Datos
    }
}