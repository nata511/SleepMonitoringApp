package com.example.sleepmonitoring.Visual.interfaces.ViesModelyDatas

data class DatosInicio(
    var nombre: String="",
    var correo:String="",
    var contraseña:String="",
    var errorNombre: String?=null,
    var errorCorreo: String?=null,
    var errorContraseña : String?=null,
    var  chulo: String?="",
    var cargando: Boolean=false
)
data class DatosRegis(
    var nombre: String="",
    var correo:String="",
    var contraseña:String="",
    var errorNombre: String?=null,
    var errorCorreo: String?=null,
    var errorContraseña : String?=null,
    var confirContra: String="",
    var errorConfir: String?="",
    var errorCoincidencia : String?=null,
    var cargando: Boolean=false
)


