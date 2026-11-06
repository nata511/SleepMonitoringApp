package com.example.sleepmonitoring

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.sleepmonitoring.Controladores.Navegacion

import com.example.sleepmonitoring.ui.theme.SleepMonitoringTheme
import com.google.firebase.auth.FirebaseAuth
import android.Manifest


class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    101
                )
            }
        }
        enableEdgeToEdge()
        if(auth.currentUser!=null){
            setContent {
                SleepMonitoringTheme {
                    Navegacion("Validacion")
                }
            }
        }else {
            setContent {
                SleepMonitoringTheme {
                    Navegacion("Inicio")
                }
            }
        }
    }



}