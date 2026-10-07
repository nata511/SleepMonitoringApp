package com.example.sleepmonitoring.Visual.interfaces.ViesModelyDatas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sleepmonitoring.BasesDatosFirebase.ConsulFirebase
import com.example.sleepmonitoring.BasesDatosFirebase.ResumenUsuarios
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelMenuTest: ViewModel() {
    val scope = CoroutineScope(Dispatchers.IO)
    var datos by mutableStateOf(ResumenUsuarios())
    var error by mutableStateOf(false)
    var faltaDatos by mutableStateOf(false)

    fun CambiarDatos(nuevosDatos: ResumenUsuarios) {
        datos = nuevosDatos

    }




}